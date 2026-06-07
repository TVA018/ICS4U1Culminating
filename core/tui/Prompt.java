package core.tui;

import util.TerminalTextFormatter;
import util.TerminalTextFormatter.ANSIFlag;
import util.ValidatedScanner.StringInputParser;

public final class Prompt {
    private final String header;
    private final PromptOption[] options;
    
    /**
     * Constructs a new prompt
     * @param header The header of the prompt
     * @param options The options of this prompt
     */
    public Prompt(String header, PromptOption ...options) {
        this.header = TerminalTextFormatter.applyFlags(header, ANSIFlag.BOLD);
        this.options = options;
    }

    /**
     * Opens this prompt
     * @return Whether this prompt should be reopened
     */
    public int exec() {
        // Limit the options
        StringInputParser<Integer> optionParser = (stringInput) -> {
            int option = Integer.parseInt(stringInput);

            if(option < 1) throw new RuntimeException("Minimum value must be 1");
            if(option > options.length) throw new RuntimeException("Maximum value is " + String.valueOf(options.length));

            return option;
        };

        // Print options
        System.out.println("\n" + header);

        for(int optionIndex = 0; optionIndex < options.length; optionIndex++) {
            PromptOption option = options[optionIndex];
            System.out.print(String.valueOf(optionIndex + 1) + ". ");
            System.out.println(option.label());
        }

        // Read user input
        int optionIndex = TUICore.SCANNER.readInput("> ", optionParser) - 1;
        PromptOption optionChosen = options[optionIndex];

        return optionChosen.callback().exec(); // Run the corresponding callback
    }
}
