package util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public final class TBA {
    private TBA() {}

    private static final String TBA_ROOT = "https://www.thebluealliance.com/api/v3";
    private static final String EVENT_BASE_URL = TBA_ROOT + "/event";

    private static final String API_KEY = ENV.get("TBA_API_KEY");

    public static void getMatches(String eventKey) {
        try {
            String urlStr = EVENT_BASE_URL + "/" + eventKey + "/matches";
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
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
