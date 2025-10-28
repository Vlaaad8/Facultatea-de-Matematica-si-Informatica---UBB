package com.example.data;

import java.util.concurrent.CyclicBarrier;

public class ConvolutionH {
    private static int P;
    private static int N;
    private static int M;
    private static int K;
    private ThreadHorizontal[] threads;
    private int[][] matrix;
    private static int[][] filter;

    public ConvolutionH(int p, int n,int m,int k,int[][] matrix, int[][] filter) {
        P = p;
        N = n;
        M = m;
        K = k;
        threads = new ThreadHorizontal[P];
        this.matrix = matrix;
        this.filter = filter;

    }

    public void calculateConvolution(String resultFile) throws InterruptedException {

        int rowPerThread= N/P;
        int extra = N%P;
        int startIdx= 0 ;
        CyclicBarrier barrier = new CyclicBarrier(P);

        for(int i= 0 ;i<P;i++){
            int endIdx = startIdx+rowPerThread;
            if(extra > 0){
                endIdx++;
                extra--;
            }
            threads[i]= new ThreadHorizontal(matrix,filter,startIdx,endIdx,N,M,barrier);
            threads[i].start();
            startIdx=endIdx;
        }
        for(ThreadHorizontal thread: threads){
            thread.join();
        }


        //MatrixGenerator.writeMatrix(newMatrix, resultFile);

    }

    public int[][] getNewMatrix() {
        return matrix;
    }

    public static int computeElement(int j, int[] previousRow, int[] currentRow, int[] belowRow) {
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
