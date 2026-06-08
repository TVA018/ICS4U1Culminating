package core;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import core.data.Constants;
import core.data.Match;
import core.data.Team;
import core.data.enums.WinningAlliance;
import core.ranking.Event;
import util.Algorithms;
import util.CSVParser;
import util.ComparatorFactory;

public class Simulation {
    private record MatchConfig(ArrayList<Integer> redTeamNums, ArrayList<Integer> blueTeamNums) {}

    private String eventKey;
    private String eventName;

    private int energizedThreshold;
    private int superchargedThreshold;
 
    private ArrayList<Team> teams;
    private ArrayList<Integer> teamNumbers;
    private ArrayList<MatchConfig> matchConfigs = new ArrayList<>();

    /**
     * Creates a new event simulation
     * 
     * The event info is in the following format:
     * 
     * <pre>
     * eventKey
     * eventName
     * energizedThreshold,superchargedThreshold
     * team1Number,team2Number,team3Number,...
     * match1RedTeam1,match1RedTeam2,match1RedTeam3,match1BlueTeam1,match1BlueTeam2,match1BlueTeam3
     * match2RedTeam1,match2RedTeam2,match2RedTeam3,match2BlueTeam1,match2BlueTeam2,match2BlueTeam3
     * ...
     * </pre>
     * 
     * @param eventFile The file to read from
     * @throws IOException If the file couldn't be parsed properly
     */
    public Simulation(String eventFile) throws IOException {
        // Open the file
        try(BufferedReader reader = new BufferedReader(new FileReader(eventFile))) {
            this.eventKey = reader.readLine();
            this.eventName = reader.readLine();
            String[] rpThresholds = reader.readLine().split(",", 2);
            this.energizedThreshold = Integer.parseInt(rpThresholds[0]);
            this.superchargedThreshold = Integer.parseInt(rpThresholds[1]);

            String[] teamNumberStrings = reader.readLine().split(",");

            teams = new ArrayList<>(teamNumberStrings.length);
            teamNumbers = new ArrayList<>(teamNumberStrings.length);

            // Add all the team objects and check whether the team numbers provided are valid
            for(String str : teamNumberStrings) {
                int teamNumber = Integer.parseInt(str); 
                teamNumbers.add(teamNumber);

                Optional<Team> teamOptional = Algorithms.binarySearch(CSVParser.getTeams(), ComparatorFactory.ascendingSearchComparator(teamNumber, Team::getTeamNum));

                if(teamOptional.isEmpty()) throw new IllegalArgumentException("Could not find team " + str);

                teams.add(teamOptional.get());
            }

            // Sort the list of teams
            Algorithms.mergeSort(teamNumbers, (t1, t2) -> t1 - t2);

            // Read remaining rows (match info)
            String row = reader.readLine();

            while(row != null) {
                String[] allTeamNums = row.split(",", 6); // List of teams, first 3 are red alliance, last 3 are blue alliance
                ArrayList<Integer> redTeams = new ArrayList<>(3);
                ArrayList<Integer> blueTeams = new ArrayList<>(3);

                // Add the team numbers
                for(int i = 0; i < 3; i++) {
                    redTeams.add(Integer.parseInt(allTeamNums[i]));
                }

                for(int i = 0; i < 3; i++) {
                    blueTeams.add(Integer.parseInt(allTeamNums[i + 3]));
                }

                // Add the config
                matchConfigs.add(new MatchConfig(redTeams, blueTeams));

                row = reader.readLine();
            }
        }
    }

    /**
     * Simulates this event
     * @return The simulation results as an Event object
     */
    public Event simulate() {
        HashMap<Integer, Integer> totalRankingPoints = new HashMap<>(); // {teamNumber: rp}
        HashMap<Integer, Double> rankingScoresMap = new HashMap<>(); // {teamNumber: rankingScore}
        HashMap<Integer, Double> teamMADs = new HashMap<>(); // {teamNumber: MAD}
        HashMap<Integer, Integer> numMatchesPlayed = new HashMap<>(); // {teamNumber: numMatches}
        ArrayList<Match> matches = new ArrayList<>(matchConfigs.size());

        // Cache the MAD values so we don't have to constantly recalculate
        for (Team team : teams) {
            teamMADs.put(team.getTeamNum(), team.calculateMAD(Constants.MAD_FACTOR));
        }

        // Simulate qualifier matches
        for(int i = 0; i < matchConfigs.size(); i++) {
            MatchConfig matchConfig = matchConfigs.get(i);

            ArrayList<Integer> redTeams = matchConfig.redTeamNums();
            ArrayList<Integer> blueTeams = matchConfig.blueTeamNums();

            // Calculate scores
            int redScore = (int) (
                teamMADs.get(redTeams.get(0)) +
                teamMADs.get(redTeams.get(1)) +
                teamMADs.get(redTeams.get(2))
            );

            int blueScore = (int) (
                teamMADs.get(blueTeams.get(0)) +
                teamMADs.get(blueTeams.get(1)) +
                teamMADs.get(blueTeams.get(2))
            );
            
            WinningAlliance winningAlliance = (redScore > blueScore) ? WinningAlliance.RED : ((blueScore > redScore) ? WinningAlliance.BLUE : WinningAlliance.TIE);

            // Create match object
            Match match = new Match(
                i + 1, 
                true, 
                redTeams, 
                blueTeams, 
                redScore, 
                blueScore, 
                winningAlliance
            );

            // Calculate RP
            int redRP = calculateRP(redScore, winningAlliance.equals(WinningAlliance.RED));
            int blueRP = calculateRP(blueScore, winningAlliance.equals(WinningAlliance.BLUE));
            
            for(int teamNum : redTeams) {
                numMatchesPlayed.put(teamNum, numMatchesPlayed.getOrDefault(teamNum, 0) + 1);
                totalRankingPoints.put(teamNum, totalRankingPoints.getOrDefault(teamNum, 0) + redRP);
            }

            for(int teamNum : blueTeams) {
                numMatchesPlayed.put(teamNum, numMatchesPlayed.getOrDefault(teamNum, 0) + 1);
                totalRankingPoints.put(teamNum, totalRankingPoints.getOrDefault(teamNum, 0) + blueRP);
            }

            // Add match to the list
            matches.add(match);
        }

        // Calculate ranking score from total ranking points and matches played
        for(var entry : totalRankingPoints.entrySet()) {
            int teamNum = entry.getKey();
            rankingScoresMap.put(teamNum, ((double) entry.getValue()) / numMatchesPlayed.get(teamNum));
        }

        // Return the simulated results
        return new Event(
            eventKey, 
            eventName, 
            LocalDate.now(), 
            matches, 
            teamNumbers, 
            rankingScoresMap, 
            new HashMap<>() // Don't calculate district points
        );
    }

    /**
     * Calculates the RP earned by an alliance in a given match. <br>
     * <br>
     * Here is how RP is calculated:<br>
     * <pre>
     * |              Objective              | Ranking Points Earned |
     * ---------------------------------------------------------------
     * |              Win Match              |           3           |
     * |      Score X fuel (Energized)       |           1           |
     * | Score Y fuel (Y > X) (Supercharged) |           1           |
     * </pre>
     * 
     * @param score
     * @param wonMatch
     * @return
     */
    private int calculateRP(int score, boolean wonMatch) {
        int rp = wonMatch ? 3 : 0;

        if(score >= energizedThreshold) rp++;
        if(score >= superchargedThreshold) rp++;

        return rp;
    }
}
