package com.example;

public class Subtraction implements Operation {
    @Override
    public double execute(double... numbers) {
        if (numbers.length > 1){
            double subtraction = numbers[0];
            for(int i=1;i<numbers.length;i++){
                subtraction -= numbers[i];
            }
            return subtraction;
        }
        else{
            throw new OperationException("Subtraction should is between at least 2 numbers");
        }
    }
}
