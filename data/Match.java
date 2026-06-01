package data;

import java.util.ArrayList;

import data.enums.WinningAlliance;
import util.Algorithms;
import util.CSVParser;

public class Match {
    private final int matchNumber;
    private final ArrayList<Team> redTeams = new ArrayList<>();
    private final ArrayList<Team> blueTeams = new ArrayList<>();
    private final int redScore;
    private final int blueScore;
    private final boolean qualifier;
    private final WinningAlliance winner;

    public Match(int matchNumber, boolean isQualifier, ArrayList<Integer> redTeams, ArrayList<Integer> blueTeams, int redScore, int blueScore, WinningAlliance winner){
        this.matchNumber = matchNumber;
        this.redScore = redScore;
        this.blueScore = blueScore;
        this.winner = winner;
        this.qualifier = isQualifier;

        for(int teamNum : redTeams) {
            var teamOpt = CSVParser.getTeam(teamNum);

            if(teamOpt.isEmpty()) {
                System.err.printf("Team %s could not be found\n", teamNum);
                continue;
            }

            this.redTeams.add(teamOpt.get());
        }

        
        for(int teamNum : blueTeams) {
            var teamOpt = CSVParser.getTeam(teamNum);

            if(teamOpt.isEmpty()) {
                System.err.printf("Team %s could not be found\n", teamNum);
                continue;
            }

            this.blueTeams.add(teamOpt.get());
        }
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    public ArrayList<Team> getRedTeams() {
        return redTeams;
    }

    public ArrayList<Team> getBlueTeams() {
        return blueTeams;
    }

    /** 
     * @return returns the red score as an int
     */
    public int getRedScore() {
        return redScore;
    }

    /** @return the mean of the red score */
    public double getMeanRedScore() {
        return ((double) redScore) / 3;
    }

    /** 
     * @return returns the blue score as an int
     */
    public int getBlueScore() {
        return blueScore;
    }

    /** @return the mean of the blue score */
    public double getMeanBlueScore() {
        return ((double) blueScore) / 3;
    }

    public boolean isQualifier() {
        return qualifier;
    }

    public WinningAlliance getWinner(){
        return winner;
    }
}
