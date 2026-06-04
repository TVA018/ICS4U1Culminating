import java.io.IOException;

import data.Team;
import ranking.District;
import tba.APIFetcher;
import util.Algorithms;
import util.CSVParser;
import util.ComparatorFactory;

public class Main {
    public static void main(String[] args) throws IOException {
        District district = APIFetcher.getDistrict("2026ont");

        for(Team team : district.getTeams()) {
            System.out.println(team.getTeamNum());
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