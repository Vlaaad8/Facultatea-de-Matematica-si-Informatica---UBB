package com.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to PPOP Calculator");
        System.out.println("Write exit to stop");

        while(true){
            System.out.print(">");
            String input = scanner.nextLine().trim();
            if(input.equals("exit")){
                break;
            }

            String[] parts =input.split(" ");
            try{
                double[] numbers=new double[parts.length-1];
                for(int i=1;i<parts.length;i++){
                    numbers[i-1]=Double.parseDouble(parts[i]);
                }
                double result = calculator.run(parts[0],numbers);
                System.out.println("The result is " +result);
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }

    }
}