package core.data.robot;

import core.data.robot.enums.DriveTrain;

public class DefenceBot extends Robot {
    private boolean shotBlocker;

    /**
     * Constructs a new defence robot
     * @param driveTrain The drivetrain type
     * @param fuelCapacity How much fuel can the robot hold within itself
     * @param extendoHopper Does it have an extendo-hopper?
     * @param trench Can the robot go under the trench?
     * @param bump Can the robot go over the bump?
     * @param shotBlocker Does the robot have a shot-blocker?
     */
    public DefenceBot(DriveTrain driveTrain, int fuelCapacity, boolean extendoHopper, boolean trench, boolean bump, boolean shotBlocker) {
        super(driveTrain, fuelCapacity, extendoHopper, trench, bump);
        this.shotBlocker = shotBlocker;
    }

    /** 
     * @return true if a team has a shot blocking mechanism
     */
    public boolean hasShotBlocker(){
        return shotBlocker;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(super.toString());

        if(shotBlocker) builder.append("\n- Has shot blocker");

        return builder.toString();
    }
}
