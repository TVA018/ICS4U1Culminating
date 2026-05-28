package util;

import java.util.HashMap;

public final class Benchmark {
    private Benchmark() {}

    private static final HashMap<String, Long> runningStartTimes = new HashMap<>();

    /** Unit for Time */
    public enum TimeUnit {
        SECONDS("s", 1_000_000_000),
        MILLISECONDS("ms", 1_000_000),
        MICROSECONDS("µs", 1_000),
        NANOSECONDS("ns", 1);

        public final String symbol;
        public final int multiplier;

        private TimeUnit(String symbol, int multiplier) {
            this.symbol = symbol;
            this.multiplier = multiplier;
        }
    }

    public static String nanoTimeTo(long nanoTime, TimeUnit timeUnit) {
        return String.format("%s %s", (double) nanoTime / timeUnit.multiplier, timeUnit.symbol);
    }

    /**
     * Times how long it takes to run the {@code target} function
     * @param target The function to run
     * @return the time taken in nanoseconds
     */
    public static long time(Runnable target) {
        long startTime = System.nanoTime();
        target.run();
        return System.nanoTime() - startTime;
    }

    /**
     * Times how long it takes to run the {@code target} function, and prints out the time taken
     * @param identifier An identifier to represent the function. Used when outputting the time elapsed
     * @param target The function to run
     * @param timeUnit The time unit to print the result as
     * @return The time taken to run the function in nanoseconds
     */
    public static long timedPrint(String identifier, Runnable target, TimeUnit timeUnit) {
        long timeElapsed = time(target);
        
        TerminalTextFormatter.println(
            String.format("Time taken to run %s: %s", identifier, nanoTimeTo(timeElapsed, timeUnit)), 
            TerminalTextFormatter.ANSIFlag.YELLOW_TEXT, 
            TerminalTextFormatter.ANSIFlag.ITALIC
        );

        return timeElapsed;
    }

    /** 
     * Defaults time unit to milliseconds
     * @see #timedPrint(String, Runnable, TimeUnit)
     */
    public static long timedPrint(String identifier, Runnable target) {
        return timedPrint(identifier, target, TimeUnit.MILLISECONDS);
    }

    public static void startTimer(String identifier) {
        runningStartTimes.put(identifier, System.nanoTime());
    }

    public static void endTimer(String identifier, TimeUnit timeUnit) {
        Long start = runningStartTimes.get(identifier);
        if(start == null) {
            System.err.println("Could not find a start time for " + identifier);
            return;
        }

        long nanoElapsed = System.nanoTime() - start;

        TerminalTextFormatter.println(
            String.format("Time taken to run %s: %s", identifier, nanoTimeTo(nanoElapsed, timeUnit)), 
            TerminalTextFormatter.ANSIFlag.YELLOW_TEXT, 
            TerminalTextFormatter.ANSIFlag.ITALIC
        );
    }

    public static void endTimer(String identifier) {
        endTimer(identifier, TimeUnit.MILLISECONDS);
    }
}