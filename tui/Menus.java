package tui;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.Constants;
import data.Ranking;
import data.Team;
import ranking.Event;
import tba.APIFetcher;
import util.Algorithms;
import util.CSVParser;
import util.ComparatorFactory;
import util.OutputFormatter;
import util.TerminalTextFormatter;
import util.TerminalTextFormatter.ANSIFlag;
import util.ValidatedScanner.StringInputParser;
import util.ValidatedScanner;

public final class Menus {
    private Menus() {}

    private static final StringInputParser<Integer> BASIC_INT_PARSER = stringInput -> Integer.parseInt(stringInput);
    private static final StringInputParser<Boolean> BASIC_BOOL_PARSER = stringInput -> {
        if(stringInput.equalsIgnoreCase("t")) return true;
        if(stringInput.equalsIgnoreCase("f")) return false;

        throw new RuntimeException("Input must be 't' or 'f'");
    };
    public static final ValidatedScanner SCANNER = new ValidatedScanner();

    private static PromptOptionCallback createMainMenuOption(Prompt submenu) {
        return () -> {
            int lastCode = 1;

            while (lastCode > 0) {
                lastCode = submenu.exec();
            }

            return 1;
        };
    }

    private static Optional<Event> getEvent() {
        int searchType = SCANNER.readInput("Search type (0: By index, 1: By name)\n> ", stringInput -> {
            int choice = Integer.parseInt(stringInput);

            if(choice != 0 && choice != 1) throw new RuntimeException("The input must be a 0 or 1");

            return choice;
        });

        List<Event> events = APIFetcher.ONT_DISTRICT.getEvents();

        if(searchType == 0) { // Index search
            for(int i = 0; i < events.size(); i++) {
                System.out.println(String.valueOf(i + 1) + ". " + events.get(i).getName());
            }

            int eventIndex = SCANNER.readInput("> ", stringInput -> {
                int choice = Integer.parseInt(stringInput);

                if(choice < 1) throw new RuntimeException("Minimum value: 1");
                if(choice > events.size()) throw new RuntimeException("Maximum value: " + String.valueOf(events.size()));

                return choice - 1;
            });

            return Optional.of(events.get(eventIndex));
        } else { // Fuzzy name search
            String eventPartialName = SCANNER.readLine("Enter the name of the event (partial names accepted)\n> ");
            Pattern pattern = Pattern.compile(eventPartialName, Pattern.CASE_INSENSITIVE);

            return Algorithms.linearSearch(events, event -> pattern.matcher(event.getName()).find());
        }
    }

    private static int printRankings(List<Ranking> rankings, String pointsName) {
        int currentRank = 1;
        String[][] table = new String[rankings.size() + 1][4];

        table[0][0] = "Rank";
        table[0][1] = "Team";
        table[0][2] = "Archetype";
        table[0][3] = pointsName;

        for(Ranking ranking : rankings) {
            String[] row = table[currentRank];
            Team team = ranking.getTeam();

            row[0] = String.valueOf(currentRank);
            row[1] = team.asNameLabel();
            row[2] = team.getRobot().getClass().getSimpleName();
            row[3] = String.format("%.2f", ranking.getPoints());

            currentRank++;
        }

        System.out.println(OutputFormatter.generateTable(table));

        return 1; // Used for menu
    }

    private static int eventMenu() {
        // Get event
        Optional<Event> eventOpt = getEvent();

        if(eventOpt.isEmpty()) {
            TerminalTextFormatter.println("Event could not be found", ANSIFlag.RED_TEXT);
            return 1;
        }

        Event event = eventOpt.get();

        // Re-prompt menu until exited
        Prompt prompt = new Prompt(
            "EVENT CHOSEN: " + event.getName(), 
            new PromptOption("Teams List", () -> {
                System.out.println("\n" + event.getName());
                for(Team team : event.getTeams()) {
                    System.out.println(String.valueOf(team.getTeamNum()) + " - " + team.getName());
                }

                return 1;
            }),
            new PromptOption("Event Rankings", () -> printRankings(event.getRankings(), "RANKING SCORE")),
            new PromptOption("MAD Rankings", () -> {
                boolean onlyIncludeShooters = SCANNER.readInput("Only include shooter robots?\n> ", BASIC_BOOL_PARSER);
                return printRankings(event.getMADRankings(Constants.MAD_FACTOR, onlyIncludeShooters), "MAD");
            }),
            new PromptOption("Return", () -> 0)
        );

        return createMainMenuOption(prompt).exec();
    }

    private static final Prompt DISTRICT_PROMPT = new Prompt(
        "ONTARIO DISTRICT",
        new PromptOption("View Teams", () -> {
            System.out.println();
            for(Team team : APIFetcher.ONT_DISTRICT.getTeams()) {
                System.out.println(String.valueOf(team.getTeamNum()) + " - " + team.getName());
            }
            
            return 1;
        }),
        new PromptOption("District Rankings", () -> printRankings(APIFetcher.ONT_DISTRICT.getRankings(), "DISTRICT POINTS")),
        new PromptOption("MAD Rankings", () -> {
                boolean onlyIncludeShooters = SCANNER.readInput("Only include shooter robots?\n> ", BASIC_BOOL_PARSER);
                return printRankings(APIFetcher.ONT_DISTRICT.getMADRankings(Constants.MAD_FACTOR, onlyIncludeShooters), "MAD");
        }),
        new PromptOption("Return", () -> 0)
    );

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
        new PromptOption("District", createMainMenuOption(DISTRICT_PROMPT)),
        new PromptOption("Event", Menus::eventMenu),
        new PromptOption("Team", createMainMenuOption(TEAM_PROMPT)),
        new PromptOption("Exit", () -> 0)
    );
}
