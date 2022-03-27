package nstu.vehicles;

import nstu.BaseAI;
import nstu.MyFrame;

import static nstu.Habitat.*;

public class CarAI extends BaseAI {
	public final Object carMonitor;

	public CarAI() {
		carMonitor = new Object();
		setPriority(1);
	}

	@Override
	public void run() {
		while (isMoving) {
			synchronized (carMonitor) {
				if (!MyFrame.startCarMoving) {
					try {
						carMonitor.wait();
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
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println("Поток прерван: " + e.getMessage());
			}
		}
	}
}
