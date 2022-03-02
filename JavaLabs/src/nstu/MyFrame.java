package nstu;

import nstu.vehicles.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class MyFrame extends JFrame {
    boolean willShowStatsLabel = false;
    boolean willShowTime = false;
    boolean isStarted = false;
    Habitat h = new Habitat();
    long time;

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

        MyPanel road = new MyPanel();
        road.setLayout(new FlowLayout(FlowLayout.LEFT));
        road.setBackground(Color.LIGHT_GRAY);
        scene.add(road);

        JLabel timeLabel = new JLabel();
        timeLabel.setFont(new Font("JavaLabs/fonts/ttf/JetBrainsMono-Regular.ttf", Font.BOLD, 16));
        timeLabel.setForeground(Color.RED);
        timeLabel.setVisible(false);
        road.add(timeLabel);

        JLabel statsLabel = new JLabel();
        statsLabel.setFont(new Font("JavaLabs/fonts/ttf/JetBrainsMono-Regular.ttf", Font.ITALIC, 20));
        statsLabel.setForeground(Color.BLUE);
        statsLabel.setVisible(false);
        road.add(statsLabel);

        addKeyListener(new KeyAdapter() {
            Timer timer;

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case 'b' -> {
                        if (!isStarted) {
                            System.out.println("---------------------------");
                            System.out.println("Car: chance - " + h.P1 + "%, time - " + h.N1 +
                                            "\nBike: chance - " + h.P2 + "%, time - " + h.N2);
                            repaint();
                            if (willShowStatsLabel) {
                                statsLabel.setVisible(false);
                                willShowStatsLabel = false;
                            }
                            Habitat.vehicles.clear();
                            isStarted = true;
                            h.carCount = 0;
                            h.motoCount = 0;
                            timer = new Timer();
                            time = 0;
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    drawVehicles();
                                    h.update(time);
                                    timeLabel.setText("<html><p>Time: " + time + " s</html>");
                                    time++;
                                }
                            }, 0, 1000);
                        }
                    }
                    case 'e' -> {
                        if (isStarted) {
                            timer.cancel();
                            isStarted = false;
                        }
                        if (!willShowStatsLabel) {
                            if (willShowTime) {
                                timeLabel.setVisible(false);
                                willShowTime = false;
                            }
                            statsLabel.setText("<html>" +
                                    "<p>Simulation time: " + time + " s" +
                                    "<p>Total vehicles: " + Habitat.vehicles.size() +
                                    "<p>Cars number: " + h.carCount +
                                    "<p>Motorbikes number: " + h.motoCount +
                                    "</html>"
                            );
                            statsLabel.setVisible(true);
                            willShowStatsLabel = true;
                        }
                    }
                    case 't' -> {
                        if (!willShowStatsLabel) {
                            if (!willShowTime) {
                                timeLabel.setVisible(true);
                                willShowTime = true;
                            } else {
                                timeLabel.setVisible(false);
                                willShowTime = false;
                            }
                        }
                    }
                    default -> {}
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
                for (Vehicle v : Habitat.vehicles) {
                    g.drawImage(v.getImg().getImage(), v.getX(), v.getY(), null);
                }
            }
        });
    }
}
