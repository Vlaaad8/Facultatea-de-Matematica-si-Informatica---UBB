package com.example.data;

public class ConvolutionH {
    private final int P;
    private final int N;
    private final int M;
    private ThreadHorizontal[] threads;
    private int[][] matrix;
    private int[][] filter;
    public int[][] newMatrix;

    public ConvolutionH(int p, int n,int m,int[][] matrix, int[][] filter) {
        P = p;
        N = n;
        M = m;
        threads = new ThreadHorizontal[P];
        this.matrix = matrix;
        this.filter = filter;
        newMatrix= new int[N][M];

    }

    public void calculateConvolution(String resultFile) throws InterruptedException {

        int rowPerThread= N/P;
        int extra = N%P;

        int startIdx= 0 ;
        for(int i= 0 ;i<P;i++){
            int endIdx = startIdx+rowPerThread;
            if(extra > 0){
                endIdx++;
                extra--;
            }
            threads[i]= new ThreadHorizontal(matrix,filter,newMatrix,startIdx,endIdx);

            threads[i].start();
            startIdx=endIdx;
        }
        for(ThreadHorizontal thread: threads){
            thread.join();
        }

        //MatrixGenerator.writeMatrix(newMatrix, resultFile);

    }

    public int[][] getNewMatrix() {
        return newMatrix;
    }
}
