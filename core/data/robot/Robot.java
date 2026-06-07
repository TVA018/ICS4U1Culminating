package core.data.robot;

import core.data.robot.enums.DriveTrain;
import util.TerminalTextFormatter;
import util.TerminalTextFormatter.ANSIFlag;

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

    /** 
     * @return Drivetrain type as an enum
     */
    public DriveTrain getDriveTrain(){
        return driveTrain;
    }

    /** 
     * @return int of total fuel capacity
     */
    public int getFuelCapacity(){
        return fuelCapacity;
    }

    /** 
     * @return true if a team has an extendable hopper mechanism
     */
    public boolean hasExtendoHopper(){
        return extendoHopper;
    }

    /** 
     * @return true if a team can go under the trench
     */
    public boolean canDoTrench(){
        return trench;
    }

    /** 
     * @return true if a team can traverse the bump
     */
    public boolean canDoBump(){
        return bump;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder
            .append(TerminalTextFormatter.applyFlags(getClass().getSimpleName() + ":", ANSIFlag.ITALIC) + "\n")
            .append("- GENERAL\n")
            .append(" > Drivetrain: " + driveTrain.toString() + "\n")
            .append(" > Fuel Capacity: " + String.valueOf(fuelCapacity) + "\n");

        if(extendoHopper) builder.append(" > Has Extendo-Hopper\n");
        
        builder.append(" > Can Traverse ");

        if(bump) {
            if(trench) {
                builder.append("Bump and Trench");
            } else {
                builder.append("Bump Only");
            }
        } else if(trench) {
            builder.append("Trench Only");
        } else {
            builder.append("nothing");
        }

        return builder.toString();
    }
}
