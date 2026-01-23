package com.example;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DoubleO {
    private final Double[] list;

    public DoubleO(Double[] list) {
        this.list = list;
    }
    public Double sum() {
        return Arrays.stream(list).reduce(0.0 ,Double::sum);
    }

    public  Double average() {
        if (list.length == 0) return 0.0;
        int count = list.length;
        return sum() / count;
    }
    public List<Double> top() {
        int limit = list.length/ 10;
        return Arrays.stream(list).sorted(Comparator.reverseOrder()).limit(limit).collect(Collectors.toList());
    }
}
