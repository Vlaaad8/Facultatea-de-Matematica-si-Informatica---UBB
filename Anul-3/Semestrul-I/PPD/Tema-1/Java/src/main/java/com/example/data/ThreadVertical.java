package com.example.data;

public class ThreadVertical extends Thread {
    private final int[][] matrix;
    private final int[][] filter;
    private int[][] v;
    private final int startRow, endRow;

    public ThreadVertical(int[][] matrix, int[][] filter, int[][] v, int startRow, int endRow) {
        this.matrix = matrix;
        this.filter = filter;
        this.v = v;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    public void run() {
        for (int i = 0; i < matrix.length; i++){
        for (int j = startRow; j < endRow; j++) {
                v[i][j] = ConvolutionS.computeElement(matrix, filter, i, j);
            }
        }


    }
}
