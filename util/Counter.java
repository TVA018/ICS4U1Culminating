package util;

public class Counter {
    private int value;

    public Counter(int start) {
        this.value = start;
    }

    public Counter() {
        this(0);
    }

    public void step() {
        value++;
    }

    public void step(int stepCount) {
        value += stepCount;
    }

    public int get() {
        return value;
    }
}
