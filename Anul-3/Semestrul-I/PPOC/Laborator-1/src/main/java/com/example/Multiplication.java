package com.example;

public class Multiplication implements Operation {
    @Override
    public double execute(double... numbers) {
        if (numbers.length > 1) {
            double result = numbers[0];
            for (int i = 1; i < numbers.length; i++) {
                result *= numbers[i];
            }
            return result;
        } else {
            throw new OperationException("Multiplication should is between at least 2 numbers");
        }
    }
}
