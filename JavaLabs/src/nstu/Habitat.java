package nstu;

import nstu.vehicles.Car;
import nstu.vehicles.Motorbike;
import nstu.vehicles.Vehicle;

import java.util.*;

public class Habitat {
	public final static int WIDTH = 1200;
	public final static int HEIGHT = 750;
	public int N1 = 3, N2 = 4;
	public int P1 = 60, P2 = 70;
	public int speed = 3;
	public int carCount = 0;
	public int motoCount = 0;
	public Random random = new Random();

	public final static List<Vehicle> vehicles = new ArrayList<>();
	public static Set<Integer> ids = new HashSet<>();
	public final static Map<Integer, Long> times = new TreeMap<>();

	public void update(long time) {
		for (int i = 0; i < vehicles.size(); i++) {
			Vehicle v = vehicles.get(i);
			if ((v instanceof Car) &&
							(time - v.getTimeAppear() >= Car.getTimeLifeCar())) {
				vehicles.remove(v);
				ids.remove(v.getId());
				times.remove(v.getId());
				i--;
				System.out.println("-car{" + v.getX() + "; " + v.getY() + "; " + v.getId() + "}");
			} else if ((v instanceof Motorbike) &&
							(time - v.getTimeAppear() >= Motorbike.getTimeLifeMoto())) {
				vehicles.remove(i);
				ids.remove(i);
				times.remove(i);
				i--;
				System.out.println("-motorbike{" + v.getX() + "; " + v.getY() + "; " + v.getId() + "}");
			}
		}

		if (random.nextInt(100) < P1 && (int) time % N1 == 0) {
			Car car = new Car(random.nextInt(WIDTH - 440), random.nextInt(HEIGHT - 128));
			car.setTimeAppear(time);
			car.setSpeed(speed);
			vehicles.add(car);
			times.put(car.getId(), time);
			System.out.println("+car{" + car.getX() + "; " + car.getY() + "; " + car.getId() + "}");
			carCount++;
		}
		if (random.nextInt(100) < P2 && (int) time % N2 == 0) {
			Motorbike motorbike = new Motorbike(random.nextInt(WIDTH - 390), random.nextInt(HEIGHT - 144));
			motorbike.setTimeAppear(time);
			motorbike.setSpeed(speed);
			vehicles.add(motorbike);
			times.put(motorbike.getId(), time);
			System.out.println("+motorbike{" + motorbike.getX() + "; " + motorbike.getY() + "; " + motorbike.getId() + "}");
			motoCount++;
		}

	}
}