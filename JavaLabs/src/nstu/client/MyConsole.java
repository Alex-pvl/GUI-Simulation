package nstu.client;

import nstu.client.vehicles.Motorbike;
import nstu.client.vehicles.Vehicle;

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
		setBounds(1000, 300, 500, 420);
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
					outputArea.append(line + "\nmotoCount: " + motoCountPrev + " -> " + habitat.motoCount + "\n");
					while (motoCountPrev > habitat.motoCount) {
						for (int i = 0; i < habitat.vehicles.size(); i++) {
							Vehicle v = habitat.vehicles.get(i);
							if (v instanceof Motorbike) {
								habitat.vehicles.remove(i);
								System.out.println("-motorbike{" + v.getX() + "; " + v.getY() + "; " + v.getId() + "}");
								break;
							}
						}
						motoCountPrev--;
					}
					outputArea.append("nstu@road $: ");
				}
			} else {
				JOptionPane.showMessageDialog(null, "Неверная команда!", "Ошибка!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
