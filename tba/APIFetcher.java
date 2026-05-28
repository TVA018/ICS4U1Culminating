package tba;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import data.Match;
import data.enums.WinningAlliance;
import util.ENV;
import util.SimpleJSon;

public final class APIFetcher {
    private APIFetcher() {}

    private static final String TBA_ROOT = "https://www.thebluealliance.com/api/v3";

    private static final String API_KEY = ENV.get("TBA_API_KEY");

    @SuppressWarnings("unchecked")
    public static void generateTeamsList(String filePath) {
        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(new File(filePath)))) {
            for(int pageNumber = 0; pageNumber < 100; pageNumber++) {
                String jsonString = fetch("/teams/2026/" + String.valueOf(pageNumber) + "/keys");
                // System.out.println(jsonString);
                // System.out.println(jsonString);

                var json = (List<String>) SimpleJSon.parse(jsonString);

                if(json.isEmpty()) break;

                for(var teamKey : json) {
                    fileWriter.write(teamKey.substring(3) + "\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Match> getMatches(String eventKey) {
        String jsonString = fetch("/event/" + eventKey + "/matches/simple");

        ArrayList<Match> matches = new ArrayList<>();

        var json = (List<HashMap<String, Object>>) SimpleJSon.parse(jsonString);

        for(var matchData : json) {
            ArrayList<Integer> redTeams = new ArrayList<>(3);
            ArrayList<Integer> blueTeams = new ArrayList<>(3);
            String tbaWinnerStr = (String) matchData.get("winning_alliance");

            var alliances = (HashMap<String, HashMap<String, Object>>) matchData.get("alliances");

            var redAlliance = alliances.get("red");
            var blueAlliance = alliances.get("blue");

            var redTeamKeys = (List<String>) redAlliance.get("team_keys");
            var blueTeamKeys = (List<String>) redAlliance.get("team_keys");

            for(String teamKey : redTeamKeys) {
                redTeams.add(Conversions.teamNumberFromKey(teamKey));
            }
            
            for(String teamKey : blueTeamKeys) {
                blueTeams.add(Conversions.teamNumberFromKey(teamKey));
            }

            Match match = new Match(
                (int) matchData.get("match_number"),
                matchData.get("comp_level").equals("qm"),
                redTeams,
                blueTeams,
                (int) redAlliance.get("score"),
                (int) blueAlliance.get("score"),
                tbaWinnerStr.equals("red") ? WinningAlliance.RED : 
                (tbaWinnerStr.equals("blue") ? WinningAlliance.BLUE : 
                WinningAlliance.TIE)
            );

            matches.add(match);
        }

        return matches;
    }

    private static String fetch(String endpoint) {
        String urlStr = TBA_ROOT + endpoint;

        try {
            URL url = new URI(urlStr).toURL();

            System.out.println(urlStr);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-TBA-Auth-Key", API_KEY);
            connection.connect();

            int responseCode = connection.getResponseCode();
            boolean isError = responseCode >= HttpURLConnection.HTTP_BAD_REQUEST;
            InputStream inputStream;

            if(isError) {
                inputStream = connection.getErrorStream();
            } else {
                inputStream = connection.getInputStream();
            }

            StringBuilder contentBuilder = new StringBuilder();
            
            try (Scanner scanner = new Scanner(inputStream)) {
                while (scanner.hasNext()) {
                    contentBuilder.append(scanner.nextLine() + "\n");
                }
            }

            if(isError)
                throw new RuntimeException("Failed to fetch '"+ urlStr + "': " + contentBuilder.toString().stripTrailing());

            return contentBuilder.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
