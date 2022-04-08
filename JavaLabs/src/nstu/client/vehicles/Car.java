package nstu.client.vehicles;

import nstu.client.Habitat;

import javax.swing.*;
import java.util.Random;

public class Car extends Vehicle implements IBehaviour {
	private static long timeLifeCar = 20;
	private boolean rightBorder = false;
	private boolean leftBorder = false;

	public Car(int x, int y) {
		this.setImage(new ImageIcon("C:\\Users\\Александр\\Java\\Labs-4-sem\\JavaLabs\\src\\nstu\\client\\imgs\\car.png"));
		this.setX(x);
		this.setY(y);
		int id = new Random().nextInt(1000);
		while (true) {
			if (Habitat.ids.contains(id)) {
				id = new Random().nextInt(1000);
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
			if (this.x <= 1200 - 440) {
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
