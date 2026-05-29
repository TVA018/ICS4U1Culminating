package util;

/** A helper class that just holds an integer value that can be incremented */
public class Counter {
    private int value;

    /**
     * Constructs a new Counter starting at the given value
     * @param start The value to start at
     */
    public Counter(int start) {
        this.value = start;
    }

    /** Constructs a new Counter starting at 0 */
    public Counter() {
        this(0);
    }

    /** Increments the Counter by 1 */
    public void step() {
        value++;
    }

    /**
     * Increments the Counter by the given amount
     * @param stepCount The increment size
     */
    public void step(int stepCount) {
        value += stepCount;
    }

    /** @return The value of the Counter */
    public int get() {
        return value;
    }
}
