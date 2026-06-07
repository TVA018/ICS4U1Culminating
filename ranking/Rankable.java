package ranking;

import java.util.ArrayList;
import java.util.List;

import data.Ranking;
import data.Team;
import data.robot.DefenceBot;
import util.Algorithms;

public abstract class Rankable {
    protected final List<Team> teams = new ArrayList<>();

    public List<Team> getTeams() {
        return teams;
    }

    public List<Ranking> getMADRankings(double factor, boolean onlyIncludeShooters) {
        List<Team> allTeams = getTeams();
        List<Team> validTeams;


        if(onlyIncludeShooters){
            validTeams = Algorithms.filter(allTeams, team -> !(team.getRobot() instanceof DefenceBot));
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

    public abstract List<Ranking> getRankings();
}
