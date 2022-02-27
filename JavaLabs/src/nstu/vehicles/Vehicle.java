package nstu.vehicles;

import javax.swing.*;

public abstract class Vehicle {
    int x, y;
    ImageIcon image;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public ImageIcon getImg() {
        return image;
    }
}
