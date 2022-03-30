package nstu;

import nstu.vehicles.Vehicle;

import javax.swing.*;
import java.awt.*;

class MyPanel extends JPanel {
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < Habitat.vehicles.size(); i++) {
			Vehicle v = Habitat.vehicles.get(i);
			v.setSpeed(Habitat.speed);
			g.drawImage(v.getImg().getImage(), (int) v.getX(), (int) v.getY(), null);
		}
	}

}
