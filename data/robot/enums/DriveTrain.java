package data.robot.enums;

public enum DriveTrain {
    SWERVE,
    TANK,
    MECANUM;

    public static DriveTrain fromString(String string) {
        return switch (string) {
            case "swerve" -> DriveTrain.SWERVE;
            case "tank" -> DriveTrain.TANK;
            case "mecanum" -> DriveTrain.MECANUM;
            default -> throw new RuntimeException(string + " is not a valid Drivetrain String");
        };
    }
}
