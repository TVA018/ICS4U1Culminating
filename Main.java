import data.robot.TurretBot;
import data.robot.enums.DriveTrain;
import data.robot.enums.Indexer;
import java.util.Arrays;
import java.util.List;

import java.util.ArrayList;
import util.Algorithms;
import util.Benchmark;
import util.TestArrayGenerator;

public class Main {
    class Person {
        public int getAge() {
            return 0;
        }
    }

    public static void main(String[] args) {
        TurretBot WARP7 = new TurretBot(DriveTrain.SWERVE, 10000, true, true, true, 100, true, true, Indexer.DYE_ROTOR,
                true, true, 1, 10000, true);
        System.out.println(WARP7.canAutoAim());

        // Benchmarking sorting implementations
        Integer[] generatedArray = TestArrayGenerator.generateIntArray(10);
        ArrayList<Integer> testList = new ArrayList<>(Arrays.asList(generatedArray));

        Benchmark.timedPrint("Sorting", () -> Algorithms.mergeSort(testList, (a, b) -> a - b));

        for (int i = -5; i < testList.size() + 5; i++) {
            int temp = i;
            System.out.printf("Searching for %s\n", i);
            System.out.printf("Item found: %s\n", Algorithms.binarySearch(testList, v -> v - temp));
        }
    }
}