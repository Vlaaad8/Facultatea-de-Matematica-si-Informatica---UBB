package com.example;

public class Min implements Operation {
    @Override
    public double execute(double... numbers) {
        if (numbers.length > 1) {
            double min = numbers[0];
            for (int i = 1; i < numbers.length; i++) {
                if (numbers[i] < min) {
                    min = numbers[i];
                }
            }
            return min;
        } else {
            throw new OperationException("Min should is between at least 2 numbers");
        }
    }
}
