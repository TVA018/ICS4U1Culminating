package ranking;

import java.util.ArrayList;
import java.util.List;

import data.Match;
import data.Ranking;
import data.Team;
import data.robot.DefenceBot;
import util.Algorithms;

public class Event implements Rankable{
    private List<Match> matches;

    public Event(List<Match> matches){
        this.matches = null; // import from tba
    }

    public List<Match> getMatches(){
        return matches;
    }

    public List<Team> getTeams() {
        // TODO Implement TBA
        return null;
    }

    /** 
     * @param onlyIncludeShooters option to exclude bias from teams who do not score
     * @return a sorted ArrayList of the rankings from highest MAD descending
     */
    @Override
    public ArrayList getMADRankings(boolean onlyIncludeShooters) {
        ArrayList<Team> madRanks = new ArrayList<>();
        if(onlyIncludeShooters){
            for (Team team : getTeams()){ // TODO make proper list
                if(!(team.getRobot() instanceof DefenceBot)){
                    madRanks.add(team);
                }
            }
        } else {
            madRanks.addAll(getTeams());
        }
        Algorithms.mergeSort(madRanks, null); // TODO add comparator
        return madRanks;
    }

    public List<Ranking> getRankings() {
        // TODO Implement TBA
        return null;
    }
    
}
