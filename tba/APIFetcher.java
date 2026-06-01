package tba;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import data.Match;
import data.Team;
import data.enums.WinningAlliance;
import ranking.Event;
import util.Algorithms;
import util.CSVParser;
import util.ComparatorFactory;
import util.ENV;
import util.SimpleJSon;

public final class APIFetcher {
    private APIFetcher() {}

    private static final String TBA_ROOT = "https://www.thebluealliance.com/api/v3";

    private static final String API_KEY = ENV.get("TBA_API_KEY");

    /**
     * Dev function. Writes a list of all team numbers in 2026 to a file, delimited by new-lines (\n)
     * @param filePath The file to write to
     */
    @SuppressWarnings("unchecked")
    public static void generateTeamsList(String filePath) {
        System.out.println("Started");

        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(new File(filePath)))) {
            int pageNumber = 0;
            while (true) { // The loop will break itself when needed
                // Grab all the teams in on this page
                var json = (List<HashMap<String, Object>>) fetch("/teams/2026/" + String.valueOf(pageNumber) + "/simple");

                if(json.isEmpty()) break; // No teams on this page, we have reached the end

                for(var teamInfo : json) {
                    fileWriter.write(String.join(
                        CSVParser.SEPARATOR,
                        teamInfo.get("team_number").toString(),
                        teamInfo.get("nickname").toString()
                    ) + "\n");
                }

                System.out.printf("Page %s read\n");
                pageNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns all the matches for a given event
     * @param eventKey The event key
     * @return The matches
     */
    @SuppressWarnings("unchecked")
    public static List<Match> getMatches(String eventKey) {
        var json = (List<HashMap<String, Object>>) fetch("/event/" + eventKey + "/matches/simple");

        ArrayList<Match> matches = new ArrayList<>();

        Algorithms.mergeSort(json, (match1, match2) -> (int) match1.get("actual_time") - (int) match2.get("actual_time"));

        for(var matchData : json) { // Parse through each match
            // List of team numbers
            ArrayList<Integer> redTeams = new ArrayList<>(3);
            ArrayList<Integer> blueTeams = new ArrayList<>(3);
            String tbaWinnerStr = (String) matchData.get("winning_alliance");

            // These are the alliance objects
            var alliances = (HashMap<String, HashMap<String, Object>>) matchData.get("alliances");

            var redAlliance = alliances.get("red");
            var blueAlliance = alliances.get("blue");

            // Grab the team keys and add them to the list of team numbers
            var redTeamKeys = (List<String>) redAlliance.get("team_keys");
            var blueTeamKeys = (List<String>) blueAlliance.get("team_keys");

            for(String teamKey : redTeamKeys) {
                redTeams.add(Conversions.teamNumberFromKey(teamKey));
            }
            
            for(String teamKey : blueTeamKeys) {
                blueTeams.add(Conversions.teamNumberFromKey(teamKey));
            }

            // Create a new match object and append it to the list
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

    @SuppressWarnings("unchecked")
    public static Event getEvent(String eventKey) {
        var matches = getMatches(eventKey);
        var teamNumbers = getEventTeamNumbers(eventKey);

        var eventInfo = (HashMap<String, Object>) fetch("/event/" + eventKey + "/simple");
        var eventName = (String) eventInfo.get("name");
        var startDateStr = (String) eventInfo.get("start_date");

        Date date = Date.valueOf(startDateStr);

        Event event = new Event(eventName, date, matches, teamNumbers);

        for(int teamNumber : teamNumbers) {
            var teamOpt = Algorithms.binarySearch(CSVParser.getTeams(), ComparatorFactory.ascendingSearchComparator(teamNumber, Team::getTeamNum));

            if(teamOpt.isEmpty()) {
                System.err.printf("Could not find team %s\n", teamNumber);
                continue;
            }

            teamOpt.get().addEvent(event);
        }

        return event;
    }

    @SuppressWarnings("unchecked")
    private static List<Integer> getEventTeamNumbers(String eventKey) {
        var json = (List<String>) fetch("/event/" + eventKey + "/teams/keys");

        ArrayList<Integer> teamNumbers = new ArrayList<>();

        for(String teamKey : json) {
            teamNumbers.add(Conversions.teamNumberFromKey(teamKey));
        }

        Algorithms.mergeSort(teamNumbers, (team1, team2) -> team1 - team2);

        return teamNumbers;
    }

    @SuppressWarnings("unchecked")
    public static List<Event> getEvents(String districtKey) {
        var json = (List<String>) fetch("/district/" + districtKey + "/events/keys");

        ArrayList<Event> events = new ArrayList<>();

        for(String eventKey : json) {
            events.add(getEvent(eventKey));
        }

        Algorithms.mergeSort(events, (event1, event2) -> event1.getStartDate().compareTo(event2.getStartDate()));

        return events;
    }

    /**
     * Fetches the JSon from a RESTful TBA API endpoint. See https://www.thebluealliance.com/apidocs/v3 for more details.
     * @param endpoint The endpoint excluding the API root (for example, to fetch the status of TBA, pass in "/status")
     * @return The JSon as an object (either a HashMap or ArrayList) 
     */
    private static Object fetch(String endpoint) {
        String urlStr = TBA_ROOT + endpoint; // Create the full url

        try {
            URL url = new URI(urlStr).toURL();

            // Open the connection to the endpoint
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-TBA-Auth-Key", API_KEY);
            connection.connect();

            int responseCode = connection.getResponseCode();
            boolean isError = responseCode >= HttpURLConnection.HTTP_BAD_REQUEST;
            InputStream inputStream;

            // Read the input stream into a String(Builder)
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

            if(isError) // Throw an error if the endpoint returned an error-code
                throw new RuntimeException(
                    "Error (" + Integer.valueOf(responseCode) + ") '"+ 
                    urlStr + "': " + contentBuilder.toString().stripTrailing()
                );

            // Return the JSon
            return SimpleJSon.parse(contentBuilder.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
