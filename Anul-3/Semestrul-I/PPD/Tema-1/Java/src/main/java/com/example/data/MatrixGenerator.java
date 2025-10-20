package com.example.data;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class MatrixGenerator {

    public static Random rand = new Random();
    private final String matrixFile;
    private final String filterFile;
    private final int rows;
    private final int cols;
    private final int k;

    public MatrixGenerator(String matrixFile, String filterFile,int N,int M, int k) {
        this.matrixFile = matrixFile;
        this.filterFile = filterFile;
        this.rows = N;
        this.cols = M;
        this.k = k;
    }

    public static void writeMatrix(int[][] matrix, String filename) {
        try (PrintWriter out = new PrintWriter(filename)) {
            out.println(matrix.length+" "+matrix[0].length);
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    out.print(matrix[i][j] + " ");
                }
                out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateMatrix(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = rand.nextInt(100);
            }
        }
        writeMatrix(matrix, matrixFile);
    }

    private void generateFilter(int k) {
        int[][] matrix = new int[k][k];
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                matrix[i][j] = rand.nextInt(10);
            }
        }
        writeMatrix(matrix, filterFile);
    }

    public void run(){
        generateMatrix(rows, cols);
        generateFilter(k);
    }
}
