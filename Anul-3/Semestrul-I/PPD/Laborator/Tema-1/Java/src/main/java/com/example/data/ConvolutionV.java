package com.example.data;

public class ConvolutionV {
    private final int P;
    private final int N;
    private final int M;
    private ThreadVertical[] threads;
    private int[][] matrix;
    private int[][] filter;
    public int[][] newMatrix;

    public ConvolutionV(int p, int n, int m,int[][] matrix, int[][] filter) {
        P = p;
        N = n;
        M = m;
        threads = new ThreadVertical[P];
        this.matrix = matrix;
        this.filter = filter;
        newMatrix = new int[N][M];

    }

    public int[][] getNewMatrix() {
        return newMatrix;
    }

    public void calculateConvolution(String resultFile) throws InterruptedException {

        int rowPerThread = M / P;
        int extra = M % P;

        int startIdx = 0;
        for (int i = 0; i < P; i++) {
            int endIdx = startIdx + rowPerThread;
            if (extra > 0) {
                endIdx++;
                extra--;
            }
            threads[i] = new ThreadVertical(matrix, filter, newMatrix, startIdx, endIdx);

            threads[i].start();
            startIdx = endIdx;
        }
        for (ThreadVertical thread : threads) {
            thread.join();
        }


    }
}
