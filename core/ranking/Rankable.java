package core.ranking;

import java.util.ArrayList;
import java.util.List;

import core.data.Ranking;
import core.data.Team;
import core.data.robot.DefenceBot;
import util.Algorithms;

public abstract class Rankable {
    protected final List<Team> teams = new ArrayList<>();

    /** @return the teams in this object */
    public List<Team> getTeams() {
        return teams;
    }

    /** @return the ranking of teams in this object sorted by their MAD in descending order (highest MAD first) */
    public List<Ranking> getMADRankings(double factor, boolean onlyIncludeShooters) {
        List<Team> allTeams = getTeams();
        List<Team> validTeams;


        if(onlyIncludeShooters){
            validTeams = Algorithms.filter(allTeams, team -> (team.getRobot() instanceof DefenceBot));
        } else {
            validTeams = allTeams;
        }
        
        ArrayList<Ranking> madRanks = new ArrayList<>(validTeams.size());

        for(Team team : validTeams) {
            madRanks.add(new Ranking(team, team.calculateMAD(factor)));
        }

        Algorithms.mergeSort(madRanks, (ranking1, ranking2) -> (int) ((ranking2.getPoints() - ranking1.getPoints()) * 1000));

        return madRanks;
    }

    /** @return the ranking of teams in this object sorted by their rank in ascending order (rank 1 first, rank 2 second, etc.) */
    public abstract List<Ranking> getRankings();
}
