package nstu.vehicles;

import nstu.Habitat;

import javax.swing.*;
import java.util.Random;

public class Car extends Vehicle implements IBehaviour {
    private static long timeLifeCar = 10;

    public Car(int x, int y) {
        this.image = new ImageIcon("JavaLabs/src/nstu/imgs/car.png");
        this.x = x;
        this.y = y;
        int id = new Random().nextInt(2000000000) - 1000000000;
        while (true) {
            if (Habitat.ids.contains(id)) {
                id = new Random().nextInt(2000000000) - 1000000000;
            } else {
                break;
            }
        }
        this.id = id;
        Habitat.ids.add(id);
    }

    public static long getTimeLifeCar() {
        return timeLifeCar;
    }

    public static void setTimeLifeCar(long timeLifeCar) {
        Car.timeLifeCar = timeLifeCar;
    }
}
