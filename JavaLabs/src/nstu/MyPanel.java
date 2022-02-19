package nstu;

import javax.swing.*;
import java.awt.*;

class MyPanel extends JPanel {
    MyPanel(int x, int y){
        setPreferredSize(new Dimension(x, y));
        setLayout(new FlowLayout(FlowLayout.LEFT));
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        for (int i = 0; i < Habitat.vehicles.size(); i++) {
            g.drawImage(Habitat.vehicles.get(i).getImage().getImage(), Habitat.vehicles.get(i).getX(),
                    Habitat.vehicles.get(i).getY(), null);
        }
    }
}
