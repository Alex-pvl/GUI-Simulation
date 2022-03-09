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
    Timer timer;
    JLabel timeLabel;
    JLabel statsLabel;
    JButton start;
    JButton stop;
    JCheckBox showTimer;
    JCheckBox hideTimer;
//    JOptionPane stopOrContinue;

    public void startSimulation() {
        if (!isStarted) {
            start.setEnabled(false);
            stop.setEnabled(true);
            start.setContentAreaFilled(false);
            stop.setContentAreaFilled(true);
            System.out.println("---------------------------");
            System.out.println("Car: chance - " + h.P1 + "%, time - " + h.N1 +
                    "\nBike: chance - " + h.P2 + "%, time - " + h.N2);
            repaint();
            if (willShowStatsLabel) {
                statsLabel.setVisible(false);
                willShowStatsLabel = false;
            }

            isStarted = true;
            h.carCount = 0;
            h.motoCount = 0;
            timer = new Timer();
            time = 0;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    h.update(time);
                    add(new JComponent(){});
                    time++;
                    timeLabel.setText("Время: " + time + " с");
                    repaint();
                }
            }, 0, 1000);
        }
    }

    public void stopSimulation() {
        if (isStarted) {
            stop.setEnabled(true);
            timer.cancel();
            isStarted = false;
        }
        if (!willShowStatsLabel) {
            if (willShowTime) {
                timeLabel.setVisible(false);
                willShowTime = false;
            }
            statsLabel.setText("<html>" +
                    "<p>Время симуляции: " + time + " c" +
                    "<p>Всего объектов: " + Habitat.vehicles.size() +
                    "<p>Число машин: " + h.carCount +
                    "<p>Число мотоциклов: " + h.motoCount +
                    "</html>"
            );
            statsLabel.setVisible(true);
            willShowStatsLabel = true;
            Habitat.vehicles.clear();
            start.setEnabled(true);
            stop.setEnabled(false);
            start.setContentAreaFilled(true);
            stop.setContentAreaFilled(false);
        }
    }

    public void getTimer() {
        if (!willShowStatsLabel) {
            if (!willShowTime) {
                timeLabel.setVisible(true);
                willShowTime = true;
            } else {
                timeLabel.setVisible(false);
                willShowTime = false;
            }
        }
        showTimer.setSelected(willShowTime);
        hideTimer.setSelected(!willShowTime);
    }

    public MyFrame() {
        super("Road");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("JavaLabs/src/nstu/imgs/icon.png").getImage());
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setBounds(dimension.width/2 - h.WIDTH/2, dimension.height/2 - h.HEIGHT/2, h.WIDTH, h.HEIGHT);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel scene = new JPanel();
        scene.setLayout(new BorderLayout());
        add(scene);

        MyPanel road = new MyPanel();
        road.setBounds(0, 0, h.WIDTH-350, h.HEIGHT);
        road.setLayout(new FlowLayout(FlowLayout.LEFT));
        road.setBackground(Color.LIGHT_GRAY);
        scene.add(road);

        timeLabel = new JLabel("Время: 0 с");
        timeLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
        timeLabel.setForeground(Color.RED);
        timeLabel.setVisible(false);
        road.add(timeLabel);

        statsLabel = new JLabel();
        statsLabel.setFont(new Font("JetBrains Mono", Font.ITALIC, 20));
        statsLabel.setForeground(Color.BLUE);
        statsLabel.setVisible(false);
        road.add(statsLabel);

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(350, h.HEIGHT));
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.GRAY);
        scene.add(panel, BorderLayout.EAST);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case 'b' -> startSimulation();
                    case 'e' -> stopSimulation();
                    case 't' -> getTimer();
                    default -> {}
                }
            }
        });

        start = new JButton("Старт");
        start.setBorderPainted(false);
        start.setBackground(Color.green);
        start.setPreferredSize(new Dimension(100, 30));
        start.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
        start.addActionListener(e -> startSimulation());
        start.setFocusable(false);
        start.setContentAreaFilled(true);
        panel.add(start);

        stop = new JButton("Стоп");
        stop.setBorderPainted(false);
        stop.setBackground(Color.red);
        stop.addActionListener(e -> stopSimulation());
        stop.setEnabled(false);
        stop.setPreferredSize(new Dimension(100, 30));
        stop.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
        stop.setFocusable(false);
        panel.add(stop);
        stop.setContentAreaFilled(false);

        showTimer = new JCheckBox("Показывать время симуляции");
        showTimer.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
        showTimer.setBackground(panel.getBackground());
        showTimer.addActionListener(e -> getTimer());
        showTimer.setFocusable(false);
        panel.add(showTimer);

        hideTimer = new JCheckBox("Скрывать время симуляции");
        hideTimer.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
        hideTimer.setBackground(panel.getBackground());
        hideTimer.addActionListener(e -> getTimer());
        hideTimer.setFocusable(false);
        panel.add(hideTimer);

        //JOptionPane pane = new JOptionPane();
        //JOptionPane.showConfirmDialog(scene, "Завершить симуляцию?", "Завершение...", JOptionPane.OK_CANCEL_OPTION);












        setVisible(true);
    }
}
