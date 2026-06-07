package core.tui;

/** An option of a prompt */
public record PromptOption(String label, PromptOptionCallback callback) {}
