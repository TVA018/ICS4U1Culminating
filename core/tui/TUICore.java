package core.tui;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import core.Simulation;
import core.data.Constants;
import core.data.Match;
import core.data.Ranking;
import core.data.Team;
import core.data.enums.WinningAlliance;
import core.ranking.Event;
import core.tba.APIFetcher;
import util.Algorithms;
import util.CSVParser;
import util.ComparatorFactory;
import util.OutputFormatter;
import util.TerminalTextFormatter;
import util.TerminalTextFormatter.ANSIFlag;
import util.ValidatedScanner.StringInputParser;
import util.ValidatedScanner;

/** The core class for the user interface */
public final class TUICore {
    private TUICore() {}

    private static final StringInputParser<Integer> BASIC_INT_PARSER = stringInput -> Integer.parseInt(stringInput);
    private static final StringInputParser<Boolean> BASIC_BOOL_PARSER = stringInput -> {
        if(stringInput.equalsIgnoreCase("y")) return true;
        if(stringInput.equalsIgnoreCase("n")) return false;

        throw new RuntimeException("Input must be 'y' or 'n'");
    };

    public static final ValidatedScanner SCANNER = new ValidatedScanner();

    /**
     * Creates a new callback that will execute the callback of the provided submenu until the submenu's callback return 0
     * @param submenu The submenu to use the callback of
     * @return Always 1
     */
    private static PromptOptionCallback execUntilZero(Prompt submenu) {
        return () -> {
            int lastCode = 1;

            while (lastCode > 0) {
                lastCode = submenu.exec();
            }

            return 1;
        };
    }

    /**
     * Provides a prompt for the user to use to search for a particular event
     * @return Optional.empty if the event could not be found, Optional.of(eventObj) if it was
     */
    private static Optional<Event> getEvent() {
        int searchType = SCANNER.readInput("Search type (0: By index, 1: By name/key)\n> ", stringInput -> {
            int choice = Integer.parseInt(stringInput);

            if(choice != 0 && choice != 1) throw new RuntimeException("The input must be a 0 or 1");

            return choice;
        });

        List<Event> events = APIFetcher.ONT_DISTRICT.getEvents();

        if(searchType == 0) { // Index search
            // List out events
            for(int i = 0; i < events.size(); i++) {
                System.out.println(String.valueOf(i + 1) + ". " + events.get(i).getName());
            }

            // Get the event the user wantgs
            int eventIndex = SCANNER.readInput("> ", stringInput -> {
                int choice = Integer.parseInt(stringInput);

                if(choice < 1) throw new RuntimeException("Minimum value: 1");
                if(choice > events.size()) throw new RuntimeException("Maximum value: " + String.valueOf(events.size()));

                return choice - 1;
            });

            return Optional.of(events.get(eventIndex));
        } else { // Fuzzy name search
            // Use RegEx to match any string that contains the user input (case-insensitive)
            String eventPartialName = SCANNER.readLine("Enter the name or key of the event (partial names accepted)\n> ");
            Pattern pattern = Pattern.compile(eventPartialName, Pattern.CASE_INSENSITIVE);

            // Check the key first, then the event name
            return Algorithms.linearSearch(
                events, 
                event -> pattern.matcher(event.getKey()).find() || pattern.matcher(event.getName()).find()
            );
        }
    }

    /**
     * Prints a list of rankings as a table
     * @param rankings The list of rankings
     * @param pointsName The column label to use for the points
     * @return Always 1
     */
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

    private static final Prompt DISTRICT_PROMPT = new Prompt(
        "ONTARIO DISTRICT",
        new PromptOption("View Teams", () -> {
            // Print out all teams
            System.out.println();
            for(Team team : APIFetcher.ONT_DISTRICT.getTeams()) {
                System.out.println(String.valueOf(team.getTeamNum()) + " - " + team.getName());
            }
            
            return 1;
        }),
        new PromptOption("Events List", () -> {
            for(Event event : APIFetcher.ONT_DISTRICT.getEvents()) {
                System.out.println("- " + event.getName());
            }
            
            return 1;
        }),
        new PromptOption("District Rankings", () -> printRankings(APIFetcher.ONT_DISTRICT.getRankings(), "DISTRICT POINTS")),
        new PromptOption("MAD Rankings", () -> {
            boolean onlyIncludeShooters = SCANNER.readInput("Only include shooter robots? (y/n)\n> ", BASIC_BOOL_PARSER);
            return printRankings(APIFetcher.ONT_DISTRICT.getMADRankings(Constants.MAD_FACTOR, onlyIncludeShooters), "MAD");
        }),
        new PromptOption("Return", () -> 0)
    );

