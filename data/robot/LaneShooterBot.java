package data.robot;

import data.robot.enums.DriveTrain;
import data.robot.enums.Indexer;

public class LaneShooterBot extends ShooterBot{
    protected int numLanes;

    public LaneShooterBot(DriveTrain driveTrain, int fuelCapacity, boolean extendoHopper, boolean trench, boolean bump,
            double bps, boolean adjustableHood, boolean flywheel, Indexer indexer, boolean passing, boolean autoAim, int numLanes) {
        super(driveTrain, fuelCapacity, extendoHopper, trench, bump, bps, adjustableHood, flywheel, indexer, passing, autoAim);
        this.numLanes = numLanes;
    }

    public int getNumLanes() {
        return numLanes;
    }
}
