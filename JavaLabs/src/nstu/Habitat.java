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
    int N1 = 3, N2 = 4;
    int P1 = 60, P2 = 70;
    int carCount = 0;
    int motoCount = 0;
    Random random = new Random();

    final static List<Vehicle> vehicles = new ArrayList<>();

    public void update(double time) {
        if (random.nextInt(100) < P1 && (int) time % N1 == 0) {
            Car car = new Car(random.nextInt(WIDTH-440), random.nextInt(HEIGHT-128));
            vehicles.add(car);
            System.out.println("+car{" + car.getX() + "; " + car.getY() + "}");
            carCount++;
        }
        if (random.nextInt(100) < P2 && (int) time % N2 == 0) {
            Motorbike motorbike = new Motorbike(random.nextInt(WIDTH-390), random.nextInt(HEIGHT-144));
            vehicles.add(motorbike);
            System.out.println("+motorbike{" + motorbike.getX() + "; " + motorbike.getY() + "}");
            motoCount++;
        }
    }
}