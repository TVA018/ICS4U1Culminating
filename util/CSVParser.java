package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import core.data.Team;
import core.data.robot.DefenceBot;
import core.data.robot.DrumBot;
import core.data.robot.LaneShooterBot;
import core.data.robot.PlaceholderBot;
import core.data.robot.Robot;
import core.data.robot.TurretBot;
import core.data.robot.enums.DriveTrain;
import core.data.robot.enums.Indexer;
import core.tba.Conversions;

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

    private static DriveTrain getDrivetrainFromCell(String csvCell) {
        return switch (csvCell) {
            case "swerve" -> DriveTrain.SWERVE;
            case "tank" -> DriveTrain.TANK;
            case "mecanum" -> DriveTrain.MECANUM;
            default -> throw new RuntimeException(csvCell + " is not a valid Drivetrain String");
        };
    }

    private static Indexer getIndexerFromCell(String csvCell) {
        return switch (csvCell) {
            case "dye rotor" -> Indexer.DYE_ROTOR;
            case "double dye rotor" -> Indexer.DOUBLE_DYE_ROTOR;
            case "roller floor" -> Indexer.ROLLER_FLOOR;
            case "belt floor" -> Indexer.BELT_FLOOR;
            case "spindexer" -> Indexer.SPINDEXER;
            case "double spindexer" -> Indexer.DOUBLE_SPINDEXER;
            case "none" -> Indexer.NONE;
            default -> throw new RuntimeException(csvCell + " is not a valid Indexer String");
        };
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
        DriveTrain driveTrain = getDrivetrainFromCell(csvRow[3]);
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
        Indexer indexer = getIndexerFromCell(csvRow[11]); 
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
