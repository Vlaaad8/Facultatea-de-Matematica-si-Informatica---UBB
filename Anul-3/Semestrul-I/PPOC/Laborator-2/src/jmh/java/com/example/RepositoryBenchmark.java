package com.example;


import com.example.Elipse.ElipseArrayRepository;
import com.example.Elipse.ElipseHashRepository;
import com.example.Fastutil.FastListRepository;
import com.example.Fastutil.FastSetRepository;
import com.example.JDK.ArrayRepository;
import com.example.JDK.ConcurrentHashRepository;
import com.example.JDK.HashRepository;
import com.example.JDK.TreeRepository;
import com.example.Koloboke.KolobokeMapRepository;
import com.example.Koloboke.KolobokeSetRepository;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 10, time = 1)
@Measurement(iterations = 20, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class RepositoryBenchmark {


    private InMemoryRepository<Integer> repository;
    @Param({"HashSet","ArrayList","ThreeSett","ConcurrentHash","FastSet","FastList","ElipseSet","ElipseArray","KolobokeSet","KolobokeMap"})
    private String repoType;

    @Setup(Level.Iteration)
    public void setUp(){
        switch (repoType) {
            case "HashSet": {
                repository = new HashRepository<>();
                break;
            }
            case "ArrayList": {
                repository = new ArrayRepository<>();
                break;
            }
            case "ThreeSett": {
                repository = new TreeRepository<>();
                break;
            }
            case "ConcurrentHash": {
                repository = new ConcurrentHashRepository<>();
                break;
            }
            case "FastSet": {
                repository = new FastSetRepository<>();
                break;
            }
            case "FastList": {
                repository = new FastListRepository();
                break;
            }
            case "ElipseSet": {
                repository = new ElipseHashRepository<>();
                break;
            }
            case "ElipseArray": {
                repository = new ElipseArrayRepository<>();
                break;
            }
            case "KolobokeSet": {
                repository = new KolobokeSetRepository<>();
                break;
            }
            case "KolobokeMap": {
                repository = new KolobokeMapRepository<>();
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown repository type: " + repoType);
        }

    }

    @Benchmark
    public void testAdd() {
        repository.add(1002);
    }

    @Benchmark
    public void testContains() {
        repository.contains(1002);
    }

    @Benchmark
    public void testRemove() {
        repository.remove(1002);
    }

}

