package ranking;

public interface Rankable {
    public Team[] getTeams();
    public Ranking[] getMADRankings(boolean onlyIncludeShooters);
    public Ranking[] getRankings()
}
