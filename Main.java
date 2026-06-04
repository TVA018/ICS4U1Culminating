import java.io.IOException;

import data.Team;
import ranking.District;
import ranking.Event;
import tba.APIFetcher;
import util.Algorithms;
import util.CSVParser;
import util.ComparatorFactory;

public class Main {
    public static void main(String[] args) throws IOException {
        Event event = APIFetcher.getEvent("2026oncmp1");

        for(Team team : event.getTeams()) team.addEvent(event);

        for(var ranking : event.getRankings()) {
            System.out.printf("%s: %s\n", ranking.getTeam().getTeamNum(), ranking.getPoints());
        }

        // var match1 = event.getMatches().get(0);

        // System.out.println("RED");

        // for(Team team : match1.getRedTeams()) {
        //     System.out.printf("%s [%s]\n", team.getTeamNum(), team.calculateMAD(Constants.MAD_FACTOR));
        // }
        
        // System.out.println("BLUE");

        // for(Team team : match1.getBlueTeams()) {
        //     System.out.printf("%s [%s]\n", team.getTeamNum(), team.calculateMAD(Constants.MAD_FACTOR));
        // }
    }
}