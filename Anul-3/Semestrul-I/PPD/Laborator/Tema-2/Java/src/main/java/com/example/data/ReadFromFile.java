package com.example.data;

import java.io.*;
import java.util.Scanner;

public class ReadFromFile {

    public static int[][] readMatrix(String fileName) {
        int[][] matrix;
        try(Scanner sc = new Scanner(new File(fileName))) {
            int rows = sc.nextInt();
            int cols = sc.nextInt();
            matrix = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    matrix[i][j] = sc.nextInt();
                }
            }
            return matrix;
        }
        catch(FileNotFoundException e) {
            throw new RuntimeException("File not found");
        }
    }
}
