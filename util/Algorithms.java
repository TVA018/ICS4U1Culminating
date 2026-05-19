package util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class Algorithms {
    private Algorithms() {}

    // TODO: Convert this to an in-place sort that doesn't have to create a separate array.
    // Also convert to an iterative approach rather than recursive approach
    public static <T> List<T> mergeSorted(List<T> list, Comparator<T> comparator) {
        if(list.size() == 1) {
            return list;
        }

        int middleIndex = list.size() / 2;
        List<T> sortedLeftHalf = mergeSorted(list.subList(0, middleIndex), comparator);
        List<T> sortedRightHalf = mergeSorted(list.subList(middleIndex, list.size()), comparator);

        return merge(sortedLeftHalf, sortedRightHalf, comparator);
    }

    private static <T> List<T> merge(List<T> list1, List<T> list2, Comparator<T> comparator) {
        int totalItems = list1.size() + list2.size();
        ArrayList<T> mergedList = new ArrayList<>(totalItems);

        int list1Index = 0;
        int list2Index = 0;

        for(int mergedIndex = 0; mergedIndex < totalItems; mergedIndex++) {
            if(list1Index == list1.size()) { // list1 has been completely run through
                mergedList.add(list2.get(list2Index));
                list2Index++;
            } else if(list2Index == list2.size()) { // list2 has been completely run through
                mergedList.add(list1.get(list1Index));
                list1Index++;
            } else {
                T list1Item = list1.get(list1Index);
                T list2Item = list2.get(list2Index);

                // Item in list1 is greater than item in list2
                if(comparator.compare(list1Item, list2Item) > 0) {
                    mergedList.add(list2Item);
                    list2Index++;
                } else {
                    mergedList.add(list1Item);
                    list1Index++;
                }
            }
        }

        return mergedList;
    }
}
