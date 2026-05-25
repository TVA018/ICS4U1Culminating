package util;

import java.util.function.Function;

public final class ComparatorFactory {
    private ComparatorFactory() {}

    /**
     * Creates a comparator for Algorithms.binarySearch() for a list in ASCENDING order<br>
     * 
     * For example:
     * 
     * <pre>
     * {@code
     * class Person {
     *   ...
     *   public int getAge() { ... }
     * }
     * 
     * ... {
     *   List<Person> people = ... // List of people sorted by age in ASCENDING ORDER
     * 
     *   // Find a person with the age 19
     *   Person nineteenYearOld = Algorithms.binarySearch(
     *     people,
     *     ascendingSearchComparator(
     *       19,
     *       Person::getAge // or do: person -> person.getAge()
     *     )
     *   )
     * }
     * }
     * </pre>
     * 
     * @param <T> The type of each item in the list
     * @param targetValue The target value you are aiming for after valueSupplier is applied on an item
     * @param valueSupplier Returns an integer representation for a given item
     * @return The comparator
     */
    public static <T> Function<T, Integer> ascendingSearchComparator(int targetValue, Function<T, Integer> valueSupplier) {
        return item -> valueSupplier.apply(item) - targetValue;
    }

    /**
     * Creates a comparator for Algorithms.binarySearch() for a list in DESCENDING order<br>
     * 
     * For example:
     * 
     * <pre>
     * {@code
     * class Person {
     *   ...
     *   public int getAge() { ... }
     * }
     * 
     * ... {
     *   List<Person> people = ... // List of people sorted by age in DESCENDING ORDER
     * 
     *   // Find a person with the age 19
     *   Person nineteenYearOld = Algorithms.binarySearch(
     *     people,
     *     descendingSearchComparator(
     *       19,
     *       Person::getAge // or do: person -> person.getAge()
     *     )
     *   )
     * }
     * }
     * </pre>
     * 
     * @param <T> The type of each item in the list
     * @param targetValue The target value you are aiming for after valueSupplier is applied on an item
     * @param valueSupplier Returns an integer representation for a given item
     * @return The comparator
     */
    public static <T> Function<T, Integer> descendingSearchComparator(int targetValue, Function<T, Integer> valueSupplier) {
        return item -> targetValue - valueSupplier.apply(item);
    }
}
