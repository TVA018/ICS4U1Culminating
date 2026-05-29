package ranking;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import data.Match;
import data.Ranking;
import data.Team;
import data.robot.DefenceBot;
import util.Algorithms;
import util.CSVParser;

/** Represents an event */
public class Event implements Rankable{
    private final String name;
    private Date startDate;
    private List<Match> matches; // The matches in this event
    private List<Team> teams = new ArrayList<>();

    public Event(String name, Date startDate, List<Match> matches, List<Integer> teamNumbers){
        this.name = name;
        this.startDate = startDate;
        this.matches = matches;

        int teamObjectListIndex = 0;

        List<Team> allTeamsObjects = CSVParser.getTeams();

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
    }

    public String getName() {
        return name;
    }

    public List<Match> getMatches(){
        return matches;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public Date getStartDate() {
        return startDate;
    }

    /** 
     * @param onlyIncludeShooters option to exclude bias from teams who do not score
     * @return a sorted ArrayList of the rankings from highest MAD descending
     */
    @Override
    public ArrayList<Ranking> getMADRankings(boolean onlyIncludeShooters) {
        List<Team> allTeams = getTeams();
        List<Team> validTeams;


        if(onlyIncludeShooters){
            validTeams = Algorithms.filter(allTeams, team -> (team.getRobot() instanceof DefenceBot));
        } else {
            validTeams = allTeams;
        }
        
        ArrayList<Ranking> madRanks = new ArrayList<>(validTeams.size());

        for(Team team : validTeams) {
            madRanks.add(new Ranking(team, team.calculateMAD(0.96)));
        }

        Algorithms.mergeSort(madRanks, (ranking1, ranking2) -> (int) (ranking2.points - ranking1.points));

        return madRanks;
    }

    public List<Ranking> getRankings() {
        // TODO Implement TBA
        return null;
    }
    
}
