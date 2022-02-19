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

        JPanel scene = new JPanel();
        add(scene);
        scene.setLayout(new BorderLayout());

        MyPanel panel = new MyPanel(h.WIDTH, h.HEIGHT);
        scene.add(panel);

        JLabel timeLabel = new JLabel();
        timeLabel.setText("Time: " + timeS + " s");
        timeLabel.setFont(new Font("JavaLabs/fonts/ttf/JetBrainsMono-Regular.ttf", Font.BOLD, 16));
        timeLabel.setForeground(Color.RED);
        timeLabel.setVisible(false);
        panel.add(timeLabel);


        JLabel stats = new JLabel();
        stats.setFont(new Font("JavaLabs/fonts/ttf/JetBrainsMono-Regular.ttf", Font.ITALIC, 20));
        stats.setForeground(Color.BLUE);
        stats.setLocation(0,0);
        stats.setVisible(false);
        panel.add(stats);

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
                drawVehicles();
                habitat.update(time);
                time++;
                timeLabel.setText("Time: " + timeS + " s");
            }
        }

        addKeyListener(new KeyAdapter() {
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

                            Habitat.vehicles.clear();
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

                            stats.setText("<html>Simulation time: " + timeS + " s" +
                                    "<br>Total vehicles: " + h.vehicles.size() +
                                    "<br>Cars number: " + h.carCount +
                                    "<br>Motorbikes number: " + h.motoCount);
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
                super.paintComponent(g);
                for (int i = 0; i < Habitat.vehicles.size(); i++) {
                    g.drawImage(Habitat.vehicles.get(i).getImage().getImage(), Habitat.vehicles.get(i).getX(),
                            Habitat.vehicles.get(i).getY(), null);
                }
            }
        });
    }
}