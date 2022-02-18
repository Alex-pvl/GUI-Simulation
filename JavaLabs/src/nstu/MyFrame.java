package nstu;

import nstu.vehicles.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class MyFrame extends JFrame {
    Habitat h = new Habitat();
    Timer timer = new Timer();
    long time = 0;

    public MyFrame() {
        super("Road");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("JavaLabs/src/nstu/imgs/icon.png").getImage());
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setFocusable(true);
        setBounds(dimension.width/2 - h.WIDTH/2, dimension.height/2 - h.HEIGHT/2, h.WIDTH, h.HEIGHT);
        class MyTimerTask extends TimerTask {
            @Override
            public void run() {
                System.out.println("time = " + time);
                h.update(time);
                start();
                repaint();
                time++;
            }
        }
        timer.schedule(new MyTimerTask(), 0 ,1000);
        setVisible(true);
    }

    public void start() {
        add(new JComponent() {
            @Override
            protected void paintComponent(Graphics g){
                for (Vehicle v : h.vehicles) {
                    g.drawImage(v.getImage().getImage(), v.getX(), v.getY(), null);
                }
            }
        });
    }
}
