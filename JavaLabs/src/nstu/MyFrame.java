package nstu;

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

        MyPanel panel = new MyPanel();
        panel.setPreferredSize(new Dimension(h.WIDTH, h.HEIGHT));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        scene.add(panel);

        JLabel timeLabel = new JLabel();
        timeLabel.setText("Time: " + 0 + " s");
        timeLabel.setFont(new Font("JavaLabs/fonts/ttf/JetBrainsMono-Regular.ttf", Font.BOLD, 16));
        timeLabel.setForeground(Color.RED);
        timeLabel.setVisible(false);
        panel.add(timeLabel);

        JLabel statsLabel = new JLabel();
        statsLabel.setFont(new Font("JavaLabs/fonts/ttf/JetBrainsMono-Regular.ttf", Font.ITALIC, 20));
        statsLabel.setForeground(Color.BLUE);
        statsLabel.setVisible(false);
        panel.add(statsLabel);

        class MyTimerTask extends TimerTask {
            @Override
            public void run() {
                drawVehicles();
                h.update(time);
                time++;
                timeLabel.setText("<html><p>Time: " + time + " s</html>");
            }
        }

        addKeyListener(new KeyAdapter() {
            Timer timer;
            MyTimerTask task = null;

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case 'B' -> {
                        if (!isStarted) {
                            System.out.println("---------------------------");
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
                            task = new MyTimerTask();
                            timer.schedule(task, 0, 1000);
                        }
                    }
                    case 'E' -> {
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
                                    "<p>Motorbikes number: " + h.motoCount
                                    + "</html>");
                            statsLabel.setVisible(true);
                            repaint();
                            willShowStatsLabel = true;
                        }
                    }
                    case 'T' -> {
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
                for (int i = 0; i < Habitat.vehicles.size(); i++) {
                    g.drawImage(Habitat.vehicles.get(i).getImage().getImage(), Habitat.vehicles.get(i).getX(),
                            Habitat.vehicles.get(i).getY(), null);
                }
            }
        });
    }
}