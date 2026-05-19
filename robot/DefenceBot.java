package robot;

import robot.enums.DriveTrain;

public class DefenceBot extends Robot{
    private boolean shotBlocker;

    public DefenceBot(DriveTrain driveTrain, int fuelCapacity, boolean extendoHopper, boolean trench, boolean bump, boolean shotBlocker) {
        super(driveTrain, fuelCapacity, extendoHopper, trench, bump);
        this.shotBlocker = shotBlocker;
    }

    //Accessors
    public boolean hasShotBlocker(){
        return this.shotBlocker;
    }
}
