package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import data.Team;
import data.robot.ShooterBot;
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
                    new ShooterBot(DriveTrain.SWERVE, 20, false, true, true, 2, false, false, Indexer.BELT_FLOOR, false, false)
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
