package data;

public class Ranking {
    public Team team;
    public int points;

    public Ranking(Team team, int points, double MAD){
        this.team = team;
        this.points = points;
    }

    public Team getTeam() {
        return team;
    }

    public int getPoints() {
        return points;
    }
    
}
