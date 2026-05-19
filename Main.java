import robot.TurretBot;
import robot.enums.DriveTrain;
import robot.enums.Indexer;
import java.util.Arrays;
import java.util.List;

import util.Algorithms;
import util.Benchmark;
import util.TestArrayGenerator;

public class Main {
    public static void main(String[] args) {
        TurretBot WARP7 = new TurretBot(DriveTrain.SWERVE, 10000, true, true, true, 100, true, true, Indexer.DYE_ROTOR, true, true, 1, 10000, true);
        System.out.println(WARP7.canAutoAim());

        // Benchmarking sorting implementations
        List<Integer> testArray = Arrays.asList(TestArrayGenerator.generateIntArray(100000));
        
        Benchmark.timedPrint("Sorting", () -> Algorithms.mergeSorted(testArray, (a, b) -> a - b));
        Benchmark.timedPrint("Build-in Sorting", () -> testArray.sort((a, b) -> a - b));
    }
}