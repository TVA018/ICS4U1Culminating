package data;

import java.util.ArrayList;

import data.enums.WinningAlliance;

public class Match {
    private final int matchNumber;
    private final ArrayList<Integer> redTeams;
    private final ArrayList<Integer> blueTeams;
    private final int redScore;
    private final int blueScore;
    private final boolean qualifier;
    private final WinningAlliance winner;

    public Match(int matchNumber, boolean isQualifier, ArrayList<Integer> redTeams, ArrayList<Integer> blueTeams, int redScore, int blueScore, WinningAlliance winner){
        this.matchNumber = matchNumber;
        this.redTeams = redTeams;
        this.blueTeams = blueTeams;
        this.redScore = redScore;
        this.blueScore = blueScore;
        this.winner = winner;
        this.qualifier = isQualifier;
    }

    public ArrayList<Integer> getRedTeams() {
        return redTeams;
    }

    public ArrayList<Integer> getBlueTeams() {
        return blueTeams;
    }

    public int getRedScore() {
        return redScore;
    }

    public int getBlueScore() {
        return blueScore;
    }

    public boolean isQualifier() {
        return qualifier;
    }

    public WinningAlliance getWinner(){
        return winner;
    }
}
