package robot;

import robot.enums.DriveTrain;
import robot.enums.Indexer;

public class LaneShooterBot extends ShooterBot{
    protected int numLanes;

    public LaneShooterBot(DriveTrain driveTrain, int fuelCapacity, boolean extendoHopper, boolean trench, boolean bump,
            double bps, boolean adjustableHood, boolean flywheel, Indexer indexer, boolean passing, boolean autoAim, int numLanes) {
        super(driveTrain, fuelCapacity, extendoHopper, trench, bump, bps, adjustableHood, flywheel, indexer, passing, autoAim);
        this.numLanes = numLanes;
    }

    public int getNumLanes() {
        return this.numLanes;
    }
}
