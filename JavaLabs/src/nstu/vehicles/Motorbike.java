package nstu.vehicles;

import nstu.Habitat;

import javax.swing.*;
import java.util.Random;

public class Motorbike extends Vehicle implements IBehaviour {
	private static long timeLifeMoto = 18;
	private boolean topBorder = false;
	private boolean bottomBorder = false;

	public Motorbike(int x, int y) {
		this.setImage(new ImageIcon("JavaLabs/src/nstu/imgs/moto.png"));
		this.setX(x);
		this.setY(y);
		int id = new Random().nextInt(2000000000);
		while (true) {
			if (Habitat.ids.contains(id)) {
				id = new Random().nextInt(2000000000);
			} else {
				break;
			}
		}
		this.setId(id);
		Habitat.ids.add(id);
	}

	public static long getTimeLifeMoto() {
		return timeLifeMoto;
	}

	public static void setTimeLifeMoto(long timeLifeMoto) {
		Motorbike.timeLifeMoto = timeLifeMoto;
	}

	@Override
	public void move() {
		if (!topBorder && !bottomBorder) {
			if (this.y >= 0) {
				this.y -= speed;
			} else {
				topBorder = true;
			}
		}
		if (topBorder && !bottomBorder) {
			if (this.y <= 750 - 135) {
				this.y += speed;
			} else {
				bottomBorder = true;
			}
		}
		if (topBorder && bottomBorder) {
			topBorder = false;
			bottomBorder = false;
		}
	}
}


