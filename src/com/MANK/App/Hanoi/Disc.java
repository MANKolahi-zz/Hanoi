package com.MANK.App.Hanoi;


import javafx.scene.shape.Rectangle;

public class Disc {
    private int X;
    private int Y;
    private final int width;
    private final int height;

    public Disc(int x, int y, int width, int height) {
        this.X = x;
        this.Y = y;
        this.height = height;
        this.width = width;
    }

    public Disc(Rectangle disc){
        X = (int) disc.getX();
        Y = (int) disc.getY();
        height = (int) disc.getHeight();
        width = (int) disc.getWidth();
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
