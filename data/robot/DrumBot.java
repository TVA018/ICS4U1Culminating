package data.robot;

import data.robot.enums.DriveTrain;
import data.robot.enums.Indexer;

public class DrumBot extends ShooterBot{
    private double drumBallWidth;

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

    // Accessors

    /** 
     * @return a double of how many balls can fit through a team's shooter at once
     */
    public double getDrumBallWidth() {
        return drumBallWidth;
    }
}
