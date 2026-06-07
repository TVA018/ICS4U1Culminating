package tui;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.Team;
import util.Algorithms;
import util.CSVParser;
import util.ComparatorFactory;
import util.TerminalTextFormatter;
import util.TerminalTextFormatter.ANSIFlag;
import util.ValidatedScanner.StringInputParser;
import util.ValidatedScanner;

public final class Constants {
    private Constants() {}

    private static final StringInputParser<Integer> BASIC_INT_PARSER = stringInput -> Integer.parseInt(stringInput);
    public static final ValidatedScanner SCANNER = new ValidatedScanner();

    private static final Prompt TEAM_PROMPT = new Prompt(
        "TEAM",
        new PromptOption("Search by Team Number", () -> {
            int teamNumber = SCANNER.readInput("Enter the team number to search for\n> ", BASIC_INT_PARSER);

            Optional<Team> teamOpt = Algorithms.binarySearch(CSVParser.getTeams(), ComparatorFactory.ascendingSearchComparator(teamNumber, Team::getTeamNum));

            if(teamOpt.isEmpty()) {
                TerminalTextFormatter.println("Could not find team " + String.valueOf(teamNumber), ANSIFlag.RED_TEXT);
            } else {
                System.out.println();
                System.out.println(teamOpt.get());
            }

            return 1;
        }),
        new PromptOption("Search by Team Name", () -> {
            String partialTeamName = SCANNER.readLine("Enter the team name to search for (partial names are allowed, case-insensitive)\n> ");

            Pattern pattern = Pattern.compile(partialTeamName, Pattern.CASE_INSENSITIVE);

            Optional<Team> teamOpt = Algorithms.linearSearch(CSVParser.getTeams(), team -> {
                Matcher matcher = pattern.matcher(team.getName());

                return matcher.find();
            });

            if(teamOpt.isEmpty()) {
                TerminalTextFormatter.println("Could not find a team that contains " + partialTeamName + " in their name", ANSIFlag.RED_TEXT);
            } else {
                System.out.println();
                System.out.println(teamOpt.get());
            }
            
            return 1;
        }),
        new PromptOption("Return", () -> 0)
    );

    public static final Prompt MAIN_MENU = new Prompt(
        "--[MADStrat]--",
        new PromptOption("District", () -> 1),
        new PromptOption("Event", () -> 1),
        new PromptOption("Team", () -> {
            int lastCode = 1;

            while (lastCode > 0) {
                lastCode = TEAM_PROMPT.exec();
            }

            return 1;
        }),
        new PromptOption("Exit", () -> 0)
    );
}
