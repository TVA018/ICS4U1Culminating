package core.data;

import java.util.ArrayList;
import java.util.List;

import core.data.enums.TeamAlliance;
import core.data.robot.Robot;
import core.ranking.Event;
import util.Algorithms;
import util.TerminalTextFormatter;
import util.TerminalTextFormatter.ANSIFlag;

public class Team {
    private final String teamName;
    private final int teamNum;
    private final Robot robot;
    private final ArrayList<Event> events = new ArrayList<>();
    private final ArrayList<Match> matches = new ArrayList<>();

    /**
     * Constructs a new team
     * @param teamName The name of the team
     * @param teamNum The team number
     * @param robot The robot associated with the team
     */
    public Team(String teamName, int teamNum, Robot robot){
        this.teamName = teamName;
        this.teamNum = teamNum;
        this.robot = robot;
    }

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
    public TeamAlliance teamColour(Match match){
        if(match.getRedTeams().contains(this)){
            return TeamAlliance.RED;
        } else if (match.getBlueTeams().contains(this)){
            return TeamAlliance.BLUE;
        } else {
            return TeamAlliance.NONE;
        }
    }

    /**
     * Adds an event to this team
     * @param event The event to add
     */
    public void addEvent(Event event){
        this.events.add(event);

        for (Match eventMatch : event.getMatches()){
            if(!(this.teamColour(eventMatch)==TeamAlliance.NONE)){
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

        if(validMatches.isEmpty()) return 0.0;

        double mad;
        if (this.teamColour(validMatches.get(0))==TeamAlliance.BLUE){
            mad = validMatches.get(0).getMeanBlueScore();
        } else {
            mad = validMatches.get(0).getMeanRedScore();
        }
        
        for (Match teamMatch : validMatches){
            mad *= factor;
            if (this.teamColour(teamMatch)==TeamAlliance.BLUE){
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

        if(validMatches.isEmpty()) return 0.0;

        int totalPoints = 0;

        for(Match match : validMatches) {
            totalPoints += this.teamColour(match) == TeamAlliance.BLUE ? match.getBlueScore() : match.getRedScore();
        }

        return ((double) totalPoints) / validMatches.size();
    }

    /** @return A label for the team in the format "teamNumber: teamName" */
    public String asNameLabel() {
        return String.valueOf(teamNum) + ": " + teamName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(
            TerminalTextFormatter.applyFlags("Team " + String.valueOf(teamNum) + ": " + teamName, ANSIFlag.BOLD) +
            "\nAverage Match Points: " + String.format("%.2f", calculateAverageMatchPoints()) +
            "\nMAD: " + String.format("%.2f", calculateMAD(Constants.MAD_FACTOR)) +
            "\n\nEvents:\n"
        );

        if(events.size() > 0) {
            for (Event event : events) {
                builder.append("- " + event.getName() + "\n");
            }
        } else {
            builder.append("- None\n");
        }

        builder.append("\n" + robot.toString());

        return builder.toString();
    }
}
