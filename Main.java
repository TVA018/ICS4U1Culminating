import java.io.IOException;

import data.Team;
import ranking.District;
import ranking.Event;
import tba.APIFetcher;

public class Main {
    public static void main(String[] args) throws IOException {
        // District district = APIFetcher.getDistrict("2026ont");

        Event event = APIFetcher.getEvent("2026onwin");

        for(Team team : event.getTeams()) {
            team.addEvent(event);
        }

        while (tui.Constants.MAIN_MENU.exec() > 0) {}
    }
}