package com.example;

public class Addition implements Operation {

    @Override
    public double execute(double... numbers) {
        if (numbers.length > 1){
            double sum = 0.0;
            for(double num : numbers){
                sum += num;
            }
            return sum;
        }
        else{
            throw new OperationException("Adding should is between at least 2 numbers");
        }
    }
}
