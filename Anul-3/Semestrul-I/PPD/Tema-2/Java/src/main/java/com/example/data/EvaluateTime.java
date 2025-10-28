package com.example.data;

public class EvaluateTime {
    private int[][] matrix;
    private int[][] filter;
    private final int thread;
    private final int N;
    private final int M;
    private final int K;

    public EvaluateTime(int N,int M,int P, int K) {
        this.N = N;
        this.M = M;
        this.thread = P;
        this.K = K;
    }

    public void run(int thread) throws InterruptedException {
        System.out.println("Tip Matrice: N="+N+" M="+M);
        System.out.println("Tip filter: n=m="+K);
        System.out.println("Secvential:"+ estimateConvS()+"ms");
        System.out.println("Thread Orizontal cu P="+thread+": "+estimateConvH(thread)+"ms");
    }

    public double estimateConvS() {
        matrix = ReadFromFile.readMatrix("matrix.txt");
        filter = ReadFromFile.readMatrix("filter.txt");
            long startTimeS = System.nanoTime();
            ConvolutionS convolution = new ConvolutionS(matrix, filter,N,M,K);
            convolution.calculateConvolution();
            long endTimeS = System.nanoTime();
            MatrixGenerator.writeMatrix(convolution.getMatrix(),"result.txt");
            double timeS = (endTimeS - startTimeS) / 1e6;

        return timeS;
    }

    public double estimateConvH(int threads) throws InterruptedException {
        matrix = ReadFromFile.readMatrix("matrix.txt");
        filter = ReadFromFile.readMatrix("filter.txt");
            long startTimeH = System.nanoTime();
            ConvolutionH convolutionH = new ConvolutionH(threads, N,M,K, matrix, filter);
            convolutionH.calculateConvolution("resultH.txt");
            long endTimeH = System.nanoTime();
            MatrixGenerator.writeMatrix(convolutionH.getNewMatrix(),"resultH.txt");
            double timeH = (endTimeH - startTimeH) / 1e6;
            return timeH;
    }

}
