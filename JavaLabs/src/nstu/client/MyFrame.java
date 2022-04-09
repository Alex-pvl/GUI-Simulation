package nstu.client;

import nstu.client.vehicles.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

import static nstu.client.Habitat.*;

public class MyFrame extends JFrame {
	public Toolkit toolkit = Toolkit.getDefaultToolkit();
	public Dimension dimension = toolkit.getScreenSize();
	public boolean willShowTime = false;
	public boolean isStarted = false;
	public Habitat h = new Habitat();
	public static long time;
	public Timer timer;
	public Timer timerRepaint;
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

	public JMenuItem saveSimilation;
	public JMenuItem loadSimilation;
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

	public JButton carAI;
	public JButton motoAI;
	public CarAI carMoving;
	public MotorbikeAI motoMoving;
	public static boolean startCarMoving;
	public static boolean startMotoMoving;
	public JLabel carMovingPriority;
	public JLabel motoMovingPriority;
	public JComboBox<String> carPriority;
	public JComboBox<String> motoPriority;
	public JSlider speedSlider;

	public JButton console;
	public JDialog consoleDialog;

	public JMenuItem networkItem;
	public NDialog networkDialog;

	public static final String DB_NAME = "vehicles.db";
	public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\Александр\\Java\\Labs-4-sem\\JavaLabs\\src\\" + DB_NAME;
	public static final String VEHICLES_TABLE = "vehicles";
	public static final String CREATE_VEHICLES_TABLE = "CREATE TABLE IF NOT EXISTS " + VEHICLES_TABLE +
					"(id INTEGER, type TEXT, x INTEGER, y INTEGER, timeAppear INTEGER)";
  public Connection con;
	public Statement statement;
	public ResultSet resultSet;
	public JMenu saveToDB;
	public JMenuItem saveToDBAllItem;
	public JMenuItem saveToDBOnlyCarsItem;
	public JMenuItem saveToDBOnlyMotosItem;
	public JMenu loadFromDB;
	public JMenuItem loadFromDBAllItem;
	public JMenuItem loadFromDBOnlyCarsItem;
	public JMenuItem loadFromDBOnlyMotosItem;

