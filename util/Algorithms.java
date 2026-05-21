package util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class Algorithms {
    private Algorithms() {}

    // TODO: Convert this to an in-place sort that doesn't have to create a separate array.
    // Also convert to an iterative approach rather than recursive approach
    public static <T> void mergeSorted(List<T> list, Comparator<T> comparator) {
        if(list.size() < 2) {
            return;
        }

        int middleIndex = list.size() / 2;

        List<T> leftHalf = list.subList(0, middleIndex);
        List<T> rightHalf = list.subList(middleIndex, list.size());

        mergeSorted(leftHalf, comparator);
        mergeSorted(rightHalf, comparator);

        merge(list, leftHalf, rightHalf, comparator);
    }

    private static <T> void merge(List<T> original, List<T> list1, List<T> list2, Comparator<T> comparator) {
        int totalItems = list1.size() + list2.size();

        int list1Index = 0;
        int list2Index = 0;

        for(int mergedIndex = 0; mergedIndex < totalItems; mergedIndex++) {
            if(list1Index == list1.size()) { // list1 has been completely run through
                original.set(mergedIndex, list2.get(list2Index));
                list2Index++;
            } else if(list2Index == list2.size()) { // list2 has been completely run through
                original.set(mergedIndex, list1.get(list1Index));
                list1Index++;
            } else {
                T list1Item = list1.get(list1Index);
                T list2Item = list2.get(list2Index);

                // Item in list1 is greater than item in list2
                if(comparator.compare(list1Item, list2Item) > 0) {
                    original.set(mergedIndex, list2Item);
                    list2Index++;
                } else {
                    original.set(mergedIndex, list1Item);
                    list1Index++;
                }
            }
        }
    }
}
