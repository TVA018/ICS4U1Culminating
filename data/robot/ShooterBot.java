package data.robot;

import data.robot.enums.DriveTrain;
import data.robot.enums.Indexer;

public class ShooterBot extends Robot{
    protected double bps;
    protected boolean adjustableHood;
    protected boolean flywheel;
    protected Indexer indexer;
    protected boolean passing;
    protected boolean autoAim;

    public ShooterBot(DriveTrain driveTrain, int fuelCapacity, boolean extendoHopper, boolean trench, boolean bump,
            double bps, boolean adjustableHood, boolean flywheel, Indexer indexer, boolean passing, boolean autoAim) {
        super(driveTrain, fuelCapacity, extendoHopper, trench, bump);
        this.bps = bps;
        this.adjustableHood = adjustableHood;
        this.flywheel = flywheel;
        this.indexer = indexer;
        this.passing = passing;
        this.autoAim = autoAim;
    }

    //Accessors
    public double getBps(){
        return bps;
    }
    
    public boolean hasAdjustableHood(){
        return adjustableHood;
    }

    public boolean hasFlywheel(){
        return flywheel;
    }

    public Indexer getIndexerType(){
        return indexer;
    }

    public boolean canPass(){
        return passing;
    }

    public boolean canAutoAim(){
        return autoAim;
    }
    
    
}
