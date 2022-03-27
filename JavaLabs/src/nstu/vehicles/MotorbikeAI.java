package nstu.vehicles;

import nstu.BaseAI;
import nstu.MyFrame;

import static nstu.Habitat.vehicles;

public class MotorbikeAI extends BaseAI {
	public final Object motoMonitor;

	public MotorbikeAI() {
		setPriority(1);
		motoMonitor = new Object();
	}

	@Override
	public void run() {
		while (isMoving) {
			synchronized (motoMonitor) {
				if (!MyFrame.startMotoMoving) {
					try {
						wait();
					} catch (InterruptedException e) {
						System.out.println("Поток прерван: " + e.getMessage());
					}
				}

				for (Vehicle v : vehicles) {
					if (v instanceof Motorbike) {
						((Motorbike) v).move();
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
