package com.vidor;

public class Math {
    public static final int initData = 666;
    public static User user = new User();

    public static void main(String[] args) {
        Math math = new Math();
        math.compute();
    }

    private int compute() {
        int a = 1;
        int b = 2;
        int c = (a + b) * 10;
        return c;
    }
}
