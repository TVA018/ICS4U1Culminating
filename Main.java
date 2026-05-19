
import robot.TurretBot;
import robot.enums.DriveTrain;
import robot.enums.Indexer;

public class Main {
    public static void main(String[] args) {
        TurretBot WARP7 = new TurretBot(DriveTrain.SWERVE, 10000, true, true, true, 100, true, true, Indexer.DYE_ROTOR, true, true, 1, 10000, true);
        System.out.println(WARP7.canAutoAim());
    }
}