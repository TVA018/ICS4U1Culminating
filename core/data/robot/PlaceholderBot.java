package core.data.robot;

import core.data.robot.enums.DriveTrain;
import util.TerminalTextFormatter;
import util.TerminalTextFormatter.ANSIFlag;

public class PlaceholderBot extends Robot {
    /**
     * Constructs a new robot that:
     * - Uses Swerve drive
     * - Holds 12 fuel at most in its hopper
     * - Does not have an extendo-hopper
     * - Can go under the trench
     * - Can go over the bump
     */
    public PlaceholderBot() {
        super(DriveTrain.SWERVE, 12, false, true, true);
    }

    @Override
    public String toString() {
        return super.toString() + TerminalTextFormatter.applyFlags("\nWARNING: This robot has insufficient data", ANSIFlag.YELLOW_TEXT);
    }
}
