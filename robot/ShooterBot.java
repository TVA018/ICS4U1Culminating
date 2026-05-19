package robot;

import robot.enums.DriveTrain;
import robot.enums.Indexer;

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
        return this.bps;
    }
    
    public boolean hasAdjustableHood(){
        return this.adjustableHood;
    }

    public boolean hasFlywheel(){
        return this.flywheel;
    }

    public Indexer getIndexerType(){
        return this.indexer;
    }

    public boolean canPass(){
        return this.passing;
    }

    public boolean canAutoAim(){
        return this.autoAim;
    }
    
    
}
