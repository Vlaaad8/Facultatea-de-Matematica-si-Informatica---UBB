package org.example;

import java.util.Arrays;
import java.util.Random;

public class Main {
    public static final int N = 1000;
    public static final int P = 8;
    public static final int MAX_VAL = 100;
    public static final Random rnd = new Random();
    public static int[] a = new int[N];
    public static int[] b = new int[N];
    public static int[] c = new int[N];
    public static int[] c_secv = new int[N];

    public static void init(int[] v) {
        for (int i = 0; i < v.length; i++) {
            v[i] = rnd.nextInt(MAX_VAL);
        }
    }

    public static void print_arr(int[] v, int elems_to_print) {
        for (int i = 0; i < elems_to_print && i < v.length; i++) {
            System.out.printf("%d ", v[i]);

        }
        System.out.println();
    }

    public static void sum(int[] a, int[] b, int[] c) {
        if (a.length != b.length || a.length != c.length) {
            throw new RuntimeException("Length mismatch");
        }
        for (int i = 0; i < a.length; i++) {
//            c[i] = a[i] + b[i];
            c[i] = (int) (Math.pow(a[i], 2) + Math.pow(b[i], 2) - Math.pow(a[i], 2));
        }

    }

    public static void main(String[] args) throws InterruptedException {
        init(a);
        init(b);
        long start_time_sec = System.nanoTime();
        sum(a, b, c_secv);
        long end_time_sec = System.nanoTime();

        MyThread[] threads = new MyThread[P];

        long start_time_par = System.nanoTime();
        int dim_split = N / P;
        int r = N % P;


        int start_idx = 0;
        int end_idx = dim_split;
        for (int i = 0; i < P; i++) {
            if (r > 0) {
                end_idx++;
                r--;
            }
            threads[i] = new MyThread(a, b, c, start_idx, end_idx);

            threads[i].start();

            start_idx = end_idx;
            end_idx += dim_split;
        }
        for (MyThread thread : threads) {
            thread.join();
        }
        long end_time_par = System.nanoTime();
        if (Arrays.equals(c, c_secv)) {
            System.out.println("Echivalent");
        } else {
            System.out.println("Not Echivalent");
        }
        long sec_delta_time = end_time_sec - start_time_sec;
        double sec_delta_time_ms = sec_delta_time / 1e6;

        long par_delta_time = end_time_par - start_time_par;
        double par_delta_time_ms = par_delta_time / 1e6;

        System.out.printf("Secv time: %10.3f\n",sec_delta_time_ms);
        System.out.printf("Par time: %10.3f\n",par_delta_time_ms);
        print_arr(a, 3);
        print_arr(b, 3);
        print_arr(c_secv, 3);
        print_arr(c, 3);
    }
}