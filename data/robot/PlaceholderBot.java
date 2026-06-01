package data.robot;

import data.robot.enums.DriveTrain;

public class PlaceholderBot extends Robot {
    public PlaceholderBot() {
        super(DriveTrain.SWERVE, 12, false, true, true);
    }
}
