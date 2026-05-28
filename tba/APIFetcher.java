package tba;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import data.Match;
import util.ENV;
import util.PrettyPrint;
import util.SimpleJSon;

public final class APIFetcher {
    private APIFetcher() {}

    private static final String TBA_ROOT = "https://www.thebluealliance.com/api/v3";

    private static final String API_KEY = ENV.get("TBA_API_KEY");

    public static void getMatches(String eventKey) {
        String jsonString = fetch("/event/" + eventKey + "/matches");

        ArrayList<Match> matches = new ArrayList<>();

        Object json = SimpleJSon.parse(jsonString);

        PrettyPrint.pprint(json);
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
