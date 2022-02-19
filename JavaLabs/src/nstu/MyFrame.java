package nstu;

import nstu.vehicles.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class MyFrame extends JFrame {
    boolean willShowTime = false;
    Habitat h = new Habitat();
    Timer timer = new Timer();
    long time = 0;

    public MyFrame() {
        super("Road");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("JavaLabs/src/nstu/imgs/icon.png").getImage());
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setBounds(dimension.width/2 - h.WIDTH/2, dimension.height/2 - h.HEIGHT/2, h.WIDTH, h.HEIGHT);
        timer.schedule(new MyTimerTask(), 100 ,1000);
        setVisible(true);
    }

    // параметр для таймера
    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            System.out.println("time = " + time);
            drawVehicles();
            h.update(time);
            showTimer();
            repaint();
            setVisible(true);
            time++;
            repaint();
        }
    }

    public void showTimer() {
        add(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                String timeS = "Time: " + (time-1) + " s";
                Font font = new Font("JavaLabs/fonts/ttf/JetBrainsMono-Regular.ttf", Font.BOLD, 15);
                g.setFont(font);
                g.setColor(Color.BLACK);
                g.drawString(timeS, 2, 15);
                setVisible(true);
            }
        });
    }

    public void drawVehicles() {
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