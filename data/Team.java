package data;

import java.util.ArrayList;

import data.enums.TeamType;
import data.robot.Robot;
import ranking.Event;

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
        if(match.getRedTeams().contains(this.teamNum)){
            return TeamType.RED;
        } else if (match.getBlueTeams().contains(this.teamNum)){
            return TeamType.BLUE;
        } else {
            return TeamType.NONE;
        }
    }

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
        double mad = 0.0;
        if (this.teamColour(matches.get(0))==TeamType.BLUE){
            mad += matches.get(0).getBlueScore()/3;
        } else {
            mad += matches.get(0).getRedScore()/3;
        }
        for (Match teamMatch : matches){
            mad *= factor;
            if (this.teamColour(teamMatch)==TeamType.BLUE){
                mad += (teamMatch.getBlueScore()/3)*(1-factor);
            } else {
                mad += (teamMatch.getRedScore()/3)*(1-factor);
            }
        }
        return mad;
    }    
}
