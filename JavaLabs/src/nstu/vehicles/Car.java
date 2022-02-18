package nstu.vehicles;

import javax.swing.*;

public class Car extends Vehicle implements IBehaviour {
    public Car(int x, int y) {
        this.image = new ImageIcon("JavaLabs/src/nstu/imgs/car.png");
        this.x = x;
        this.y = y;
    }
}
