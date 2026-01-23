package com.example.data;

public class EvaluateTime {
    private final int[][] matrix;
    private final int[][] filter;
    private final int thread;
    private final int N;
    private final int M;
    private final int K;

    public EvaluateTime(int N,int M,int P, int K) {
        matrix = ReadFromFile.readMatrix("matrix.txt");
        filter = ReadFromFile.readMatrix("filter.txt");
        this.N = N;
        this.M = M;
        this.thread = P;
        this.K = K;
    }


    public void run(int thread) throws InterruptedException {
        System.out.println("Tip Matrice: N="+N+" M="+M);
        System.out.println("Tip filter: n=m="+K);
        System.out.println("Secvential:"+ estimateConvS()+"ms");
        System.out.println("Thread Vertical cu P="+thread+": "+estimateConvV(thread)+"ms");
        System.out.println("Thread Orizontal cu P="+thread+": "+estimateConvH(thread)+"ms");
    }

    public double estimateConvS() {
            long startTimeS = System.nanoTime();
            ConvolutionS convolution = new ConvolutionS(matrix, filter);
            convolution.calculateConvolution("result.txt");
            long endTimeS = System.nanoTime();
            MatrixGenerator.writeMatrix(convolution.getNewMatrix(),"result.txt");
            double timeS = (endTimeS - startTimeS) / 1e6;

        return timeS;
    }

    public double estimateConvH(int threads) throws InterruptedException {
            long startTimeH = System.nanoTime();
            ConvolutionH convolutionH = new ConvolutionH(threads, N,M, matrix, filter);
            convolutionH.calculateConvolution("resultH.txt");
            long endTimeH = System.nanoTime();
            MatrixGenerator.writeMatrix(convolutionH.getNewMatrix(),"resultH.txt");
            double timeH = (endTimeH - startTimeH) / 1e6;
            return timeH;
    }

    public double estimateConvV(int threads) throws InterruptedException {

            long startTimeV = System.nanoTime();
            ConvolutionV convolutionV = new ConvolutionV(threads, N,M, matrix, filter);
            convolutionV.calculateConvolution("resultV.txt");
            long endTimeV = System.nanoTime();
        System.out.println(convolutionV.getNewMatrix().length);
            MatrixGenerator.writeMatrix(convolutionV.getNewMatrix(),"resultV.txt");
            double timeV = (endTimeV - startTimeV) / 1e6;
            return timeV;
    }
}
