package ranking;

import java.util.List;

import data.Ranking;
import data.Team;

public interface Rankable {
    public List<Team> getTeams();
    public List<Ranking> getMADRankings(boolean onlyIncludeShooters);
    public List<Ranking> getRankings();
}
