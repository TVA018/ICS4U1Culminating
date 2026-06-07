package core.data.robot;

import core.data.robot.enums.DriveTrain;
import core.data.robot.enums.Indexer;

public class LaneShooterBot extends ShooterBot{
    protected int numLanes;

    /**
     * Constructs a new lane shooter robot (the fuel moves through lanes)
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
     * @param numLanes How many lanes does the robot have?
     */
    public LaneShooterBot(
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
        
        int numLanes
    ) {
        super(driveTrain, fuelCapacity, extendoHopper, trench, bump, bps, adjustableHood, flywheel, indexer, passing, autoAim);
        this.numLanes = numLanes;
    }

    /** 
     * @return the number of shooting lanes on the robot
     */
    public int getNumLanes() {
        return numLanes;
    }

    @Override
    public String toString() {
        return super.toString() + "\n > Number of lanes: " + numLanes + "\n";
    }
}
