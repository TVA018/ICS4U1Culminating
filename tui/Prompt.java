package tui;

import util.TerminalTextFormatter;
import util.TerminalTextFormatter.ANSIFlag;
import util.ValidatedScanner.StringInputParser;

public final class Prompt {
    private final String header;
    private final PromptOption[] options;
    
    public Prompt(String header, PromptOption ...options) {
        this.header = TerminalTextFormatter.applyFlags(header, ANSIFlag.BOLD);
        this.options = options;
    }

    public int exec() {
        StringInputParser<Integer> optionParser = (stringInput) -> {
            int option = Integer.parseInt(stringInput);

            if(option < 1) throw new RuntimeException("Minimum value must be 1");
            if(option > options.length) throw new RuntimeException("Maximum value is " + String.valueOf(options.length));

            return option;
        };

        // Print
        System.out.println("\n" + header);

        for(int optionIndex = 0; optionIndex < options.length; optionIndex++) {
            PromptOption option = options[optionIndex];
            System.out.print(String.valueOf(optionIndex + 1) + ". ");
            System.out.println(option.label());
        }

        // Read user input
        int optionIndex = TUICore.SCANNER.readInput("> ", optionParser) - 1;
        PromptOption optionChosen = options[optionIndex];

        return optionChosen.callback().exec();
    }
}
