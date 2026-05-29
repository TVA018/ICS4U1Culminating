import java.io.IOException;
import tba.APIFetcher;

public class Main {
    public static void main(String[] args) throws IOException {
        var event = APIFetcher.getEvent("2026oncmp1");

        System.out.printf("MAD Rankings for %s:\n", event.getName());
        System.out.println("Rank: Team Number [MAD]");

        int rank = 1;
        for(var ranking : event.getMADRankings(false)) {
            System.out.printf("%s: %s [%s]\n", rank, ranking.team.getTeamNum(), ranking.points);
            rank++;
        }
    }
}