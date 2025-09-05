package org.example;

import java.util.Random;

public class MyThread3 implements  Runnable {
    @Override
    public void run() {
        Random rand = new Random();
        int number=rand.nextInt(8);
        try {
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(number);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
