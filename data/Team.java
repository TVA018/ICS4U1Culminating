package data;

import java.util.ArrayList;
import java.util.List;

import data.enums.TeamType;
import data.robot.Robot;
import ranking.Event;
import util.Algorithms;

public class Team {
    private final String teamName;
    private final int teamNum;
    private final Robot robot;
    private final ArrayList<Event> events = new ArrayList<>();
    private final ArrayList<Match> matches = new ArrayList<>();

    public Team(String teamName, int teamNum, Robot robot){
        this.teamName = teamName;
        this.teamNum = teamNum;
        this.robot = robot;
    }

    // Accessors

    /** 
     * @return the team's name
     */
    public String getName(){
        return teamName;
    }

    /** 
     * @return the team's official number
     */
    public int getTeamNum() {
        return teamNum;
    }

    /** 
     * @return the robot associated with the team
     */
    public Robot getRobot() {
        return robot;
    }

    /** 
     * @param match the Match object to check a team's alliance
     * @return an enum TeamType of the team's colour
     */
    public TeamType teamColour(Match match){
        if(match.getRedTeams().contains(this)){
            return TeamType.RED;
        } else if (match.getBlueTeams().contains(this)){
            return TeamType.BLUE;
        } else {
            return TeamType.NONE;
        }
    }

    /**
     * Adds an event to this team
     * @param event The event to add
     */
    public void addEvent(Event event){
        this.events.add(event);

        for (Match eventMatch : event.getMatches()){
            if(!(this.teamColour(eventMatch)==TeamType.NONE)){
                matches.add(eventMatch);
            }
        }
    }

    /** 
     * @param factor Bias toward a team's previous events from 0-1
     * @return a double of the team's MAD
     */
    public double calculateMAD(double factor){
        List<Match> validMatches = Algorithms.filter(matches, Match::isQualifier);

        double mad;
        if (this.teamColour(validMatches.get(0))==TeamType.BLUE){
            mad = validMatches.get(0).getMeanBlueScore();
        } else {
            mad = validMatches.get(0).getMeanRedScore();
        }
        
        for (Match teamMatch : validMatches){
            mad *= factor;
            if (this.teamColour(teamMatch)==TeamType.BLUE){
                mad += teamMatch.getMeanBlueScore()*(1-factor);
            } else {
                mad += teamMatch.getMeanRedScore()*(1-factor);
            }
        }

        return mad * 2; // Multiply by 2 because it seems to give a more accurate value for expected score
    }    

    /**
     * Calculates the average (mean) points an alliance this team is on will score
     * @return The mean points
     */
    public double calculateAverageMatchPoints() {
        List<Match> validMatches = Algorithms.filter(matches, Match::isQualifier);

        int totalPoints = 0;

        for(Match match : validMatches) {
            totalPoints += this.teamColour(match) == TeamType.BLUE ? match.getBlueScore() : match.getRedScore();
        }

        return ((double) totalPoints) / validMatches.size();
    }
}
