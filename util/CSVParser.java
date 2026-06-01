package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import data.Team;
import data.robot.DefenceBot;
import data.robot.DrumBot;
import data.robot.LaneShooterBot;
import data.robot.PlaceholderBot;
import data.robot.Robot;
import data.robot.ShooterBot;
import data.robot.TurretBot;
import data.robot.enums.DriveTrain;
import data.robot.enums.Indexer;
import tba.Conversions;

/** A helper class to parse teams.csv */
public class CSVParser {
    private static final List<Team> teams;
    public static final String SEPARATOR = ";";

    static {
        ArrayList<Team> modifiableList = new ArrayList<>();

        // Loop and parse
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("res/teams.csv")))) {
            String currentLine = reader.readLine();

            while (currentLine != null) {
                String[] cells = currentLine.split(SEPARATOR);

                Team team = new Team(
                    cells[1], 
                    Integer.parseInt(cells[0]), 
                    getRobot(cells)
                );

                modifiableList.add(team);

                currentLine = reader.readLine();
            }
        } catch (Exception e) { // Crash the program on purpose, the program can't run properly if this fails
            throw new RuntimeException(e);
        }

        teams = Collections.unmodifiableList(modifiableList);
    }

    /**
     * Parses a CSV row from teams.csv, and returns the corresponding Robot object
     * 
     * @param csvRow an array of Strings, where each String is a cell
     * @return The Robot object
     */
    private static Robot getRobot(String[] csvRow) {
        if(csvRow.length < 3) return new PlaceholderBot();

        String robotTypeString = csvRow[2];
        DriveTrain driveTrain = DriveTrain.fromString(csvRow[3]);
        int fuelCapacity = Integer.parseInt(csvRow[4]);
        boolean extendoHopper = getBoolean(csvRow[5]);
        boolean trench = getBoolean(csvRow[6]);
        boolean bump = getBoolean(csvRow[7]);

        if(robotTypeString.equals("defence")) {
            boolean shotBlocker = getBoolean(csvRow[9]);

            return new DefenceBot(
                driveTrain, 
                fuelCapacity, 
                extendoHopper,
                trench, 
                bump, 
                shotBlocker
            );
        }
        
        double bps = Double.parseDouble(csvRow[8]);
        boolean adjustableHood = getBoolean(csvRow[9]);
        boolean hasFlywheel = getBoolean(csvRow[10]); 
        Indexer indexer = Indexer.fromString(csvRow[11]); 
        boolean canPass = getBoolean(csvRow[12]); 
        boolean canAutoAim = getBoolean(csvRow[13]); 

        return switch (robotTypeString) {
            case "drum" -> new DrumBot(
                driveTrain, 
                fuelCapacity, 
                extendoHopper, 
                trench, 
                bump, 
                bps,
                adjustableHood,
                hasFlywheel,
                indexer,
                canPass,
                canAutoAim,
                Double.parseDouble(csvRow[14]) // Drum Ball Width
            );
            case "lane fixed" -> new LaneShooterBot(
                driveTrain,
                fuelCapacity,
                extendoHopper,
                trench,
                bump,
                bps,
                adjustableHood,
                hasFlywheel,
                indexer,
                canPass,
                canAutoAim,
                Integer.parseInt(csvRow[14]) // Number of Lanes
            );
            case "turret" -> new TurretBot(
                driveTrain,
                fuelCapacity,
                extendoHopper,
                trench,
                bump,
                bps,
                adjustableHood,
                hasFlywheel,
                indexer,
                canPass,
                canAutoAim,
                Integer.parseInt(csvRow[14]), // Number of Lanes
                Double.parseDouble(csvRow[15]), // Degrees of Rotation
                getBoolean(csvRow[16]) // Shoot on the move
            );
            default ->
                // Crash the program. This is fine because this will happen right at the start, and
                // the file needs to be parsed properly in order to run the program
                throw new RuntimeException(robotTypeString + " is not a valid robot type: " + Arrays.toString(csvRow));
        };
    }

    private static boolean getBoolean(String booleanStr) {
        String lowerStr = booleanStr.toLowerCase();

        if(lowerStr.startsWith("t")) return true;
        if(lowerStr.startsWith("f")) return false;
        
        throw new RuntimeException(booleanStr + " is not a valid boolean String!");
    }

    /**
     * Gets a team by team number
     * @param teamNumber The team number
     * @return Optional.empty if the team couldn't be found
     */
    public static Optional<Team> getTeam(int teamNumber) {
        return Algorithms.binarySearch(teams, ComparatorFactory.ascendingSearchComparator(teamNumber, Team::getTeamNum));
    }

    /**
     * Gets a team by team key (i.e. frc865)
     * @param teamKey The team key
     * @return Optional.empty if the team couldn't be found
     */
    public static Optional<Team> getTeam(String teamKey) {
        return getTeam(Conversions.teamNumberFromKey(teamKey));
    }

    /** @return The list of teams (unmodifiable List) */
    public static List<Team> getTeams() {
        return teams;
    }
}
