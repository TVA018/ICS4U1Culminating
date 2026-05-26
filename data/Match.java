package data;

import java.util.ArrayList;

import data.enums.WinnerType;

public class Match {
    private ArrayList<Team> redTeams = new ArrayList<>();
    private ArrayList<Team> blueTeams = new ArrayList<>();
    private int redScore;
    private int blueScore;

    public Match(ArrayList<Team> redTeams, ArrayList<Team> blueTeams, int redScore, int blueScore){
        this.redTeams = redTeams;
        this.blueTeams = blueTeams;
        this.redScore = redScore;
        this.blueScore = blueScore;
    }

    public ArrayList<Team> getRedTeams() {
        return redTeams;
    }

    public ArrayList<Team> getBlueTeams() {
        return blueTeams;
    }

    public int getRedScore() {
        return redScore;
    }

    public int getBlueScore() {
        return blueScore;
    }

    public WinnerType getWinner(){
        if (blueScore > redScore){
            return WinnerType.BLUE;
        } else if (redScore > blueScore) {
            return WinnerType.RED;
        } else {
            return WinnerType.TIE;
        }
    }
}
