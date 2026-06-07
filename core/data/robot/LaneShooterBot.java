package core.data.robot;

import core.data.robot.enums.DriveTrain;
import core.data.robot.enums.Indexer;

public class LaneShooterBot extends ShooterBot{
    protected int numLanes;

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

    // Accessors

    /** 
     * @return an int of the number of shooting lanes
     */
    public int getNumLanes() {
        return numLanes;
    }

    @Override
    public String toString() {
        return super.toString() + "\n > Number of lanes: " + numLanes + "\n";
    }
}
