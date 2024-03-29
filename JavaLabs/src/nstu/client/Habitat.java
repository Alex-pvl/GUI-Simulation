package nstu.client;

import nstu.client.vehicles.*;
import java.io.Serializable;
import java.util.*;

public class Habitat implements Serializable {
	private final static long serializedUID = 1;
	public final int WIDTH = 1200;
	public final int HEIGHT = 750;
	public static int N1 = 3, N2 = 4;
	public static int P1 = 60, P2 = 70;
	public static float speed = 3.5f;
	public int carCount = 0;
	public int motoCount = 0;
	public Random random = new Random();

	public static List<Vehicle> vehicles = new ArrayList<>();
	public static Set<Integer> ids = new HashSet<>();
	public static Map<Integer, Long> times = new TreeMap<>();

	public void update(long time) {
		for (int i = 0; i < vehicles.size(); i++) {
			Vehicle v = vehicles.get(i);
			if ((v instanceof Car) &&
							(time - v.getTimeAppear() >= Car.getTimeLifeCar())) {
				vehicles.remove(v);
				ids.remove(v.getId());
				times.remove(v.getId());
				carCount--;
				i--;
				System.out.println("-car{" + v.getX() + "; " + v.getY() + "; " + v.getId() + "}");
			} else if ((v instanceof Motorbike) &&
							(time - v.getTimeAppear() >= Motorbike.getTimeLifeMoto())) {
				vehicles.remove(i);
				ids.remove(i);
				times.remove(i);
				motoCount--;
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

	public static List<Vehicle> getVehicles() {
		return new ArrayList<>(vehicles);
	}
}