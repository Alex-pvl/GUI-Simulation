package nstu;

import nstu.vehicles.Vehicle;

import javax.swing.*;
import java.awt.*;

class MyPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        for (Vehicle v : Habitat.vehicles) {
            g.drawImage(v.getImage().getImage(), v.getX(), v.getY(), null);
        }
    }
}
