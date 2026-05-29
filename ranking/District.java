package ranking;

import java.util.ArrayList;
import java.util.List;
import data.Ranking;
import data.Team;
import data.robot.*;
import util.Algorithms;

public class District implements Rankable{
    private List<Event> events;

    public District(List<Event> events){
        this.events = events;
    }

    @Override
    public List<Team> getTeams() {
        ArrayList<Team> teams = new ArrayList<>();

        for(Event event : events) {
            List<Team> eventTeams = event.getTeams();
        }
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
        // TODO loop through all events and sim event district points then sum and rank
        throw new UnsupportedOperationException("Unimplemented method 'getRankings'");
    }
}
