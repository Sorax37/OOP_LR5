package com.company;

import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator{

    public static final int MAX_ITERATIONS = 2000;

    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = - 1.5;
        range.width = 3;
        range.height = 3;
    }

    public int numIterations(double x, double y) {
        int iteration = 0;
        double Re = 0;
        double Im = 0;
        while (iteration < MAX_ITERATIONS && Re * Re  + Im * Im < 4)
        {
            double NewRe = Re * Re - Im * Im + x;
            double NewIm = 2 * Re * Im + y;
            Re = NewRe;
            Im = NewIm;
            iteration++;
        }
        if (iteration == MAX_ITERATIONS)
        {
            return -1;
        }
        return iteration;
    }

    public String toString() {
        return "Mandelbrot";
    }
}
