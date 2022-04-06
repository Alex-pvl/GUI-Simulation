package nstu.client.vehicles;

import nstu.client.BaseAI;
import nstu.client.MyFrame;

import static nstu.client.Habitat.*;

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

				for (int i = 0; i < vehicles.size(); i++) {
					Vehicle v = vehicles.get(i);
					if (v instanceof Car) {
						((Car) v).move();
					}
				}

			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.out.println("Поток прерван: " + e.getMessage());
			}
		}
	}
}
