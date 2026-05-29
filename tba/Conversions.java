package tba;

public class Conversions {
    private Conversions() {}

    /**
     * Converts a TBA team key (i.e. frc865) into just a number
     * @param teamKey The team key
     * @return The team number
     */
    public static int teamNumberFromKey(String teamKey) {
        return Integer.parseInt(teamKey.substring(3));
    }
}
