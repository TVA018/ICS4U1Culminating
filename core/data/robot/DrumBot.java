package core.data.robot;

import core.data.robot.enums.DriveTrain;
import core.data.robot.enums.Indexer;

public class DrumBot extends ShooterBot{
    private double drumBallWidth;

    /**
     * Constructs a new drum shooter robot
     * @param driveTrain The drivetrain type
     * @param fuelCapacity How much fuel can the robot hold within itself
     * @param extendoHopper Does it have an extendo-hopper?
     * @param trench Can the robot go under the trench?
     * @param bump Can the robot go over the bump?
     * @param bps How much fuel can the robot shoot per second
     * @param adjustableHood Does the robot have an adjustable hood?
     * @param flywheel Does the robot have a flywheel?
     * @param indexer The type of indexer the robot uses
     * @param passing Can the robot pass?
     * @param autoAim Can the robot automatically aim?
     * @param drumBallWidth How much fuel can the robot fit in its drum shooter's width
     */
    public DrumBot(
        DriveTrain driveTrain,
        int fuelCapacity,
        boolean extendoHopper,
        boolean trench,
        boolean bump,
        double bps,
        boolean adjustableHood,
        boolean flywheel,
        Indexer indexer,
        boolean passing,
        boolean autoAim,
        
        double drumBallWidth
    ) {
        super(driveTrain, fuelCapacity, extendoHopper, trench, bump, bps, adjustableHood, flywheel, indexer, passing, autoAim);
        this.drumBallWidth = drumBallWidth;
    }

    /** 
     * @return how many balls can fit through a team's shooter at once along the width
     */
    public double getDrumBallWidth() {
        return drumBallWidth;
    }

    @Override
    public String toString() {
        return super.toString() + "\n > Drum Ball Width: " + String.format("%.2f", drumBallWidth);
    }
}
