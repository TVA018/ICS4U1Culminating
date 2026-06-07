package data.robot;

import data.robot.enums.DriveTrain;
import util.TerminalTextFormatter;
import util.TerminalTextFormatter.ANSIFlag;

public class PlaceholderBot extends Robot {
    public PlaceholderBot() {
        super(DriveTrain.SWERVE, 12, false, true, true);
    }

    @Override
    public String toString() {
        return super.toString() + TerminalTextFormatter.applyFlags("\nWARNING: This robot has insufficient data", ANSIFlag.YELLOW_TEXT);
    }
}