    private static Prompt createEventPrompt(Event event) {
        return new Prompt(
            "EVENT CHOSEN: " + event.getName(), 
            new PromptOption("Event Info", () -> {
                System.out.println(event);
                return 1;
            }),
            new PromptOption("Teams List", () -> {
                // Prints out all teams
                System.out.println("\n" + event.getName());
                for(Team team : event.getTeams()) {
                    System.out.println(String.valueOf(team.getTeamNum()) + " - " + team.getName());
                }

                return 1;
            }),
            new PromptOption("Matches", () -> {
                List<Match> matches = event.getMatches();
                int playoffMatchStartIndex = -1;

                // Loop through all matches
                for(int i = 0; i < matches.size(); i++) {
                    Match match = matches.get(i);
                    WinningAlliance winner = match.getWinner();

                    // Keep track of when playoffs start
                    if(!match.isQualifier() && playoffMatchStartIndex < 0)
                        playoffMatchStartIndex = i;

                    // Use a custom equation to get the playoff match number because the one provided by TBA is not what we are looking for
                    int matchNum = match.isQualifier() ? match.getMatchNumber() : (1 + i - playoffMatchStartIndex);

                    // Match header
                    System.out.println(TerminalTextFormatter.applyFlags(
                        (match.isQualifier() ? "Qual " : "Playoff ") + String.valueOf(matchNum) + ":",
                        ANSIFlag.BOLD
                    ));

                    String redHeader = TerminalTextFormatter.applyFlags(
                        " - Red (" + String.valueOf(match.getRedScore()) + " Points)",
                        ANSIFlag.RED_TEXT
                    );

                    String blueHeader = TerminalTextFormatter.applyFlags(
                        " - Blue (" + String.valueOf(match.getBlueScore()) + " Points)",
                        ANSIFlag.BLUE_TEXT
                    );

                    // Bold the winning alliance
                    if(winner.equals(WinningAlliance.RED)) redHeader = TerminalTextFormatter.applyFlags(redHeader, ANSIFlag.BOLD);
                    if(winner.equals(WinningAlliance.BLUE)) blueHeader = TerminalTextFormatter.applyFlags(blueHeader, ANSIFlag.BOLD);

                    // Print red teams
                    System.out.println(redHeader);

                    for(Team team: match.getRedTeams()) {
                        System.out.println("  - " + team.asNameLabel());
                    }

                    // Print blue teams
                    System.out.println(blueHeader);

                    for(Team team: match.getBlueTeams()) {
                        System.out.println("  - " + team.asNameLabel());
                    }
                }
                
                return 1;
            }),
            new PromptOption("Event Rankings", () -> printRankings(event.getRankings(), "RANKING SCORE")),
            new PromptOption("MAD Rankings", () -> {
                boolean onlyIncludeShooters = SCANNER.readInput("Only include shooter robots? (y/n)\n> ", BASIC_BOOL_PARSER);
                return printRankings(event.getMADRankings(Constants.MAD_FACTOR, onlyIncludeShooters), "MAD");
            }),
            new PromptOption("Return", () -> 0)
        );
    }

    /**
     * Opens the event menu
     * @return Always returns 1
     */
    private static int eventMenu() {
        // Get event
        Optional<Event> eventOpt = getEvent();

        if(eventOpt.isEmpty()) {
            TerminalTextFormatter.println("Event could not be found", ANSIFlag.RED_TEXT);
            return 1;
        }

        Event event = eventOpt.get();

        Prompt prompt = createEventPrompt(event);

        return execUntilZero(prompt).exec();
    }

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

            // Use RegEx to search
            Pattern pattern = Pattern.compile(partialTeamName, Pattern.CASE_INSENSITIVE);

            Optional<Team> teamOpt = Algorithms.linearSearch(CSVParser.getTeams(), team -> {
                Matcher matcher = pattern.matcher(team.getName());

                return matcher.find();
            });

            if(teamOpt.isEmpty()) {
                TerminalTextFormatter.println("Could not find a team that contains " + partialTeamName + " in their name", ANSIFlag.RED_TEXT);
            } else {
                // Prints the team
                System.out.println();
                System.out.println(teamOpt.get());
            }
            
            return 1;
        }),
        new PromptOption("Return", () -> 0)
    );

    /**
     * Opens the simulation menu
     * @return Always 1
     */
    private static int simulationMenu() {
        Simulation simulator;

        try {
            simulator = new Simulation(SCANNER.readLine("File Name: "));
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }

        Event event = simulator.simulate();

        Prompt prompt = createEventPrompt(event);

        return execUntilZero(prompt).exec();
    }

    public static final Prompt MAIN_MENU = new Prompt(
        "--[MADStrat]--",
        new PromptOption("District", execUntilZero(DISTRICT_PROMPT)),
        new PromptOption("Event", TUICore::eventMenu),
        new PromptOption("Team", execUntilZero(TEAM_PROMPT)),
        new PromptOption("Simulate", TUICore::simulationMenu),
        new PromptOption("Exit", () -> 0)
    );
}
