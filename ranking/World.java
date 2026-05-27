package ranking;

import java.util.ArrayList;
import java.util.List;

import data.Ranking;
import data.Team;
import data.robot.DefenceBot;
import util.Algorithms;

public class World implements Rankable{

    @Override
    public List<Team> getTeams() {
        // TODO Import TBA
        throw new UnsupportedOperationException("Unimplemented method 'getTeams'");
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

    @Override
    public List<Ranking> getRankings() {
        // TODO Import TBA
        throw new UnsupportedOperationException("Unimplemented method 'getRankings'");
    }
    
}
