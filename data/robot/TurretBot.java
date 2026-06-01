package data.robot;

import data.robot.enums.DriveTrain;
import data.robot.enums.Indexer;

public class TurretBot extends LaneShooterBot{
    private double degreesOfRotation;
    private boolean SOTM;

    public TurretBot(
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
        
        int numLanes,

        double degreesOfRotation,
        boolean SOTM
    ) {
        super(driveTrain, fuelCapacity, extendoHopper, trench, bump, bps, adjustableHood, flywheel, indexer, passing, autoAim,
                numLanes);
        this.degreesOfRotation = degreesOfRotation;
        this.SOTM = SOTM;        
    }

    // Accessors

    /** 
     * @return a double of the maximum amount of rotation a turret can do
     */
    public double getDegreesOfRotation() {
        return degreesOfRotation;
    }

    /** 
     * @return true if a team can shoot on the move
     */
    public boolean canSOTM(){
        return SOTM;
    }
    
}
