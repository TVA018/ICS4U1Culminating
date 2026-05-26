package ranking;

import java.util.ArrayList;
import java.util.List;
import data.Ranking;
import data.Team;

public class District implements Rankable{
    private List<Event> events;

    public District(List<Event> events){
        this.events = events;
    }

    @Override
    public List<Team> getTeams() {
        // TODO import from TBA
        throw new UnsupportedOperationException("Unimplemented method 'getTeams'");
    }

    @Override
    public ArrayList<Team> getMADRankings(boolean onlyIncludeShooters) {
        ArrayList<Team> madRanks = new ArrayList<>();
        if(onlyIncludeShooters){

        } else {

        }

        return madRanks;
    }
    

    @Override
    public List<Ranking> getRankings() {
        // TODO loop through all events and sim event district points then sum and rank
        throw new UnsupportedOperationException("Unimplemented method 'getRankings'");
    }
}
