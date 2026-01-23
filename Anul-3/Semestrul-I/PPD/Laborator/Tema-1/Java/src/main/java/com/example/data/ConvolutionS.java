package com.example.data;

public class ConvolutionS {

    private int[][] matrix;
    private int[][] filter;
    public int[][] newMatrix;

    public ConvolutionS(int[][] matrix, int[][] filter) {
        this.matrix = matrix;
        this.filter = filter;
        newMatrix = new int[matrix.length][matrix[0].length];
    }

    public void calculateConvolution(String resultFile){
        int n=matrix.length;
        int m=matrix[0].length;
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                newMatrix[i][j] = computeElement(matrix, filter, i, j);
            }
        }

       // MatrixGenerator.writeMatrix(newMatrix,resultFile);

    }


    public int[][] getNewMatrix() {
        return newMatrix;
    }

    public static int computeElement(int[][] F, int[][] C, int i, int j) {
        int n = F.length;
        int m = F[0].length;
        int k = C.length;
        int half = k / 2;

        int sum = 0;

        for (int a = -half; a <= half; a++) {
            for (int b = -half; b <= half; b++) {
                int x = i + a;
                int y = j + b;


                if (x < 0) x = 0;
                if (y < 0) y = 0;
                if (x >= n) x = n - 1;
                if (y >= m) y = m - 1;

                sum += F[x][y] * C[a + half][b + half];
            }
        }

        return sum;
    }


}
