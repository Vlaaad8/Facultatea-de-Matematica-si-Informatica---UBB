package com.example;

import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class OpsBenchmark {

    @Param({"RANDOM", "ASC", "DESC"})
    private String scenario;

    private DoubleO doubleO;
    private DoubleP doubleP;

    private Double[] doubleObjects;
    private double[] doublePrimitives;
    private static final int SIZE = 1000; // 100M

    @Setup(Level.Trial)
    public void setup() {
        Random rand = new Random();

        doubleObjects = new Double[SIZE];
        doublePrimitives = new double[SIZE];

        for (int i = 0; i < SIZE; i++) {
            double val = rand.nextDouble();
            doubleObjects[i] = val;
            doublePrimitives[i] = val;
        }

        switch (scenario) {
            case "ASC":
                Arrays.sort(doubleObjects, Collections.reverseOrder());
                Arrays.sort(doublePrimitives);
                break;
            case "DESC":
                Arrays.sort(doubleObjects, Collections.reverseOrder());
                for (int i = 0; i < SIZE / 2; i++) {
                    Double temp = doubleObjects[i];
                    doubleObjects[i] = doubleObjects[SIZE - 1 - i];
                    doubleObjects[SIZE - 1 - i] = temp;
                }
                Arrays.sort(doublePrimitives);
                for (int i = 0; i < SIZE / 2; i++) {
                    double temp = doublePrimitives[i];
                    doublePrimitives[i] = doublePrimitives[SIZE - 1 - i];
                    doublePrimitives[SIZE - 1 - i] = temp;
                }
                break;
            case "RANDOM":
            default:
                // nothing
        }

        // Creează instanțele o singură dată
        doubleO = new DoubleO(doubleObjects);
        doubleP = new DoubleP(doublePrimitives);
    }

    @Benchmark
    public double sumObjects() {
        return doubleO.sum();
    }

    @Benchmark
    public double sumPrimitives() {
        return doubleP.sum();
    }

    @Benchmark
    public double averageObjects() {
        return doubleO.average();
    }

    @Benchmark
    public double averagePrimitives() {
        return doubleP.average();
    }

    @Benchmark
    public List<Double> topObjects() {
        return doubleO.top();
    }

    @Benchmark
    public double[] topPrimitives() {
        return doubleP.top();
    }
}
