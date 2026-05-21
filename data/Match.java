package data;

import data.enums.WinnerType;

public class Match {
    private Team[] redTeams;
    private Team[] blueTeams;
    private int redScore;
    private int blueScore;

    public Match(){

    }

    public Team[] getRedTeams() {
        return redTeams;
    }

    public Team[] getBlueTeams() {
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
