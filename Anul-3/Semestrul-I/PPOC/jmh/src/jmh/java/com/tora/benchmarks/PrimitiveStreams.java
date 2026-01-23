package com.tora.benchmarks;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;


@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 20, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class PrimitiveStreams {
    @Param({"1", "31", "65", "101", "103", "1024", "10240", "65535", "21474836"})
    public int size;

    @Benchmark
    public void primitive(Blackhole consumer) {
        consumer.consume(IntStream.rangeClosed(0, size)
                .map(i -> i + 1)
                .max());
    }

    @Benchmark
    public void primitive_parallel(Blackhole consumer) {
        consumer.consume(IntStream.rangeClosed(0, size)
                .map(i -> i + 1)
                .parallel()
                .max());
    }

    @Benchmark
    public void boxing(Blackhole consumer) {
        consumer.consume(Stream.iterate(0, i -> i + 1)
                .limit(size)
                .max(Integer::compareTo));
    }

    @Benchmark
    public void boxing_parallel(Blackhole consumer) {
        consumer.consume(Stream.iterate(0, i -> i + 1)
                .limit(size)
                .parallel()
                .max(Integer::compareTo));
    }

}
