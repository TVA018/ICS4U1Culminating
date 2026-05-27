package data.robot;

import data.robot.enums.DriveTrain;

public class DefenceBot extends Robot{
    private boolean shotBlocker;

    public DefenceBot(DriveTrain driveTrain, int fuelCapacity, boolean extendoHopper, boolean trench, boolean bump, boolean shotBlocker) {
        super(driveTrain, fuelCapacity, extendoHopper, trench, bump);
        this.shotBlocker = shotBlocker;
    }

    // Accessors

    /** 
     * @return true if a team has a shot blocking mechanism
     */
    public boolean hasShotBlocker(){
        return shotBlocker;
    }
}
