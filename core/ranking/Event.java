package core.ranking;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.data.Match;
import core.data.Ranking;
import core.data.Team;
import util.Algorithms;
import util.CSVParser;
import util.ComparatorFactory;

/** Represents an event */
public class Event extends Rankable {
    private final String key;
    private final String name;
    private final LocalDate startDate;
    private final List<Match> matches; // The matches in this event

    /** {teamNumber: rankingScore} */
    private final ArrayList<Ranking> rankingScores = new ArrayList<>();
    private final HashMap<Team, Integer> teamDistrictPoints = new HashMap<>();

    public Event(
        String eventKey,
        String name,
        LocalDate startDate, 
        List<Match> matches, 
        List<Integer> teamNumbers, 
        HashMap<Integer, Double> rankingScoresMap,
        HashMap<Integer, Integer> districtPointsMap
    ){
        this.key = eventKey;
        this.name = name;
        this.startDate = startDate;
        this.matches = matches;

        int teamObjectListIndex = 0;

        List<Team> allTeamsObjects = CSVParser.getTeams();

        // Iterate over the List of team numbers and List of Team objects side-by-side and 
        // add the Team object if the team number intersect
        for (int teamNumber: teamNumbers) {
            Team currentTeam = allTeamsObjects.get(teamObjectListIndex);

            while (currentTeam.getTeamNum() != teamNumber) {
                teamObjectListIndex++;

                if(teamObjectListIndex > allTeamsObjects.size()) throw new RuntimeException("Failed to find team " + teamNumber);

                currentTeam = allTeamsObjects.get(teamObjectListIndex);
            }

            teams.add(currentTeam);
            teamObjectListIndex++;
        }

        // Load rankings
        for(var entry : rankingScoresMap.entrySet()) {
            Team team = Algorithms.binarySearch(teams, ComparatorFactory.ascendingSearchComparator(entry.getKey(), Team::getTeamNum)).get();
            double rankingScore = entry.getValue();

            rankingScores.add(new Ranking(team, rankingScore));
        }
        
        for(var entry : districtPointsMap.entrySet()) {
            int teamNumber = entry.getKey();
            Team team = Algorithms.binarySearch(teams, ComparatorFactory.ascendingSearchComparator(teamNumber, Team::getTeamNum)).get();
            int districtPoints = entry.getValue();

            teamDistrictPoints.put(team, districtPoints);
        }
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public List<Match> getMatches(){
        return matches;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Map<Team, Integer> getDistrictPointsMap() {
        return teamDistrictPoints;
    }

    @Override
    public List<Ranking> getRankings() {
        return rankingScores;
    }

    @Override
    public String toString() {
        return name;
    }
}
