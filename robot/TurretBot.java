package robot;

import robot.enums.DriveTrain;
import robot.enums.Indexer;

public class TurretBot extends LaneShooterBot{
    private double degreesOfRotation;
    private boolean SOTM;

    public TurretBot(DriveTrain driveTrain, int fuelCapacity, boolean extendoHopper, boolean trench, boolean bump,
            double bps, boolean adjustableHood, boolean flywheel, Indexer indexer, boolean passing, boolean autoAim,
            int numLanes, double degreesOfRotation, boolean SOTM) {
        super(driveTrain, fuelCapacity, extendoHopper, trench, bump, bps, adjustableHood, flywheel, indexer, passing, autoAim,
                numLanes);
        this.degreesOfRotation = degreesOfRotation;
        this.SOTM = SOTM;        
    }

    public double getDegreesOfRotation() {
        return this.degreesOfRotation;
    }

    public boolean canSOTM(){
        return this.SOTM;
    }
    
}
