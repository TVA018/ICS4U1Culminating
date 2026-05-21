package data;

import data.robot.Robot;
import ranking.*;

public class Team {

    private String teamName;
    private int teamNum;
    private Robot robot;
    private Event[] events;

    public Team(String teamname, int teamNum, Robot robot, Event[] events, Match[] matches){
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

    public void displayEvents(){
        for (Event e : events) {
            
            
            
            
            /*System.out.println();
            for (Match m : e.matches){
                if(m.redTeams.contains(this)){
                    System.out.println();
                }
                if(m.blueTeams.contains(this)){
                    System.out.println();
                }
            }*/
        }
    }


    
}
