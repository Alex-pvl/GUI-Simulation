package nstu;

import nstu.vehicles.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

public class MyFrame extends JFrame {
    boolean willShowStats = false;
    boolean willShowTime = false;
    boolean isStarted = false;
    Habitat h = new Habitat();
    AtomicLong timeS = new AtomicLong(0);

    public MyFrame() {
        super("Road");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("JavaLabs/src/nstu/imgs/icon.png").getImage());
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setBounds(dimension.width/2 - h.WIDTH/2, dimension.height/2 - h.HEIGHT/2, h.WIDTH, h.HEIGHT);
        setFocusable(true);

        JLabel timeLabel = new JLabel();
        timeLabel.setText("Time: " + timeS + " s");
        timeLabel.setFont(new Font("JavaLabs/fonts/ttf/JetBrainsMono-Regular.ttf", Font.BOLD, 15));
        timeLabel.setVerticalAlignment(JLabel.TOP);
        timeLabel.setHorizontalAlignment(JLabel.CENTER);
        timeLabel.setPreferredSize(new Dimension(10, 10));
        timeLabel.setBackground(Color.BLACK);
        timeLabel.setVisible(false);
        add(timeLabel);

        JLabel stats = new JLabel();
        stats.setFont(new Font("JavaLabs/fonts/ttf/JetBrainsMono-Regular.ttf", Font.ITALIC, 20));
        stats.setVerticalAlignment(JLabel.CENTER);
        stats.setHorizontalAlignment(JLabel.CENTER);
        stats.setPreferredSize(new Dimension(100, 100));
        stats.setBackground(Color.RED);
        add(stats);

        class MyTimerTask extends TimerTask {
            private Habitat habitat;
            private long time;

            public MyTimerTask(Habitat habitat, long time) {
                this.habitat = habitat;
                this.time = time;
            }

            @Override
            public void run() {
                timeS.set(timeS.get()+1);
                System.out.println("time = " + time);
                habitat.update(time);
                time++;
                drawVehicles();
                timeLabel.setText("Time: " + timeS + " s");
            }
        }




        this.addKeyListener(new KeyAdapter() {
            Timer timer;
            long time;
            MyTimerTask task = null;

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case 'b':
                        if (!isStarted) {
                            repaint();
                            if (willShowStats) {
                                stats.setVisible(false);
                                willShowStats = false;
                            }

                            //h.vehicles.clear();
                            isStarted = true;
                            h.carCount = 0;
                            h.motoCount = 0;
                            timer = new Timer();
                            time = 0;
                            timeS.set(0);
                            task = new MyTimerTask(h, time);
                            timer.schedule(task, 0, 1000);
                        }
                        break;
                    case 'e':
                        if (isStarted) {
                            timer.cancel();
                            isStarted = false;
                        }
                        if(!willShowStats) {
                            if (willShowTime) {
                                timeLabel.setVisible(false);
                                willShowTime = false;
                            }

                            stats.setText("<html><br>Simulation time: " + timeS + " s</br>" +
                                    "<br>Total vehicles: " + h.vehicles.size() + "</br>" +
                                    "<br>Cars number: " + h.carCount + "</br>" +
                                    "<br>Motorbikes number: " + h.motoCount + "</br></html>");
                            stats.setVisible(true);
                            repaint();
                            willShowStats = true;
                        }
                        break;
                    case 't':
                        if (!willShowStats) {
                            if (!willShowTime) {
                                timeLabel.setVisible(true);
                                willShowTime = true;
                            } else {
                                timeLabel.setVisible(false);
                                willShowTime = false;
                            }
                        }
                        break;
                    default: break;
                }
            }
        });

        setVisible(true);
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