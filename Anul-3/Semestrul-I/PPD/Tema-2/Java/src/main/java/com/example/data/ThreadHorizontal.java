package com.example.data;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class ThreadHorizontal extends Thread {
    private int[][] matrix;
    private int[][] filter;
    private int startRow, endRow;
    private int N;
    private int M;
    private CyclicBarrier barrier;

    public ThreadHorizontal(int[][] matrix, int[][] filter, int startRow, int endRow,int N,int M, CyclicBarrier barrier) {
        this.matrix = matrix;
        this.filter = filter;
        this.startRow = startRow;
        this.endRow = endRow;
        this.N = N;
        this.M = M;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        int[] previousRow=new int[M];
        int[] currentRow=new int[M];

        int[] upRow= null;
        int[] downRow = null;

        if(startRow > 0){
            upRow= new int[M];
            for(int i=0;i<M;i++){
                upRow[i] = matrix[startRow-1][i];
            }
        }
        if(endRow < N){
            downRow= new int[M];
            for(int i =0 ;i<M;i++){
                downRow[i] = matrix[endRow][i];
            }
        }
        try {
            barrier.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }

        if(startRow == 0){
            for(int i=0;i<M;i++){
                previousRow[i] = matrix[0][i];
            }
        }
        else{
            for(int i=0;i<M;i++){
                previousRow[i]= upRow[i];
            }
        }


        for(int i=startRow; i<endRow; i++) {
            for(int j=0;j<M; j++) {
                currentRow[j] = matrix[i][j];

            }
            int[] bellowRow= new int[M];
            if(i == endRow-1){
                if(endRow==N){
                    for(int j=0;j<M;j++){
                        bellowRow[j] = matrix[N-1][j];
                    }
                }
                else{
                    for(int j=0;j<M;j++){
                        bellowRow[j] = downRow[j];
                    }
                }
            }
            else{
                for(int j=0;j<M;j++){
                    bellowRow[j] = matrix[i+1][j];
                }
            }
            for(int j=0;j<M;j++){
                int value = ConvolutionH.computeElement(j,previousRow,currentRow,bellowRow);
                matrix[i][j] = value;
            }
            int[] temp = previousRow;
            previousRow = currentRow;
            currentRow = temp;
        }



    }
}
