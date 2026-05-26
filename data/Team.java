package data;

import java.util.ArrayList;

import data.enums.TeamType;
import data.robot.Robot;
import ranking.Event;

public class Team {
    private String teamName;
    private int teamNum;
    private Robot robot;
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Match> matches = new ArrayList<>();

    public Team(String teamname, int teamNum, Robot robot, ArrayList<Event> events){
        this.teamName = teamName;
        this.teamNum = teamNum;
        this.robot = robot;
        this.events = events;
    }

    public String getName(){
        return teamName;
    }

    public int getTeamNum() {
        return teamNum;
    }

    public Robot getRobot() {
        return robot;
    }

    public TeamType teamColour(Match match){
        if(match.getRedTeams().contains(this)){
            return TeamType.RED;
        } else if (match.getBlueTeams().contains(this)){
            return TeamType.BLUE;
        } else {
            return TeamType.NONE;
        }
    }

    public void getEvents(){
        for (Event teamEvent : events) {
            for (Match eventMatch : teamEvent.getMatches()){
                if(!(this.teamColour(eventMatch)==TeamType.NONE)){
                    matches.add(eventMatch);
                }
            }
        }
    }

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
