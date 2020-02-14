package com.modorone.juppeteer;

/**
 * author: Shawn
 * time  : 1/13/20 3:08 PM
 * desc  :
 * update: Shawn 1/13/20 3:08 PM
 */
public class ViewPort {

    private int width = 800;
    private int height = 600;

    public ViewPort() {
    }

    public ViewPort(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "ViewPort{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}
