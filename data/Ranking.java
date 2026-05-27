package data;

public class Ranking {
    public Team team;
    public double points;

    public Ranking(Team team, int points){
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
