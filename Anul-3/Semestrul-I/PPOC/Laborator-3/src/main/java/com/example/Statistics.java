package com.example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Statistics {
    public static void main(String[] args) {
        List<BigDecimal> list = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            double value = Math.random() * 100;
            list.add(new BigDecimal(value));
        }
        System.out.println("The list is: [");
        for(int i=0;i<100;i++){
            System.out.print(list.get(i)+" ");
        }
        System.out.println("]");

        BigDecimals operations=new BigDecimals(list);
        System.out.println("The sum is: "+ operations.sum());
        System.out.println("The average is: "+ operations.average());
    }
}


