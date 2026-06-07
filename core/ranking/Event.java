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
    private final List<Match> matches;

    /** {teamNumber: rankingScore} */
    private final ArrayList<Ranking> rankingScores = new ArrayList<>();
    private final HashMap<Team, Integer> teamDistrictPoints = new HashMap<>();

    /**
     * Constructs a new event
     * @param eventKey The key of the event
     * @param name The name of the event
     * @param startDate The date the event started
     * @param matches The matches in the event
     * @param teamNumbers The teams in the event represented by their team number
     * @param rankingScoresMap A map which maps a team number to their ranking score
     * @param districtPointsMap A map which maps a team number to their achieved district points from this event
     */
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

        Algorithms.mergeSort(rankingScores, (r1, r2) -> (int) ((r2.getPoints() - r1.getPoints()) * 100));
        
        // Load district points
        for(var entry : districtPointsMap.entrySet()) {
            int teamNumber = entry.getKey();
            Team team = Algorithms.binarySearch(teams, ComparatorFactory.ascendingSearchComparator(teamNumber, Team::getTeamNum)).get();
            int districtPoints = entry.getValue();

            teamDistrictPoints.put(team, districtPoints);
        }
    }

    /** @return the event key */
    public String getKey() {
        return key;
    }

    /** @return the event name */
    public String getName() {
        return name;
    }

    /** @return the matches in the event */
    public List<Match> getMatches(){
        return matches;
    }

    /** @return the date the event started */
    public LocalDate getStartDate() {
        return startDate;
    }

    /** @return a mapping which maps a team to the number of district points they achived from this event */
    public Map<Team, Integer> getDistrictPointsMap() {
        return teamDistrictPoints;
    }

    /** @return the rankings of teams in this event in ascending order (1 -> n) */
    @Override
    public List<Ranking> getRankings() {
        return rankingScores;
    }

    @Override
    public String toString() {
        return name;
    }
}
