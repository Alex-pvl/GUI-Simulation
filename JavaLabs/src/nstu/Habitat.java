package nstu;

import nstu.vehicles.Car;
import nstu.vehicles.Motorbike;
import nstu.vehicles.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Habitat {
    final int WIDTH = 1200;
    final int HEIGHT = 750;
    final int N1 = 3, N2 = 4;
    final int P1 = 70, P2 = 60;
    int carCount = 0;
    int motoCount = 0;
    Random random = new Random();

    static List<Vehicle> vehicles = new ArrayList<>();

    public void update(long time) {
        if (random.nextInt(100) < P1 && time % N1 == 0) {
            Car car = new Car(random.nextInt(WIDTH-150), random.nextInt(HEIGHT-98));
            vehicles.add(car);
            System.out.println("+car{" + car.getX() + "; " + car.getY() + "}");
            carCount++;
        }
        if (random.nextInt(100) < P2 && time % N2 == 0) {
            Motorbike motorbike = new Motorbike(random.nextInt(WIDTH-100), random.nextInt(HEIGHT-114));
            vehicles.add(motorbike);
            System.out.println("+motorbike{" + motorbike.getX() + "; " + motorbike.getY() + "}");
            motoCount++;
        }
    }
}