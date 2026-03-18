package com.pao.laboratory02.exercise1;

public class Rectangle extends Shape {

    private double width, height;

    public Rectangle(double width, double height) {
        super("Rectangle");
        this.width = width;
        this.height = height;
    }

    @Override 
    public double area() {
        return width * height;
    }

    @Override 
    public double perimeter() {
        return 2 * (width * height);
    }
}