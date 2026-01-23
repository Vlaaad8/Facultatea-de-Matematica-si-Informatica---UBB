package com.example;

public class Max implements Operation {
    @Override
    public double execute(double... numbers) {
        if (numbers.length > 1) {
            double max = numbers[0];
            for (int i = 1; i < numbers.length; i++) {
                if (numbers[i] > max) {
                    max = numbers[i];
                }
            }
            return max;
        } else {
            throw new OperationException("Max should is between at least 2 numbers");
        }
    }
}