package util;

import java.util.ArrayList;
import java.util.stream.IntStream;

public final class TestArrayGenerator {
    private TestArrayGenerator() {}

    /** Generates an array of integers where each element is a random unique integer in the range [0, numItems)
     * @param numItems the number of items in the array
     */
    public static Integer[] generateIntArray(int numItems) {
        // A range from [0, numItems)
        ArrayList<Integer> itemPool = new ArrayList<>(IntStream.range(0, numItems).boxed().toList());

        Integer[] arr = new Integer[numItems]; // The array to return

        for(int i = 0; i < arr.length; i++) {
            // Grab a random item from the pool of available integers
            int poolIndex = (int) (Math.random() * itemPool.size());

            arr[i] = itemPool.get(poolIndex);
            itemPool.remove(poolIndex); // Remove the item from the pool
        }

        return arr;
    }
}
