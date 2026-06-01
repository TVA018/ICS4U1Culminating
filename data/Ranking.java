package data;

public class Ranking {
    private final Team team;
    private final double points;

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
