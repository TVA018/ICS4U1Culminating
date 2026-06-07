package core.data.robot.enums;

/** The drivetrain type of a robot */
public enum DriveTrain {
    SWERVE("Swerve"),
    TANK("Tank"),
    MECANUM("Mecanum");

    private final String name;

    private DriveTrain(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
