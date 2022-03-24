package nstu.vehicles;

import javax.swing.*;

public abstract class Vehicle {
    public int x, y, id;
    public long timeAppear;
    public ImageIcon image;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public long getTimeAppear() {return timeAppear;}

    public void setTimeAppear(long timeAppear) {this.timeAppear = timeAppear;}

    public int getId() {return id;}

    public ImageIcon getImg() {
        return image;
    }
}
