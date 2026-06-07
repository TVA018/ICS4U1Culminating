package core.data;

public class Ranking {
    private final Team team;
    private final double points;

    /**
     * Creates a new ranking
     * @param team The team in this ranking
     * @param points How many points this team achieved
     */
    public Ranking(Team team, double points){
        this.team = team;
        this.points = points;
    }

    /** 
     * @return the team associated with the ranking
     */
    public Team getTeam() {
        return team;
    }

    /** 
     * @return return a number of points
     * this amount of points is generic and can be for different statistics
     */
    public double getPoints() {
        return points;
    }
}
