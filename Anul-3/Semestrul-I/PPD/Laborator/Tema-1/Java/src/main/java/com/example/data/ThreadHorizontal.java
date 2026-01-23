package com.example.data;

public class ThreadHorizontal extends Thread {
    private int[][] matrix;
    private int[][] filter;
    private int[][] v;
    private int startRow, endRow;

    public ThreadHorizontal(int[][] matrix, int[][] filter, int[][] v, int startRow, int endRow) {
        this.matrix = matrix;
        this.filter = filter;
        this.v = v;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    public void run() {
        for(int i=startRow; i<endRow; i++) {
            for(int j=0;j<matrix[0].length; j++) {
                v[i][j]= ConvolutionS.computeElement(matrix,filter, i, j);
            }
        }


    }
}
