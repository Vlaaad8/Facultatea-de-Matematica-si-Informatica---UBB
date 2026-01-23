package com.example;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BigDecimals {
    private final List<BigDecimal> list;

    public BigDecimals(List<BigDecimal> list) {
        this.list = list;
    }
    public BigDecimal sum() {
        return this.list.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public  BigDecimal average() {
        if (list.isEmpty()) return BigDecimal.ZERO;
        else {
            BigDecimal count = BigDecimal.valueOf(list.size());
            return this.list.stream().reduce(BigDecimal.ZERO, BigDecimal::add).divide(count);
        }
    }
    public List<BigDecimal> top() {
        int limit = list.size() / 10;
        return this.list.stream().sorted(Comparator.reverseOrder()).limit(limit).collect(Collectors.toList());
    }
}
