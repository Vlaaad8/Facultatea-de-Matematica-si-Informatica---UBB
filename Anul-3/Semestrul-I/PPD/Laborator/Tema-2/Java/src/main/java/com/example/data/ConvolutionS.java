package com.example.data;

public class ConvolutionS {

    private int[][] matrix;
    private int[][] filter;
    private int N;
    private int M;
    private int K;

    public ConvolutionS(int[][] matrix, int[][] filter,int N,int M,int K) {
        this.matrix = matrix;
        this.filter = filter;
        this.N = N;
        this.M = M;
        this.K = K;
    }

    public void calculateConvolution() {
        int[] previousRow = new int[M];
        int[] currentRow = new int[M];
        int[] belowRow = new int[M];

        for (int i = 0; i < M; i++) {
            previousRow[i] = matrix[0][i];
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                currentRow[j] = matrix[i][j];
            }
            int id;
            if (i < N - 1) {
                id = i + 1;
            } else {
                id = N - 1;
            }
            for (int z = 0; z < M; z++) {
                belowRow[z] = matrix[id][z];
            }

            for (int j = 0; j < M; j++) {
                int value = computeElement(j, previousRow, currentRow, belowRow);
                matrix[i][j] = value;
            }
            int[] temp = previousRow;
            previousRow = currentRow;
            currentRow = temp;
        }
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int computeElement(int j, int[] previousRow, int[] currentRow, int[] belowRow) {
        int half = K / 2;

        int sum = 0;

        for (int a = -half; a <= half; a++) {
            for (int b = -half; b <= half; b++) {
                int y = j + b;

                if (y < 0) y = 0;
                if (y >= M) y = M - 1;
                int value;
                if (a == -1){
                    value=previousRow[y];
                }
                else if (a==0){
                    value=currentRow[y];
                }
                else{
                    value=belowRow[y];
                }
                sum += value* filter[a+ half][b+ half];
            }
        }

        return sum;
    }


}
