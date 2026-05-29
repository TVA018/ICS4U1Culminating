package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/** A helper class to read environment variables from a file. The file must be named .env */
public final class ENV {
    private ENV() {}

    private static Map<String, String> customEnv = new HashMap<>();

    static {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(new File(".env")))) {
            // Read through each line and add each env var to the customEnv map
            // A variable declaration is in the format of: variableName=variableValue
            String currentLine = fileReader.readLine();

            while (currentLine != null) { // If it's null, then there are no characters left to read from the file
                String[] pair = currentLine.split("="); // Delimit by =
                // Strip the strings to remove any excess whitespaces
                String envName = pair[0].strip();
                String envValue = pair[1].strip();

                customEnv.put(envName, envValue);

                currentLine = fileReader.readLine();
            }
        } catch (Exception e) { // Crash the program if the file fails to be read because the program can't be run without this
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets an environment variable. Prioritizes the custom environment variables defined in .env
     * @param name The name of the variable
     * @return The value of the variable. This is null if the variable doesn't exist
     */
    public static String get(String name) {
        // Try to read it from the custom variables map
        String value = customEnv.get(name);

        if(value == null) {
            // Return it from the actual list of environment variables instead
            return System.getenv(name);
        }

        return value;
    }
}
