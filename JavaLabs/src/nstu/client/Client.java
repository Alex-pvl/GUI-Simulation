package nstu.client;

import nstu.client.vehicles.Vehicle;
import nstu.server.*;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Client extends Thread {
  public Socket s = null;
	public int id;
	public int idToSwap;
	public ObjectOutputStream oos;
	public ObjectInputStream ois;
	public String command;
	public NDialog netDialog;
	public boolean isConnected = true;

	public Client(NDialog netDialog) {
		this.netDialog = netDialog;
	}

	@Override
	public void run() {
		try {
			s = new Socket("127.0.0.1", 8030);
			this.netDialog.isConnectedLabel.setText("Статут: " + true);
			isConnected = true;
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			List<Integer> clientIds = (ArrayList<Integer>) ois.readObject();
			id = ois.readInt();
			netDialog.getListOfClients(clientIds, id);

			while (isConnected) {
				if (ois.available() == 0) {
					if (command != null) {
						switch (command) {
							case "swap" -> {
								oos.writeUTF(StartServer.connectCommand);
								oos.writeInt(idToSwap);
								oos.writeObject(Habitat.getVehicles());
								oos.flush();
								command = null;
							}
							case "disconnect" -> {
								oos.writeUTF(StartServer.disconnectCommand);
								oos.flush();
								command = null;
							}
							default -> {}
						}
					}
				} else {
					String commandFromServer = ois.readUTF();
					System.out.println("Команда с сервера: " + commandFromServer);
					switch (commandFromServer) {
						case "swap1" -> {
							int idFrom;
							List<Vehicle> serverArray = (ArrayList<Vehicle>) ois.readObject();
							idFrom = ois.readInt();
							List<Vehicle> clientArray = Habitat.getVehicles();
							oos.writeUTF(StartServer.swapCommand);
							oos.writeObject(clientArray);
							oos.writeInt(idFrom);
							oos.flush();
							Habitat.vehicles.clear();
							Habitat.vehicles = new ArrayList<>(serverArray);
						}
						case "swap2" -> {
							List<Vehicle> serverArray = (ArrayList<Vehicle>) ois.readObject();
							Habitat.vehicles.clear();
							Habitat.vehicles = new ArrayList<>(serverArray);
						}
						case "updateListOfClients" -> {
							clientIds = (ArrayList<Integer>) ois.readObject();
							netDialog.getListOfClients(clientIds, id);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Закрытие всех потоков");
			try {
				ois.close();
				oos.close();
				s.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void swap(int idToSwap) {
		this.command = "swap";
		this.idToSwap = idToSwap;
	}

	public void disconnect() {
		this.command = "disconnect";
		this.netDialog.isConnectedLabel.setText("Статус: " + false);
	}
}
