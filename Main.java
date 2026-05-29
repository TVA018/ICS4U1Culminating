import java.io.IOException;
import tba.APIFetcher;

public class Main {
    public static void main(String[] args) throws IOException {
        var events = APIFetcher.getEvents("2026ont");

        for(var event : events) {
            System.out.println(event.getName());

            for(var team : event.getTeams())
                System.out.printf("- Team %s: %s\n", team.getTeamNum(), team.getName());
        }
    }
}