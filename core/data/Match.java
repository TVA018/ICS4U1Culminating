package core.data;

import java.util.ArrayList;

import core.data.enums.WinningAlliance;
import util.CSVParser;

public class Match {
    private final int matchNumber;
    private final ArrayList<Team> redTeams = new ArrayList<>();
    private final ArrayList<Team> blueTeams = new ArrayList<>();
    private final int redScore;
    private final int blueScore;
    private final boolean qualifier;
    private final WinningAlliance winner;

    /**
     * Constructs a new match
     * @param matchNumber The match number
     * @param isQualifier Whether this match is a qualifier match
     * @param redTeams The teams on the red alliance represented by their team number
     * @param blueTeams The teams on the blue alliance represented by their team number
     * @param redScore How much red alliance scored
     * @param blueScore How much blue alliance scored
     * @param winner The winning alliance
     */
    public Match(int matchNumber, boolean isQualifier, ArrayList<Integer> redTeams, ArrayList<Integer> blueTeams, int redScore, int blueScore, WinningAlliance winner){
        this.matchNumber = matchNumber;
        this.redScore = redScore;
        this.blueScore = blueScore;
        this.winner = winner;
        this.qualifier = isQualifier;

        // Convert the team numbers to actual team objects
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

    /** @return the match number */
    public int getMatchNumber() {
        return matchNumber;
    }

    /** @return The teams on the red alliance */
    public ArrayList<Team> getRedTeams() {
        return redTeams;
    }

    /** @return The teams on the blue alliance */
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

    /** @return whether this is a qualifier match */
    public boolean isQualifier() {
        return qualifier;
    }

    /** @return the winning alliance */
    public WinningAlliance getWinner(){
        return winner;
    }
}
