package org.sabulla;

import java.util.HashMap;
import java.util.Map;

public class Metrics {
    private final Map<String, Integer> operations = new HashMap<>();
    private long startTime, time;

    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        time = System.nanoTime() - startTime;
    }

    public void addOperations(String name) {
        operations.put(name, operations.getOrDefault(name, 0) + 1);
    }

    public double getTimeNs() {
        return time / 1000000.0;
    }

    public void print(String label) {
        System.out.println(" ---Metrics: " + label + "---");
        for (var e : operations.entrySet()) {
            System.out.println(e.getKey() + ": " + e.getValue());
        }
        System.out.println("Execution time (ns): " + getTimeNs());
    }
}