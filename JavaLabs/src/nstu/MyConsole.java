package nstu;

import nstu.vehicles.Motorbike;
import nstu.vehicles.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class MyConsole extends JDialog implements Runnable {
	private Habitat habitat;
	private final Scanner fieldInput;
	private final JTextField inputField;
	private final JTextArea outputArea;

	MyConsole(MyFrame owner, Habitat habitat) throws IOException {
		super(owner, "Консоль", false);
		this.habitat = habitat;
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(500, 420);
		setVisible(false);
		requestFocus();

		JLabel commands = new JLabel("<html>Доступная команда: <br>\"reduce moto count by N%\" - Сократить число мотоциклов на N%");
		commands.setFont(new Font("JetBrains Mono", Font.BOLD, 12));

		outputArea = new JTextArea("nstu@road $: ");
		outputArea.setEditable(false);
		outputArea.setPreferredSize(new Dimension(475, 305));
		outputArea.setBackground(Color.BLACK);
		outputArea.setForeground(Color.GREEN);
		outputArea.setFont(new Font("JetBrains Mono", Font.BOLD, 13));

		inputField = new JTextField();
		inputField.setPreferredSize(new Dimension(475, 25));
		inputField.setFont(new Font("JetBrains Mono", Font.BOLD, 13));

		add(commands);
		add(inputField);
		add(outputArea);

		PipedOutputStream outputFromField = new PipedOutputStream();
		PipedInputStream inputFromField = new PipedInputStream(outputFromField);

		fieldInput = new Scanner(inputFromField);
		PrintStream fieldOutput= new PrintStream(outputFromField);

		inputField.addActionListener(e -> {
			String text = inputField.getText();
			fieldOutput.println(text);
			inputField.setText("");
		});

		setLocationRelativeTo(null);
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (fieldInput.hasNextLine()) {
			String line = fieldInput.nextLine();
			if (line.contains("reduce moto count by ") && line.contains("%")) {
				int motoCountPrev = habitat.motoCount;
				int prs = Integer.parseInt(line.replace("reduce moto count by ", "").replace("%", ""));
				if (prs > 100 || prs < 0) {
					JOptionPane.showMessageDialog(null, "N = [0; 100]", "Ошибка!", JOptionPane.ERROR_MESSAGE);
				} else {
					habitat.motoCount -= habitat.motoCount * prs / 100;
					System.out.println(motoCountPrev + " -> " + habitat.motoCount);
					while (motoCountPrev > habitat.motoCount) {
						for (int i = 0; i < habitat.vehicles.size(); i++) {
							Vehicle v = habitat.vehicles.get(i);
							if (v instanceof Motorbike) {
								habitat.vehicles.remove(i);
								break;
							}
						}
						motoCountPrev--;
					}
					outputArea.append(line + "\nnstu@road $: ");
				}
			} else {
				JOptionPane.showMessageDialog(null, "Неверная команда!", "Ошибка!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
