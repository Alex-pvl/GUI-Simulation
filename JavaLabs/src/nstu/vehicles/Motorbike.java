package nstu.vehicles;

import nstu.Habitat;

import javax.swing.*;
import java.util.Random;

public class Motorbike extends Vehicle implements IBehaviour {
    private static long timeLifeMoto = 8;

    public Motorbike(int x, int y) {
        this.image = new ImageIcon("JavaLabs/src/nstu/imgs/moto.png");
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

    public static long getTimeLifeMoto() {
        return timeLifeMoto;
    }

    public static void setTimeLifeMoto(long timeLifeMoto) {
        Motorbike.timeLifeMoto = timeLifeMoto;
    }
}


