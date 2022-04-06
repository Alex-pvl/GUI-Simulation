package nstu.client;

import nstu.client.vehicles.Vehicle;

import javax.swing.*;
import java.awt.*;
import static nstu.client.Habitat.*;

class MyPanel extends JPanel {
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < vehicles.size(); i++) {
			Vehicle v = vehicles.get(i);
			v.setSpeed(speed);
			g.drawImage(v.getImg().getImage(), (int) v.getX(), (int) v.getY(), null);
		}
	}

}
