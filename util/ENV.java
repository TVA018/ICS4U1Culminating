package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public final class ENV {
    private ENV() {}

    private static Map<String, String> customEnv = new HashMap<>();

    static {
        try {
            // Open file
            BufferedReader fileReader = new BufferedReader(new FileReader(new File(".env")));
            String currentLine = fileReader.readLine();

            while (currentLine != null) {
                String[] pair = currentLine.split("=");
                String envName = pair[0].strip();
                String envValue = pair[1].strip();

                customEnv.put(envName, envValue);

                currentLine = fileReader.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String name) {
        String value = customEnv.get(name);

        if(value == null) {
            return System.getenv(name);
        }

        return value;
    }
}
