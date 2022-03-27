package nstu.vehicles;

import nstu.BaseAI;
import nstu.MyFrame;

import static nstu.Habitat.*;

public class CarAI extends BaseAI {
	public final Object carMonitor;

	public CarAI() {
		setPriority(1);
		carMonitor = new Object();
	}

	@Override
	public void run() {
		while (isMoving) {
			synchronized (carMonitor) {
				if (!MyFrame.startCarMoving) {
					try {
						wait();
					} catch (InterruptedException e) {
						System.out.println("Поток прерван: " + e.getMessage());
					}
				}

				for (Vehicle v : vehicles) {
					if (v instanceof Car) {
						((Car) v).move();
					}
				}
			}
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				System.out.println("Поток прерван: " + e.getMessage());
			}
		}
	}
}
