package ranking;

import java.util.ArrayList;
import java.util.List;

import data.Ranking;
import data.Team;

public interface Rankable {
    public List<Team> getTeams();
    public ArrayList<Ranking> getMADRankings(double factor, boolean onlyIncludeShooters);
    public List<Ranking> getRankings();
}
