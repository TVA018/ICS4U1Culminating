import util.SimpleJSon;

public class Main {
    public static void main(String[] args) {
        // TurretBot WARP7 = new TurretBot(DriveTrain.SWERVE, 10000, true, true, true, 100, true, true, Indexer.DYE_ROTOR,
        //         true, true, 1, 10000, true);
        // System.out.println(WARP7.canAutoAim());

        // Benchmarking sorting implementations
        // Integer[] generatedArray = TestArrayGenerator.generateIntArray(10);
        // ArrayList<Integer> testList = new ArrayList<>(Arrays.asList(generatedArray));

        // Benchmark.timedPrint("Sorting", () -> Algorithms.mergeSort(testList, (a, b) -> a - b));

        // for (int i = -5; i < testList.size() + 5; i++) {
        //     int temp = i;
        //     System.out.printf("Searching for %s\n", i);
        //     System.out.printf("Item found: %s\n", Algorithms.binarySearch(testList, v -> v - temp));
        // }

        // Test reading TBA API
        // APIFetcher.getMatches("2026oncmp1");
        new SimpleJSon("{ \"key\"}");
    }
}