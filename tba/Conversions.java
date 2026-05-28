package tba;

public class Conversions {
    private Conversions() {}

    public static int teamNumberFromKey(String teamKey) {
        return Integer.parseInt(teamKey.substring(3));
    }
}
