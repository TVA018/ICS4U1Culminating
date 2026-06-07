package core.data.robot;

import core.data.robot.enums.DriveTrain;
import core.data.robot.enums.Indexer;

public abstract class ShooterBot extends Robot {
    protected double bps;
    protected boolean adjustableHood;
    protected boolean flywheel;
    protected Indexer indexer;
    protected boolean passing;
    protected boolean autoAim;

    /**
     * Constructs a new shooter robot
     * @param driveTrain The drivetrain type
     * @param fuelCapacity How much fuel can the robot hold within itself
     * @param extendoHopper Does it have an extendo-hopper?
     * @param trench Can the robot go under the trench?
     * @param bump Can the robot go over the bump?
     * @param bps How much fuel can the robot shoot per second
     * @param adjustableHood Does the robot have an adjustable hood?
     * @param flywheel Does the robot have a flywheel?
     * @param indexer The type of indexer the robot uses
     * @param passing Can the robot pass?
     * @param autoAim Can the robot automatically aim?
     */
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

    /** 
     * @return a double of the team's bps
     */
    public double getBps(){
        return bps;
    }
    
    /** 
     * @return true if a team has an adjustable hood
     */
    public boolean hasAdjustableHood(){
        return adjustableHood;
    }

    /** 
     * @return true if a team has an inertia wheel
     */
    public boolean hasFlywheel(){
        return flywheel;
    }

    /** 
     * @return the type of indexer as an enum
     */
    public Indexer getIndexerType(){
        return indexer;
    }

    /** 
     * @return true if a team does passing
     */
    public boolean canPass(){
        return passing;
    }

    /** 
     * @return true if a team has automatic aiming
     */
    public boolean canAutoAim(){
        return autoAim;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(super.toString());

        builder
            .append("\n- SHOOTING\n")
            .append(" > BPS: " + String.valueOf(bps) + "\n")
            .append(adjustableHood ? " > Adjustable Hood" : " > Fixed Hood").append("\n")
            .append(flywheel ? " > Has Flywheel" : " > No Flywheel").append("\n")
            .append(" > Indexer: " + indexer.toString() + "\n")
            .append(passing ? " > Can Pass" : " > Cannot Pass").append("\n")
            .append(autoAim ? " > Auto-Aim" : " > Manual Aim");

        return builder.toString();
    }
}
