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

public class Simulator {
    private record MatchConfig(ArrayList<Integer> redTeamNums, ArrayList<Integer> blueTeamNums) {}

    private String eventKey;
    private String eventName;

    private int energizedThreshold;
    private int superchargedThreshold;
 
    private ArrayList<Team> teams;
    private ArrayList<Integer> teamNumbers;
    private ArrayList<MatchConfig> matchConfigs = new ArrayList<>();

    public Simulator(String eventFile) throws IOException {
        try(BufferedReader reader = new BufferedReader(new FileReader(eventFile))) {
            this.eventKey = reader.readLine();
            this.eventName = reader.readLine();
            String[] rpThresholds = reader.readLine().split(",", 2);
            this.energizedThreshold = Integer.parseInt(rpThresholds[0]);
            this.superchargedThreshold = Integer.parseInt(rpThresholds[1]);

            String[] teamNumberStrings = reader.readLine().split(",");

            teams = new ArrayList<>(teamNumberStrings.length);
            teamNumbers = new ArrayList<>(teamNumberStrings.length);

            for(String str : teamNumberStrings) {
                int teamNumber = Integer.parseInt(str); 
                teamNumbers.add(teamNumber);

                Optional<Team> teamOptional = Algorithms.binarySearch(CSVParser.getTeams(), ComparatorFactory.ascendingSearchComparator(teamNumber, Team::getTeamNum));

                if(teamOptional.isEmpty()) throw new IllegalArgumentException("Could not find team " + str);

                teams.add(teamOptional.get());
            }

            Algorithms.mergeSort(teamNumbers, (t1, t2) -> t1 - t2);

            List<String> matchRows = reader.readAllLines();

            for(String row : matchRows) {
                String[] allTeamNums = row.split(",", 6);
                ArrayList<Integer> redTeams = new ArrayList<>(3);
                ArrayList<Integer> blueTeams = new ArrayList<>(3);

                for(int i = 0; i < 3; i++) {
                    redTeams.add(Integer.parseInt(allTeamNums[i]));
                }

                for(int i = 0; i < 3; i++) {
                    blueTeams.add(Integer.parseInt(allTeamNums[i + 3]));
                }

                matchConfigs.add(new MatchConfig(redTeams, blueTeams));
            }
        }
    }

    public Event simulate() {
        HashMap<Integer, Integer> totalRankingPoints = new HashMap<>(); // {teamNumber: rp}
        HashMap<Integer, Double> rankingScoresMap = new HashMap<>(); // {teamNumber: rankingScore}
        HashMap<Integer, Double> teamMADs = new HashMap<>(); // {teamNumber: MAD}
        HashMap<Integer, Integer> numMatchesPlayed = new HashMap<>(); // {teamNumber: numMatches}
        ArrayList<Match> matches = new ArrayList<>(matchConfigs.size());

        for (Team team : teams) {
            teamMADs.put(team.getTeamNum(), team.calculateMAD(Constants.MAD_FACTOR));

        }

        for(int i = 0; i < matchConfigs.size(); i++) {
            MatchConfig matchConfig = matchConfigs.get(i);

            ArrayList<Integer> redTeams = matchConfig.redTeamNums();
            ArrayList<Integer> blueTeams = matchConfig.blueTeamNums();

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

            Match match = new Match(
                i + 1, 
                true, 
                redTeams, 
                blueTeams, 
                redScore, 
                blueScore, 
                winningAlliance
            );

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

            matches.add(match);
        }

        for(var entry : totalRankingPoints.entrySet()) {
            int teamNum = entry.getKey();
            rankingScoresMap.put(teamNum, ((double) entry.getValue()) / numMatchesPlayed.get(teamNum));
        }

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

    private int calculateRP(int score, boolean wonMatch) {
        int rp = wonMatch ? 3 : 0;

        if(score >= energizedThreshold) rp++;
        if(score >= superchargedThreshold) rp++;

        return rp;
    }
}
