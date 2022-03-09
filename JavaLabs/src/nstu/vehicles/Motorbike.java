package nstu.vehicles;

import javax.swing.*;

public class Motorbike extends Vehicle implements IBehaviour {
    public Motorbike(int x, int y) {
        this.image = new ImageIcon("JavaLabs/src/nstu/imgs/moto.png");
        this.x = x;
        this.y = y;
    }
}


