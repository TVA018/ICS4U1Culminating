package ranking;

import java.util.List;

import data.Match;
import data.Ranking;
import data.Team;

public class Event implements Rankable{
    private List<Match> matches;

    public Event(List<Match> matches){
        this.matches = null; // import from tba
    }

    public List<Match> getMatches(){
        return matches;
    }

    public List<Team> getTeams() {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Ranking> getMADRankings(boolean onlyIncludeShooters) {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Ranking> getRankings() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
