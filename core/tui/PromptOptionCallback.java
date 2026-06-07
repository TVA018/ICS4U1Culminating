package core.tui;

public interface PromptOptionCallback {
    /** 
     * The function to call when this option is selected
     * @return 0 if this menu should be exited, non-zero values for if this menu should be re-prompted (specific values can be tied to specific options)
     */
    public abstract int exec();
}
