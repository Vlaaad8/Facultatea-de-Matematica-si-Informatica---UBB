package com.example;

public class Sqrt implements Operation {
    @Override
    public double execute(double... numbers) {
        if (numbers.length == 1) {
            return Math.sqrt(numbers[0]);
        } else {
            throw new OperationException("Sqrt is only for one number");
        }
    }
}

