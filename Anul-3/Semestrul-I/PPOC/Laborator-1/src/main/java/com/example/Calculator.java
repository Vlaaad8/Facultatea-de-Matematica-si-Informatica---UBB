package com.example;

import java.util.HashMap;
import java.util.Map;

public class Calculator {
    private final Map<String,Operation> operations = new HashMap<>();

    public Calculator(){
        operations.put("+",new Addition());
        operations.put("-",new Subtraction());
        operations.put("*",new Multiplication());
        operations.put("/",new Division());
        operations.put("min",new Min());
        operations.put("max",new Max());
        operations.put("sqrt",new Sqrt());
    }

    public double run(String operation,double... numbers){
        Operation op = operations.get(operation);
        return op.execute(numbers);
    }
}
