package ranking;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.Match;
import data.Ranking;
import data.Team;
import tba.Conversions;
import util.Algorithms;
import util.CSVParser;
import util.ComparatorFactory;

/** Represents an event */
public class Event extends Rankable {
    private final String name;
    private final Date startDate;
    private final List<Match> matches; // The matches in this event

    /** {teamNumber: rankingScore} */
    private final ArrayList<Ranking> rankingScores = new ArrayList<>();
    private final HashMap<Team, Integer> teamDistrictPoints = new HashMap<>();

    @SuppressWarnings("unchecked")
    public Event(
        String name, 
        Date startDate, 
        List<Match> matches, 
        List<Integer> teamNumbers, 
        List<HashMap<String, Object>> rankingScoresJson,
        Map<String, Map<String, Integer>> districtPointsMap
    ){
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
        for(var teamRankingScoreInfo : rankingScoresJson) {
            int teamNumber = Conversions.teamNumberFromKey((String) teamRankingScoreInfo.get("team_key"));
            Team team = Algorithms.binarySearch(teams, ComparatorFactory.ascendingSearchComparator(teamNumber, Team::getTeamNum)).get();
            double rankingScore = ((List<Double>) teamRankingScoreInfo.get("sort_orders")).get(0);

            rankingScores.add(new Ranking(team, rankingScore));
        }
        
        for(var entry : districtPointsMap.entrySet()) {
            int teamNumber = Conversions.teamNumberFromKey(entry.getKey());
            Team team = Algorithms.binarySearch(teams, ComparatorFactory.ascendingSearchComparator(teamNumber, Team::getTeamNum)).get();
            int districtPoints = entry.getValue().get("total");

            teamDistrictPoints.put(team, districtPoints);
        }
    }

    public String getName() {
        return name;
    }

    public List<Match> getMatches(){
        return matches;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Map<Team, Integer> getDistrictPointsMap() {
        return teamDistrictPoints;
    }

    @Override
    public List<Ranking> getRankings() {
        return rankingScores;
    }
}
