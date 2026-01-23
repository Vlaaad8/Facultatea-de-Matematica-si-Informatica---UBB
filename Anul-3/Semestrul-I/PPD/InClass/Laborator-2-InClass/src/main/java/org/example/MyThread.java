package org.example;

public class MyThread extends Thread {
    private int[] a;
    private int[] b;
    private int[] c;
    private int start_idx;
    private int end_idx;

    public MyThread(int[] a, int[] b, int[] c, int start_idx, int end_idx) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.start_idx = start_idx;
        this.end_idx = end_idx;
    }

    @Override
    public void run() {
            for(int i=start_idx;i<end_idx;i++){
                c[i]=a[i]+b[i];
            }
    }
}
