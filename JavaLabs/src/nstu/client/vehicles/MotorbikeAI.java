package nstu.client.vehicles;

import nstu.client.BaseAI;
import nstu.client.MyFrame;

import static nstu.client.Habitat.vehicles;

public class MotorbikeAI extends BaseAI {
	public final Object motoMonitor;

	public MotorbikeAI() {
		motoMonitor = new Object();
		setPriority(1);
	}

	@Override
	public void run() {
		while (isMoving) {
			synchronized (motoMonitor) {
				if (!MyFrame.startMotoMoving) {
					try {
						motoMonitor.wait();
					} catch (InterruptedException e) {
						System.out.println("Поток прерван: " + e.getMessage());
					}
				}

				for (int i = 0; i < vehicles.size(); i++) {
				  Vehicle v = vehicles.get(i);
					if (v instanceof Motorbike) {
						((Motorbike) v).move();
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
