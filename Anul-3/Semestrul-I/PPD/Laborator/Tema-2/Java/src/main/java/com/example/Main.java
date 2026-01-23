package com.example;

import com.example.data.EvaluateTime;
import com.example.data.MatrixGenerator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import static java.lang.System.exit;

public class Main {
    private static int N;
    private static int Runs;
    private static int M;
    private static int P;


    public static boolean compareFiles(String path1, String path2) throws IOException {
        try (FileInputStream fis1 = new FileInputStream(path1);
             FileInputStream fis2 = new FileInputStream(path2)) {

            if (fis1.available() != fis2.available()) {
                return false;
            }

            byte[] buffer1 = new byte[4096];
            byte[] buffer2 = new byte[4096];

            int bytesRead1;
            int bytesRead2;

            while ((bytesRead1 = fis1.read(buffer1)) != -1) {
                bytesRead2 = fis2.read(buffer2);

                if (bytesRead1 != bytesRead2 || !Arrays.equals(
                        Arrays.copyOf(buffer1, bytesRead1),
                        Arrays.copyOf(buffer2, bytesRead2))) {
                    return false;
                }
            }
        }

        return true;
    }


    public static void main(String[] args) throws InterruptedException, IOException {

        if (args.length < 4) {
            System.out.println("Usage: java Type Main Runs,P ,N ,M");
            exit(1);
        }
        Runs = Integer.parseInt(args[0]);
        P = Integer.parseInt(args[1]);
        N = Integer.parseInt(args[2]);
        M = Integer.parseInt(args[3]);
    if(Runs==0) {
        MatrixGenerator matrixGenerator = new MatrixGenerator("matrix.txt", "filter.txt", N, M, 3);
        matrixGenerator.run();
    }
////        if (compareFiles("result.txt", "resultH.txt") && compareFiles("result.txt", "resultV.txt")) {
        EvaluateTime evaluateTime = new EvaluateTime(N, M, P, 3);
        evaluateTime.run(P);
//
////        }
//    }
    }
}