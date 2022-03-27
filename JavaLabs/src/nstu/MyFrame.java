package nstu;

import nstu.vehicles.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MyFrame extends JFrame {
	public Toolkit toolkit = Toolkit.getDefaultToolkit();
	public Dimension dimension = toolkit.getScreenSize();
	public boolean willShowTime = false;
	public boolean isStarted = false;
	public Habitat h = new Habitat();
	public long time;
	public Timer timer;
	public JPanel panel;
	public JLabel timeLabel;
	public JButton start;
	public JButton stop;
	public JCheckBox showInfo;
	public JRadioButton showTimer;
	public JRadioButton hideTimer;
	public JOptionPane dialog;
	public JButton submitCar;
	public JButton submitMoto;
	public JTextField carsFreqText;
	public JTextField motoFreqText;
	public JComboBox<String> carProbability;
	public JList<String> motoProbability;

	public JMenuItem startItem;
	public JMenuItem stopItem;
	public JMenuItem showInfoItem;
	public JMenuItem showTimerItem;
	public JMenuItem hideTimerItem;

	public JButton submitCarTime;
	public JButton submitMotoTime;
	public JTextField carsTimeText;
	public JTextField motoTimeText;
	public JButton currentVehicles;

	public CarAI carMoving;
	public MotorbikeAI motoMoving;
	public static boolean startCarMoving;
	public static boolean startMotoMoving;
	public JLabel carMovingPriority;
	public JLabel motoMovingPriority;
	public JComboBox<String> carPriority;
	public JComboBox<String> motoPriority;

	public void startSimulation() {
		if (!isStarted) {
			startCarMoving = false;
			startMotoMoving = false;
			carMoving.start();
			motoMoving.start();
			start.setEnabled(false);
			startItem.setEnabled(false);
			stop.setEnabled(true);
			stopItem.setEnabled(true);
			start.setContentAreaFilled(false);
			stop.setContentAreaFilled(true);
			System.out.println("---------------------------");
			System.out.println("Car: chance - " + h.P1 + "%, time - " + h.N1 +
							"\nBike: chance - " + h.P2 + "%, time - " + h.N2);
			repaint();

			isStarted = true;
			h.carCount = 0;
			h.motoCount = 0;
			timer = new Timer();
			time = 0;
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					h.update(time);
				  add(new JComponent() {
					});
					time++;
					timeLabel.setText("Время: " + time + " с");
					repaint();
				}
			}, 0, 1000);
		}
	}

	public void stopSimulation() {
		timer.cancel();
		dialog = new JOptionPane();
		JTextArea stats = new JTextArea(
						"Время симуляции: " + time + " c" +
										"\nВсего объектов: " + Habitat.vehicles.size() +
										"\nЧисло машин: " + h.carCount +
										"\nЧисло мотоциклов: " + h.motoCount
		);
		stats.setBackground(dialog.getBackground());
		stats.setEditable(false);
		stats.setFont(new Font("JetBrains Mono", Font.BOLD, 16));

		int n;
		if (showInfo.isSelected() || showInfoItem.isSelected()) {
			showInfoItem.setSelected(true);
			n = JOptionPane.showConfirmDialog(this, stats, "Информация", JOptionPane.OK_CANCEL_OPTION);
		} else {
			showInfoItem.setSelected(false);
			n = JOptionPane.showConfirmDialog(this, "Завершить симуляцию?", "Завершение...", JOptionPane.OK_CANCEL_OPTION);
		}

		if (n == JOptionPane.YES_OPTION) {
			showTimer.setSelected(false);
			showTimerItem.setSelected(false);
			hideTimer.setSelected(true);
			hideTimerItem.setSelected(true);
			if (isStarted) {
				stop.setEnabled(true);
				stopItem.setEnabled(true);
				timer.cancel();
				isStarted = false;
			}

			if (willShowTime) {
				timeLabel.setVisible(false);
				willShowTime = false;
			}
			timeLabel.setText("Время: 0 с");

			Habitat.vehicles.clear();
			start.setEnabled(true);
			startItem.setEnabled(true);
			stop.setEnabled(false);
			stopItem.setEnabled(false);
			start.setContentAreaFilled(true);
			stop.setContentAreaFilled(false);
			repaint();
		} else {
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					h.update(time);
					add(new JComponent() {
					});
					time++;
					timeLabel.setText("Время: " + time + " с");
					repaint();
				}
			}, 0, 1000);
		}

	}

	public void getTimer() {
		if (!willShowTime) {
			timeLabel.setVisible(true);
			willShowTime = true;
			showTimer.setSelected(true);
			showTimerItem.setSelected(true);
			hideTimer.setSelected(false);
			hideTimerItem.setSelected(false);
		} else {
			timeLabel.setVisible(false);
			willShowTime = false;
			hideTimer.setSelected(true);
			hideTimerItem.setSelected(true);
			showTimer.setSelected(false);
			showTimerItem.setSelected(false);
		}
	}


	public MyFrame() {
		super("Дорога");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(new ImageIcon("JavaLabs/src/nstu/imgs/icon.png").getImage());
		setBounds(dimension.width / 2 - Habitat.WIDTH / 2, dimension.height / 2 - Habitat.HEIGHT / 2,
						Habitat.WIDTH, Habitat.HEIGHT);
		setLayout(new BorderLayout());
		setResizable(false);

		carMoving = new CarAI();
		motoMoving = new MotorbikeAI();

		JPanel scene = new JPanel();
		scene.setLayout(new BorderLayout());
		add(scene);

		MyPanel road = new MyPanel();
		road.setBounds(0, 0, Habitat.WIDTH - 350, Habitat.HEIGHT);
		road.setLayout(new FlowLayout(FlowLayout.LEFT));
		road.setBackground(Color.WHITE);
		scene.add(road);

		timeLabel = new JLabel("Время: 0 с");
		timeLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
		timeLabel.setForeground(Color.RED);
		timeLabel.setVisible(false);
		road.add(timeLabel);


		panel = new JPanel();
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				requestFocusInWindow();
			}
		});
		panel.setPreferredSize(new Dimension(290, Habitat.HEIGHT));
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.setBackground(Color.LIGHT_GRAY);
		scene.add(panel, BorderLayout.EAST);

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyChar()) {
					case 'b' -> startSimulation();
					case 'e' -> stopSimulation();
					case 't' -> getTimer();
					default -> {
					}
				}
			}
		});

		start = new JButton("Старт");
		start.setBorderPainted(false);
		start.setBackground(new Color(0, 255, 119));
		start.setPreferredSize(new Dimension(138, 30));
		start.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
		start.addActionListener(e -> startSimulation());
		start.setFocusable(false);
		start.setContentAreaFilled(true);
		panel.add(start);

		stop = new JButton("Стоп");
		stop.setBorderPainted(false);
		stop.setBackground(new Color(222, 33, 33));
		stop.addActionListener(e -> stopSimulation());
		stop.setEnabled(false);
		stop.setPreferredSize(new Dimension(138, 30));
		stop.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
		stop.setFocusable(false);
		panel.add(stop);
		stop.setContentAreaFilled(false);

		showInfo = new JCheckBox("Показывать информацию");
		showInfo.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
		showInfo.setBackground(panel.getBackground());
		showInfo.addActionListener(e -> showInfoItem.setSelected(showInfo.isSelected()));
		showInfo.setFocusable(false);
		panel.add(showInfo);

		showTimer = new JRadioButton("Показывать время симуляции");
		showTimer.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
		showTimer.setBackground(panel.getBackground());
		showTimer.addActionListener(e -> getTimer());
		showTimer.setFocusable(false);
		panel.add(showTimer);

		hideTimer = new JRadioButton("Скрывать время симуляции");
		hideTimer.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
		hideTimer.setBackground(panel.getBackground());
		hideTimer.addActionListener(e -> getTimer());
		hideTimer.setFocusable(false);
		hideTimer.setSelected(true);
		panel.add(hideTimer);

		submitCar = new JButton("Ок");
		submitCar.setPreferredSize(new Dimension(50, 15));
		JLabel carsFreq = new JLabel("Частота появления машин");
		carsFreqText = new JTextField("" + h.N1, 2);
		carsFreq.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
		carsFreqText.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
		submitCar.addActionListener(e -> {
			try {
				h.N1 = Integer.parseInt(carsFreqText.getText());
				if (h.N1 <= 0) throw new Exception();
			} catch (Exception exp) {
				JOptionPane.showMessageDialog(this, "Введите целое положительое число!");
				h.N1 = 3;
				System.out.println("Поймано исключение " + exp.getMessage());
				carsFreqText.setText("" + h.N1);
			}
		});
		submitCar.setFocusable(false);
		panel.add(carsFreq);
		panel.add(carsFreqText);
		panel.add(submitCar);

		submitMoto = new JButton("Ок");
		submitMoto.setPreferredSize(new Dimension(50, 15));
		JLabel motoFreq = new JLabel("Частота появления мотоциклов");
		motoFreqText = new JTextField("" + h.N2, 2);
		motoFreq.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
		motoFreqText.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
		submitMoto.addActionListener(e -> {
			try {
				h.N2 = Integer.parseInt(motoFreqText.getText());
				if (h.N2 <= 0) throw new Exception();
			} catch (Exception exp) {
				JOptionPane.showMessageDialog(this, "Введите целое положительое число!");
				h.N2 = 4;
				System.out.println("Поймано исключение " + exp.getMessage());
				motoFreqText.setText("" + h.N2);
			}
		});
		submitMoto.setFocusable(false);
		panel.add(motoFreq);
		panel.add(motoFreqText);
		panel.add(submitMoto);

		String[] prob = {
						"0%",
						"10%",
						"20%",
						"30%",
						"40%",
						"50%",
						"60%",
						"70%",
						"80%",
						"90%",
						"100%",
		};

		JLabel carsP = new JLabel("Вероятность появления машин");
		carsP.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
		panel.add(carsP);
		carProbability = new JComboBox<>(prob);
		carProbability.setSelectedItem("" + h.P1 + "%");
		panel.add(carProbability);

		carProbability.addActionListener(e -> {
			if (e.getSource() == carProbability) {
				String prb = Objects.requireNonNull(carProbability.getSelectedItem()).toString().replace("%", "");
				h.P1 = Integer.parseInt(prb);
			}
		});
		carProbability.setFocusable(false);

		JLabel motoP = new JLabel("Вероятность появления мотоциклов");
		motoP.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
		panel.add(motoP);
		motoProbability = new JList<>(prob);
		motoProbability.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		motoProbability.setBackground(panel.getBackground());
		motoProbability.setVisibleRowCount(2);
		motoProbability.setSelectedIndex(7);
		motoProbability.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		motoProbability.addListSelectionListener(e -> {
			if (e.getSource() == motoProbability) {
				String prb = motoProbability.getSelectedValue().replace("%", "");
				h.P2 = Integer.parseInt(prb);
			}
		});
		motoProbability.setFocusable(false);
		panel.add(motoProbability);

		JMenuBar menu = new JMenuBar();

		JMenu simulationMenu = new JMenu("Симуляция");
		startItem = new JMenuItem("Старт");
		stopItem = new JMenuItem("Стоп");
		showInfoItem = new JCheckBoxMenuItem("Показывать информацию");
		startItem.addActionListener(e -> startSimulation());
		stopItem.addActionListener(e -> stopSimulation());
		stopItem.setEnabled(false);
		showInfoItem.addActionListener(e -> showInfo.setSelected(showInfoItem.isSelected()));
		simulationMenu.add(startItem);
		simulationMenu.add(stopItem);
		simulationMenu.add(showInfoItem);

		JMenu timerMenu = new JMenu("Таймер");
		showTimerItem = new JRadioButtonMenuItem("Показывать");
		hideTimerItem = new JRadioButtonMenuItem("Скрывать");
		showTimerItem.addActionListener(e -> getTimer());
		hideTimerItem.addActionListener(e -> getTimer());
		hideTimerItem.setSelected(true);
		timerMenu.add(showTimerItem);
		timerMenu.add(hideTimerItem);

		menu.add(simulationMenu);
		menu.add(timerMenu);

		setJMenuBar(menu);
		menu.setFocusable(false);

		submitCarTime = new JButton("Ок");
		submitCarTime.setPreferredSize(new Dimension(50, 15));
		JLabel carsTime = new JLabel("Время жизни машин");
		carsTimeText = new JTextField("" + Car.getTimeLifeCar(), 4);
		carsTime.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
		carsTimeText.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
		submitCarTime.addActionListener(e -> {
			try {
				Car.setTimeLifeCar(Integer.parseInt(carsTimeText.getText()));
				if (Car.getTimeLifeCar() <= 0) throw new Exception();
			} catch (Exception exp) {
				JOptionPane.showMessageDialog(this, "Введите целое положительое число!");
				Car.setTimeLifeCar(10);
				System.out.println("Поймано исключение " + exp.getMessage());
				carsTimeText.setText("" + Car.getTimeLifeCar());
			}
		});
		submitCarTime.setFocusable(false);
		panel.add(carsTime);
		panel.add(carsTimeText);
		panel.add(submitCarTime);

		submitMotoTime = new JButton("Ок");
		submitMotoTime.setPreferredSize(new Dimension(50, 15));
		JLabel motoTime = new JLabel("Время жизни мотоциклов");
		motoTimeText = new JTextField("" + Motorbike.getTimeLifeMoto(), 4);
		motoTime.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
		motoTimeText.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
		submitMotoTime.addActionListener(e -> {
			try {
				Motorbike.setTimeLifeMoto(Integer.parseInt(motoTimeText.getText()));
				if (Motorbike.getTimeLifeMoto() <= 0) throw new Exception();
			} catch (Exception exp) {
				JOptionPane.showMessageDialog(this, "Введите целое положительое число!");
				Motorbike.setTimeLifeMoto(8);
				System.out.println("Поймано исключение " + exp.getMessage());
				motoTimeText.setText("" + Motorbike.getTimeLifeMoto());
			}
		});
		submitMotoTime.setFocusable(false);
		panel.add(motoTime);
		panel.add(motoTimeText);
		panel.add(submitMotoTime);

		currentVehicles = new JButton("Текущие объекты");
		currentVehicles.setPreferredSize(new Dimension(280, 20));
		currentVehicles.setBackground(new Color(79, 153, 192));
		currentVehicles.addActionListener(e -> {
			StringBuilder currentObjects = new StringBuilder();
			for (int i = 0; i < Habitat.vehicles.size(); i++) {
				Vehicle v = Habitat.vehicles.get(i);
				if (v instanceof Car) {
					currentObjects.append(i + 1).
									append(". Машина\nВремя рождения: ").append(Habitat.times.get(v.getId())).
									append("\nУникальный идентификатор: ").append(v.getId()).append("\n\n");
				} else {
					currentObjects.append(i + 1).
									append(". Мотоцикл\nВремя рождения: ").append(Habitat.times.get(v.getId())).
									append("\nУникальный идентификатор: ").append(v.getId()).append("\n\n");
				}
			}
			if (currentObjects.length() == 0) {
				JOptionPane.showMessageDialog(this, "Пусто!");
			} else {
				TextArea stats = new TextArea(currentObjects.toString());
				stats.setEditable(false);
				stats.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
				JOptionPane.showMessageDialog(this, stats, "Список текущих объектов", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		currentVehicles.setFocusable(false);
		panel.add(currentVehicles);

		JButton carAI = new JButton("Движение машин");
		carAI.setFont(new Font("JetBrains Mono", Font.BOLD, 10));
		carAI.setPreferredSize(new Dimension(138, 30));
		carAI.addActionListener(e -> {
			synchronized (carMoving.carMonitor) {
				if (!startCarMoving) {
					startCarMoving = true;
					carMoving.carMonitor.notify();
				} else {
					startCarMoving = false;
				}
			}
		});
		carAI.setFocusable(false);
		panel.add(carAI);

		JButton motoAI = new JButton("Движение мотоциклов");
		motoAI.setFont(new Font("JetBrains Mono", Font.BOLD, 10));
		motoAI.setPreferredSize(new Dimension(138, 30));
		motoAI.addActionListener(e -> {
			synchronized (motoMoving.motoMonitor) {
				if (!startMotoMoving) {
					startMotoMoving = true;
					motoMoving.motoMonitor.notify();
				} else {
					startMotoMoving = false;
				}
			}
		});
		motoAI.setFocusable(false);
		panel.add(motoAI);

		String[] priorities = {
						"1",
						"2",
						"3",
						"4",
						"5",
						"6",
						"7",
						"8",
						"9",
						"10",
		};

		carMovingPriority = new JLabel("Приоритет машин");
		carMovingPriority.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
		panel.add(carMovingPriority);

		carPriority = new JComboBox<>(priorities);
		carPriority.setSelectedItem("" + carMoving.getPriority());
		panel.add(carPriority);

		carPriority.addActionListener(e -> {
			if (e.getSource() == carPriority) {
				String prb = (String) carPriority.getSelectedItem();
				carMoving.setPriority(Integer.parseInt(prb));
				System.out.println("Приоритет машин " + carMoving.getPriority());
			}

		});
		carPriority.setFocusable(false);


		motoMovingPriority = new JLabel("Приоритет мотоциклов");
		motoMovingPriority.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
		panel.add(motoMovingPriority);

		motoPriority = new JComboBox<>(priorities);
		motoPriority.setSelectedItem("" + motoMoving.getPriority());
		panel.add(motoPriority);

		motoPriority.addActionListener(e -> {
			if (e.getSource() == motoPriority) {
				String prb = motoPriority.getSelectedItem().toString();
				motoMoving.setPriority(Integer.parseInt(prb));
				System.out.println("Приоритет мотоциклов: " + motoMoving.getPriority());
			}
		});
		motoPriority.setFocusable(false);


		setVisible(true);
	}
}
