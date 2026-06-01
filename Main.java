import java.io.IOException;

import data.Ranking;
import data.Team;
import tba.APIFetcher;
import util.Algorithms;
import util.ComparatorFactory;

public class Main {
    public static void main(String[] args) throws IOException {
        var event = APIFetcher.getEvent("2026oncmp1");

        System.out.printf("MAD Rankings for %s:\n", event.getName());
        System.out.println("Rank: Team Number [MAD]");

        var match1 = event.getMatches().get(0);

        System.out.println("RED");

        for(Team team : match1.getRedTeams()) {
            System.out.printf("%s [%s]\n", team.getTeamNum(), team.calculateMAD(Constants.MAD_FACTOR));
        }
        
        System.out.println("BLUE");

        for(Team team : match1.getBlueTeams()) {
            System.out.printf("%s [%s]\n", team.getTeamNum(), team.calculateMAD(Constants.MAD_FACTOR));
        }
    }
}