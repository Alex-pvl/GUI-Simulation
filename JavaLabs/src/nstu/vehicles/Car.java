package nstu.vehicles;

import nstu.Habitat;

import javax.swing.*;
import java.util.Random;
import static nstu.Habitat.*;

public class Car extends Vehicle implements IBehaviour {
	private static long timeLifeCar = 20;
	private boolean rightBorder = false;
	private boolean leftBorder = false;

	public Car(int x, int y) {
		this.setImage(new ImageIcon("JavaLabs/src/nstu/imgs/car.png"));
		this.setX(x);
		this.setY(y);
		int id = new Random().nextInt(2000000000) - 1000000000;
		while (true) {
			if (Habitat.ids.contains(id)) {
				id = new Random().nextInt(2000000000) - 1000000000;
			} else {
				break;
			}
		}
		this.setId(id);
		Habitat.ids.add(id);
	}

	public static long getTimeLifeCar() {
		return timeLifeCar;
	}

	public static void setTimeLifeCar(long timeLifeCar) {
		Car.timeLifeCar = timeLifeCar;
	}

	@Override
	public void move() {
		if (!rightBorder && !leftBorder) {
			if (this.x <= WIDTH - 440) {
				this.x += speed;
			} else {
				rightBorder = true;
			}
		}
		if (rightBorder && !leftBorder) {
			if (this.x >= 0) {
				this.x -= speed;
			} else {
				leftBorder = true;
			}
		}
		if (rightBorder && leftBorder) {
			rightBorder = false;
			leftBorder = false;
		}
	}
}