	public void dropAndCreate() {
		try {
			con = DriverManager.getConnection(CONNECTION_STRING);
			statement = con.createStatement();
			statement.execute("DROP TABLE IF EXISTS " + VEHICLES_TABLE);
			statement.execute(CREATE_VEHICLES_TABLE);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}


	public void startSimulation() {
		if (!isStarted) {
			startCarMoving = false;
			startMotoMoving = false;
			carAI.setEnabled(true);
			motoAI.setEnabled(true);
			carMoving.start();
			motoMoving.start();
			loadSimilation.setEnabled(true);
			start.setEnabled(false);
			startItem.setEnabled(false);
			stop.setEnabled(true);
			stopItem.setEnabled(true);
			start.setContentAreaFilled(false);
			stop.setContentAreaFilled(true);
			console.setEnabled(true);
			System.out.println("---------------------------");
			repaint();

			isStarted = true;
			h.carCount = 0;
			h.motoCount = 0;
			timer = new Timer();
			time = 0;
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					time += 1;
					h.update(time);
					timeLabel.setText("Время: " + time + " с");
				}
			}, 0, 1000);
			timerRepaint = new Timer();
			timerRepaint.schedule(new TimerTask() {
				@Override
				public void run() {
					repaint();
					add(new JComponent() {
					});
				}
			}, 0, 10);
		}
	}

	public void stopSimulation() {
		timer.cancel();
		dialog = new JOptionPane();
		JTextArea stats = new JTextArea(
						"Время симуляции: " + time + " c" +
										"\nВсего объектов: " + (h.carCount + h.motoCount) +
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
			loadSimilation.setEnabled(false);
			showTimer.setSelected(false);
			showTimerItem.setSelected(false);
			hideTimer.setSelected(true);
			hideTimerItem.setSelected(true);
			carAI.setEnabled(false);
			motoAI.setEnabled(false);
			carMoving = new CarAI();
			motoMoving = new MotorbikeAI();
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

			vehicles.clear();
			start.setEnabled(true);
			startItem.setEnabled(true);
			stop.setEnabled(false);
			stopItem.setEnabled(false);
			start.setContentAreaFilled(true);
			stop.setContentAreaFilled(false);
			console.setEnabled(false);
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
		setIconImage(new ImageIcon("C:\\Users\\Александр\\Java\\Labs-4-sem\\JavaLabs\\src\\nstu\\client\\imgs\\icon.png").getImage());
		setBounds(dimension.width / 2 - h.WIDTH / 2, dimension.height / 2 - h.HEIGHT / 2,
						h.WIDTH, h.HEIGHT);
		setLayout(new BorderLayout());
		setResizable(false);

		carMoving = new CarAI();
		motoMoving = new MotorbikeAI();

		JPanel scene = new JPanel();
		scene.setLayout(new BorderLayout());
		add(scene);

		MyPanel road = new MyPanel();
		road.setBounds(0, 0, WIDTH - 350, HEIGHT);
		road.setLayout(new FlowLayout(FlowLayout.LEFT));
		road.setBackground(Color.WHITE);
		scene.add(road);

		timeLabel = new JLabel("Время: 0 с");
		timeLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 16));
		timeLabel.setForeground(Color.RED);
		timeLabel.setVisible(false);
		road.add(timeLabel);

		// -------------------------- Панель -----------------------------

		panel = new JPanel();
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				requestFocusInWindow();
			}
		});
		panel.setPreferredSize(new Dimension(290, HEIGHT));
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
		start.addActionListener(e -> {
			startSimulation();


		});
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
		carsFreqText = new JTextField("" + N1, 2);
		carsFreq.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
		carsFreqText.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
		submitCar.addActionListener(e -> {
			try {
				N1 = Integer.parseInt(carsFreqText.getText());
				if (N1 <= 0) throw new Exception();
			} catch (Exception exp) {
				JOptionPane.showMessageDialog(this, "Введите целое положительое число!");
				N1 = 3;
				System.out.println("Поймано исключение " + exp.getMessage());
				carsFreqText.setText("" + N1);
			}
		});
		submitCar.setFocusable(false);
		panel.add(carsFreq);
		panel.add(carsFreqText);
		panel.add(submitCar);

		submitMoto = new JButton("Ок");
		submitMoto.setPreferredSize(new Dimension(50, 15));
		JLabel motoFreq = new JLabel("Частота появления мотоциклов");
		motoFreqText = new JTextField("" + N2, 2);
		motoFreq.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
		motoFreqText.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
		submitMoto.addActionListener(e -> {
			try {
				N2 = Integer.parseInt(motoFreqText.getText());
				if (N2 <= 0) throw new Exception();
			} catch (Exception exp) {
				JOptionPane.showMessageDialog(this, "Введите целое положительое число!");
				N2 = 4;
				System.out.println("Поймано исключение " + exp.getMessage());
				motoFreqText.setText("" + N2);
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
		carProbability.setSelectedItem("" + P1 + "%");
		panel.add(carProbability);

		carProbability.addActionListener(e -> {
			if (e.getSource() == carProbability) {
				String prb = Objects.requireNonNull(carProbability.getSelectedItem()).toString().replace("%", "");
				P1 = Integer.parseInt(prb);
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
				P2 = Integer.parseInt(prb);
			}
		});
		motoProbability.setFocusable(false);
		panel.add(motoProbability);

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

		speedSlider = new JSlider(0, 50, 35);
		speedSlider.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
		speedSlider.setBackground(panel.getBackground());
		JLabel speedLabel = new JLabel("Скорость: " + Habitat.speed);
		speedSlider.addChangeListener(e -> {
			Habitat.speed = (float) speedSlider.getValue() / 10;
			speedLabel.setText("Скорость: " + Habitat.speed);
		});
		panel.add(speedSlider);
		panel.add(speedLabel);

		carAI = new JButton("Движение машин");
		carAI.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
		carAI.setPreferredSize(new Dimension(280, 30));
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
		carAI.setEnabled(false);
		panel.add(carAI);

		motoAI = new JButton("Движение мотоциклов");
		motoAI.setFont(new Font("JetBrains Mono", Font.BOLD, 12));
		motoAI.setPreferredSize(new Dimension(280, 30));

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
		motoAI.setEnabled(false);
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
				assert prb != null;
				carMoving.setPriority(Integer.parseInt(prb));
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
				String prb = Objects.requireNonNull(motoPriority.getSelectedItem()).toString();
				motoMoving.setPriority(Integer.parseInt(prb));
			}
		});
		motoPriority.setFocusable(false);

		console = new JButton("Консоль");
		console.setPreferredSize(new Dimension(280, 30));
		console.setFont(new Font("JetBrains Mono", Font.BOLD, 12));

		// -------------------------- Консоль -----------------------------
		console.addActionListener(e -> {
			try {
				consoleDialog = new MyConsole(this, h);
				consoleDialog.setVisible(true);
			} catch (IOException ex) {
				System.out.println("IOException: " + ex.getMessage());
			}
		});
		console.setFocusable(false);
		console.setEnabled(false);
		panel.add(console);

		// -------------------------- Меню -----------------------------

		JMenuBar menu = new JMenuBar();

		JMenu fileMenu = new JMenu("Файл");
		saveSimilation = new JMenuItem("Сохранить");
		loadSimilation = new JMenuItem("Загрузить");
		loadSimilation.setEnabled(false);

		saveSimilation.addActionListener(e -> {
			try (FileOutputStream outputStream = new FileOutputStream("C:\\Users\\Александр\\Java\\Labs-4-sem\\JavaLabs\\src\\nstu\\client\\config.txt");
					 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
				objectOutputStream.writeObject(h.vehicles);
				objectOutputStream.writeObject(h.ids);
				objectOutputStream.writeObject(h.times);

				int isInfo = showInfo.isSelected() ? 1 : 0;
				int showT = showTimer.isSelected() ? 1 : 0;
				int hideT = hideTimer.isSelected() ? 1 : 0;
				outputStream.write(isInfo);
				outputStream.write(showT);
				outputStream.write(hideT);

				outputStream.write(h.N1);
				outputStream.write(h.N2);
				outputStream.write(h.P1);
				outputStream.write(h.P2);

				outputStream.write((int) Car.getTimeLifeCar());
				outputStream.write((int) Motorbike.getTimeLifeMoto());

				outputStream.write((int) (h.speed * 10));
				outputStream.write(carMoving.getPriority());
				outputStream.write(motoMoving.getPriority());

			} catch (IOException exp) {
				System.out.println("OutputException: " + exp.getMessage());
			}
		});

		loadSimilation.addActionListener(e -> {
			try (FileInputStream inputStream = new FileInputStream("C:\\Users\\Александр\\Java\\Labs-4-sem\\JavaLabs\\src\\nstu\\client\\config.txt");
					 ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
				time = 0;
				h.carCount = 0;
				h.motoCount = 0;
				h.vehicles.clear();
				h.ids.clear();
				h.times.clear();
				h.vehicles = (List<Vehicle>) objectInputStream.readObject();
				h.ids = (Set<Integer>) objectInputStream.readObject();
				h.times = (Map<Integer, Long>) objectInputStream.readObject();

				boolean isInfoB = inputStream.read() == 1;
				boolean showTB = inputStream.read() == 1;
				boolean hideTB = inputStream.read() == 1;
				showInfo.setSelected(isInfoB);
				showInfoItem.setSelected(isInfoB);
				showTimerItem.setSelected(showTB);
				showTimer.setSelected(showTB);
				hideTimer.setSelected(hideTB);
				hideTimerItem.setSelected(hideTB);
				timeLabel.setVisible(showTB);

				h.N1 = inputStream.read();
				h.N2 = inputStream.read();
				h.P1 = inputStream.read();
				h.P2 = inputStream.read();

				Car.setTimeLifeCar(inputStream.read());
				Motorbike.setTimeLifeMoto(inputStream.read());

				h.speed = (float) (inputStream.read() / 10);
				carMoving.setPriority(inputStream.read());
				motoMoving.setPriority(inputStream.read());

				for (int i = 0; i < h.vehicles.size(); i++) {
					Vehicle v = h.vehicles.get(i);
					v.setTimeAppear(0);
					h.times.put(v.getId(), 0L);
					if (v instanceof Car) {
						h.carCount++;
					} else {
						h.motoCount++;
					}
				}
				carsFreqText.setText("" + h.N1);
				motoFreqText.setText("" + h.N2);
				carProbability.setSelectedItem("" + h.P1 + "%");
				motoProbability.setSelectedIndex(h.P2 / 10);
				carsTimeText.setText("" + Car.getTimeLifeCar());
				motoTimeText.setText("" + Motorbike.getTimeLifeMoto());
				speedSlider.setValue((int) (h.speed * 10));
				carPriority.setSelectedItem("" + carMoving.getPriority());
				motoPriority.setSelectedItem("" + motoMoving.getPriority());


				System.out.println(h.N1 + " " + h.N2 + " " + h.P1 + " " + h.P2 + " " + h.speed + " " + carMoving.getPriority() + " " +
								motoMoving.getPriority() + " ");

			} catch (IOException ex1) {
				ex1.printStackTrace();
			} catch (ClassNotFoundException ex2) {
				System.out.println("ClassNotFoundException: " + ex2.getMessage());
			}
		});

		fileMenu.add(saveSimilation);
		fileMenu.add(loadSimilation);

		JMenu simulationMenu = new JMenu("Симуляция");
		startItem = new JMenuItem("Старт");
		stopItem = new JMenuItem("Стоп");
		networkItem = new JMenuItem("Сеть");
		showInfoItem = new JCheckBoxMenuItem("Показывать информацию");
		startItem.addActionListener(e -> startSimulation());
		stopItem.addActionListener(e -> stopSimulation());
		stopItem.setEnabled(false);
		showInfoItem.addActionListener(e -> showInfo.setSelected(showInfoItem.isSelected()));
		networkItem.addActionListener(e -> {
			networkDialog = new NDialog(this);
			networkDialog.setVisible(true);
		});
		simulationMenu.add(startItem);
		simulationMenu.add(stopItem);
		simulationMenu.add(networkItem);
		simulationMenu.add(showInfoItem);

		JMenu timerMenu = new JMenu("Таймер");
		showTimerItem = new JRadioButtonMenuItem("Показывать");
		hideTimerItem = new JRadioButtonMenuItem("Скрывать");
		showTimerItem.addActionListener(e -> getTimer());
		hideTimerItem.addActionListener(e -> getTimer());
		hideTimerItem.setSelected(true);
		timerMenu.add(showTimerItem);
		timerMenu.add(hideTimerItem);

		JMenu dbMenu = new JMenu("БД");
		saveToDB = new JMenu("Сохранить");

		saveToDBAllItem = new JMenuItem("Все объекты");
		saveToDBOnlyCarsItem = new JMenuItem("Машины");
		saveToDBOnlyMotosItem = new JMenuItem("Мотоциклы");
		saveToDBAllItem.addActionListener(e -> {
			dropAndCreate();
			try {
				for (Vehicle v : vehicles) {
					statement.execute("INSERT INTO " + VEHICLES_TABLE + " (id, type, x, y, timeAppear) " +
									"VALUES(" + v.getId() + ", " + "\'" + v.getClass().getName().substring(21) + "\'" + ", " + (int) v.getX() + ", " + (int) v.getY() + ", " + (int) v.getTimeAppear() + ")");
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		});
		saveToDBOnlyCarsItem.addActionListener(e -> {
			dropAndCreate();
			try {
				for (Vehicle v : vehicles) {
					if (v instanceof Car) {
						statement.execute("INSERT INTO " + VEHICLES_TABLE + " (id, type, x, y, timeAppear) " +
										"VALUES(" + v.getId() + ", " + "\'" + v.getClass().getName().substring(21) + "\'" + ", " + (int) v.getX() + ", " + (int) v.getY() + ", " + (int) v.getTimeAppear() + ")");

					}
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		});
		saveToDBOnlyMotosItem.addActionListener(e -> {
			dropAndCreate();
			try {
				for (Vehicle v : vehicles) {
					if (v instanceof Motorbike) {
						statement.execute("INSERT INTO " + VEHICLES_TABLE + " (id, type, x, y, timeAppear) " +
										"VALUES(" + v.getId() + ", " + "\'" + v.getClass().getName().substring(21) + "\'" + ", " + (int) v.getX() + ", " + (int) v.getY() + ", " + (int) v.getTimeAppear() + ")");
					}
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		});

		loadFromDB = new JMenu("Загрузить");
		loadFromDBAllItem = new JMenuItem("Все объекты");
		loadFromDBOnlyCarsItem = new JMenuItem("Машины");
		loadFromDBOnlyMotosItem = new JMenuItem("Мотоциклы");
		loadFromDBAllItem.addActionListener(e -> {
			h.vehicles.clear();
			try {
				con = DriverManager.getConnection(CONNECTION_STRING);
				statement = con.createStatement();
				resultSet = statement.executeQuery("SELECT id, type, x, y FROM " + VEHICLES_TABLE);
				while (resultSet.next()) {
					int id = resultSet.getInt(1);
					String type = resultSet.getString(2);
					int x = resultSet.getInt(3);
					int y = resultSet.getInt(4);
					if (type.equals("Car")) {
						h.vehicles.add(new Car(x, y, id, (int) time));
					} else if (type.equals("Motorbike")) {
						h.vehicles.add(new Motorbike(x, y, id, (int) time));
					}
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		});
		loadFromDBOnlyCarsItem.addActionListener(e -> {
			h.vehicles.clear();
			try {
				con = DriverManager.getConnection(CONNECTION_STRING);
				statement = con.createStatement();
				resultSet = statement.executeQuery("SELECT id, x, y FROM " + VEHICLES_TABLE + " WHERE type = 'Car'");
				while (resultSet.next()) {
					int id = resultSet.getInt(1);
					int x = resultSet.getInt(2);
					int y = resultSet.getInt(3);
					h.vehicles.add(new Car(x, y, id, (int) time));
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		});
		loadFromDBOnlyMotosItem.addActionListener(e -> {
			h.vehicles.clear();
			try {
				con = DriverManager.getConnection(CONNECTION_STRING);
				statement = con.createStatement();
				resultSet = statement.executeQuery("SELECT id, x, y FROM " + VEHICLES_TABLE + " WHERE type = 'Motorbike'");
				while (resultSet.next()) {
					int id = resultSet.getInt(1);
					int x = resultSet.getInt(2);
					int y = resultSet.getInt(3);
					h.vehicles.add(new Motorbike(x, y, id, (int) time));
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		});

		dbMenu.add(saveToDB);
		dbMenu.add(loadFromDB);
		saveToDB.add(saveToDBAllItem);
		saveToDB.add(saveToDBOnlyCarsItem);
		saveToDB.add(saveToDBOnlyMotosItem);
		loadFromDB.add(loadFromDBAllItem);
		loadFromDB.add(loadFromDBOnlyCarsItem);
		loadFromDB.add(loadFromDBOnlyMotosItem);
		menu.add(fileMenu);
		menu.add(simulationMenu);
		menu.add(timerMenu);
		menu.add(dbMenu);

		setJMenuBar(menu);
		menu.setFocusable(false);

		setVisible(true);
	}
}
