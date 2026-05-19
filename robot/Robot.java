package robot;

import robot.enums.DriveTrain;

public abstract class Robot {
    protected DriveTrain driveTrain;
    protected int fuelCapacity;
    protected boolean extendoHopper;
    protected boolean trench;
    protected boolean bump;
    
    public Robot(DriveTrain driveTrain, int fuelCapacity, boolean extendoHopper, boolean trench, boolean bump){
        this.driveTrain = driveTrain;
        this.fuelCapacity = fuelCapacity;
        this.extendoHopper = extendoHopper;
        this.trench = trench;
        this.bump = bump;
    }

    // Accessors
    public DriveTrain getDriveTrain(){
        return this.driveTrain;
    }

    public int getFuelCapacity(){
        return this.fuelCapacity;
    }

    public boolean hasExtendoHopper(){
        return this.extendoHopper;
    }

    public boolean canDoTrench(){
        return this.trench;
    }

    public boolean canDoBump(){
        return this.bump;
    }
}
