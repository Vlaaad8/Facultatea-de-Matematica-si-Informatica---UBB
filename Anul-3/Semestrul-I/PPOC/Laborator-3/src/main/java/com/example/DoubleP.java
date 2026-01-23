package com.example;

import java.util.*;

import java.util.Arrays;

    public class DoubleP {
        private final double[] list;

        public DoubleP(double[] list) {
            this.list = list;
        }

        public double sum() {
            double s = 0;
            for (double d : list) s += d;
            return s;
        }

        public double average() {
            return sum() / list.length;
        }

        public double[] top() {
            int limit = list.length / 10;
            double[] copy = Arrays.copyOf(list, list.length);
            Arrays.sort(copy);

            double[] top = new double[limit];
            for (int i = 0; i < limit; i++) {
                top[i] = copy[copy.length - 1 - i];
            }
            return top;
        }
    }
