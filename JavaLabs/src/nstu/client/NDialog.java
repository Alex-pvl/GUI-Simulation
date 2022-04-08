package nstu.client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class NDialog extends JDialog {
	public JLabel isConnectedLabel;
	private Client client;
	private final JList<String> clients;
	private List<Integer> clientIds;

	public NDialog(MyFrame parent) {
		super(parent, "Сетевое окно", false);
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(350, 300));
		setBounds(0, 0, 350, 300);

		isConnectedLabel = new JLabel("Статус: " + false);
		isConnectedLabel.setFont(new Font("JetBrains Mono", Font.BOLD, 14));
		isConnectedLabel.setPreferredSize(new Dimension(350, 50));
		isConnectedLabel.setBorder(new EmptyBorder(0, 110, 0, 0));
		add(isConnectedLabel, BorderLayout.NORTH);

		JPanel buttonsPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonsPnl.setPreferredSize(new Dimension(160, 170));
		buttonsPnl.setBorder(new EmptyBorder(2, 2, 2, 2));
		buttonsPnl.setAlignmentX(Component.RIGHT_ALIGNMENT);
		add(buttonsPnl, BorderLayout.EAST);

		JButton connectBtn = new JButton("Подключиться");
		connectBtn.setPreferredSize(new Dimension(150, 25));
		connectBtn.setFocusable(false);
		connectBtn.addActionListener(e -> connect());
		JButton swapBtn = new JButton("Обменяться");
		swapBtn.setPreferredSize(new Dimension(150, 25));
		swapBtn.setFocusable(false);
		swapBtn.addActionListener(e -> swap());
		JButton disconnectBtn = new JButton("Отключиться");
		disconnectBtn.setPreferredSize(new Dimension(150, 25));
		disconnectBtn.setFocusable(false);
		disconnectBtn.addActionListener(e -> disconnect());
		buttonsPnl.add(connectBtn);
		buttonsPnl.add(swapBtn);
		buttonsPnl.add(disconnectBtn);
		buttonsPnl.setVisible(true);

		JPanel clientsPnl = new JPanel(new BorderLayout());
		clientsPnl.setBorder(new EmptyBorder(5, 10, 5, 10));
		clients = new JList<>();
		clientsPnl.add(clients);
		add(clientsPnl);

		setLocationRelativeTo(null);
		setVisible(false);
	}

	public void getListOfClients(List<Integer> clientIds, int id) {
		this.clientIds = clientIds;
		String[] clientsList = new String[clientIds.size()];
		for (int i = 0; i < clientIds.size(); i++) {
			if (clientIds.get(i) == id) {
				clientsList[i] = "[*] Клиент №" + clientIds.get(i);
			} else {
				clientsList[i] = "Клиент №" + clientIds.get(i);
			}
		}
		clients.setListData(clientsList);
	}

	public void connect() {
		client = new Client(this);
		client.start();
	}

	public void swap() {
		if (clientIds != null && !clientIds.isEmpty()) {
			client.swap(clientIds.get(clients.getSelectedIndex()));
		}
	}

	public void disconnect() {
		client.disconnect();
		clients.setListData(new String[0]);
	}

}
