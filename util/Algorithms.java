package util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Algorithms {
    private Algorithms() {}

    /**
     * Sorts a List of elements in-place
     * @param <T> The type of each element
     * @param list The list to sort
     * @param comparator A function that compares two values A and B, and returns: <ul>
     *  <li>negative if A should be placed before B</li>
     *  <li>positive if B should be placed before A</li>
     *  <li>0 if A and B are equivalent</li>
     * </ul>
     */
    public static <T> void mergeSort(List<T> list, Comparator<T> comparator) {
        if(list.size() < 2) { // Base case
            return;
        }

        int middleIndex = list.size() / 2;

        // Have to actually create new ArrayLists from the sublists
        // because subList() just creates a view into the list, meaning
        // the subList's elemenets point directly to the same memory locations as
        // those in the original list
        // Therefore, if you mutate the original list, it is reflected in the sublists, which
        // will break the in-place sorting
        List<T> leftHalf = new ArrayList<>(list.subList(0, middleIndex));
        List<T> rightHalf = new ArrayList<>(list.subList(middleIndex, list.size()));

        // Sort the halves recursively
        mergeSort(leftHalf, comparator);
        mergeSort(rightHalf, comparator);

        // Merge the sorted halves together
        merge(list, leftHalf, rightHalf, comparator);
    }

    /**
     * Merges two sorted lists into a single larger sorted list
     * @param <T> The type of each element in a given list
     * @param original The list to copy the elements into. This is likely the original list you are sorting
     * @param list1 The first sorted (sub)list
     * @param list2 The second sorted (sub)list
     * @param comparator A function that compares two values A and B, and returns: <ul>
     *  <li>negative if A should be placed before B</li>
     *  <li>positive if B should be placed before A</li>
     *  <li>0 if A and B are equivalent</li>
     * </ul>
     */
    private static <T> void merge(List<T> original, List<T> list1, List<T> list2, Comparator<T> comparator) {
        int mergedIndex = 0; // The index of the final, merged list
        int list1Index = 0; // The index for list1
        int list2Index = 0; // The index for list2

        // While neither lists have been fully parsed
        while(list1Index < list1.size() && list2Index < list2.size()) {
            T list1Item = list1.get(list1Index);
            T list2Item = list2.get(list2Index);

            // Item in list1 is greater than item in list2
            if(comparator.compare(list1Item, list2Item) > 0) {
                // Add the smaller item (the one from list2), and increment the index
                original.set(mergedIndex++, list2Item);
                list2Index++;
            } else {
                // Add the smaller item (the one from list1), and increment the index
                original.set(mergedIndex++, list1Item);
                list1Index++;
            }
        }

        // Add remaining items
        while (list1Index < list1.size()) {
            original.set(mergedIndex++, list1.get(list1Index++));
        }

        while (list2Index < list2.size()) {
            original.set(mergedIndex++, list2.get(list2Index++));
        }
    }

    /**
     * Performs a binary search on the list
     * @param <T> The type of each element in the list
     * @param list The list to search, it must be a SORTED list
     * @param comparator A function that takes in an item X and returns:<ul>
     *  <li>negative if X comes logically before the target value</li>
     *  <li>positive if X comes logically after the target value</li>
     *  <li>0 if X matches the target value</li>
     * </ul>
     * @return Optional.empty if the item couldn't be found, the item otherwise if it is found
     */
    public static <T> Optional<T> binarySearch(List<T> list, Function<T, Integer> comparator) {
        int lowerBound = 0; // inclusive
        int upperBound = list.size() - 1; // inclusive

        // While the range between the lower and upper bound is not <= 0
        while (lowerBound <= upperBound) {
            // Get the middle value between the lower and upper bound
            int currentIndex = lowerBound + ((upperBound - lowerBound) / 2);
            T middleItem = list.get(currentIndex);

            int comparison = comparator.apply(middleItem);

            if(comparison > 0) { // The middle item comes after the target value, search the lower half
                upperBound = currentIndex - 1;
            } else if(comparison < 0) { // The middle item comes before the target value, search the upper half
                lowerBound = currentIndex + 1;
            } else { // Target item found
                return Optional.of(middleItem);
            }
        }

        return Optional.empty();
    }

    /**
     * Filters a list by a given predicate
     * @param <T> The type of each item in the list
     * @param original The original list
     * @param predicate A function that takes in an item and returns true if it passes the filter
     * @return The filtered list
     */
    public static <T> List<T> filter(List<T> original, Predicate<T> predicate) {
        ArrayList<T> filteredList = new ArrayList<>();

        for (T item : original) {
            if(predicate.test(item))
                filteredList.add(item);
        }

        return filteredList;
    }
}
