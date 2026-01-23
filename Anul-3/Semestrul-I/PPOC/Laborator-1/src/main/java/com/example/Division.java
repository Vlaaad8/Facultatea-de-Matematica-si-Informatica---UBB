package com.example;

public class Division implements Operation{
    @Override
    public double execute(double... numbers) {
        if (numbers.length > 1) {
            double result = numbers[0];
            for (int i = 1; i < numbers.length; i++) {
                if (numbers[i] == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                result /= numbers[i];
            }
            return result;
        }
        else{
            throw new OperationException("Division should is between at least 2 numbers");
        }
    }
}
