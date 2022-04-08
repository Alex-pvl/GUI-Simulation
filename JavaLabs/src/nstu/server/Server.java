package nstu.server;

import nstu.client.vehicles.Vehicle;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server extends Thread {
	public Socket s;
	public int id;
	public String command;
	public ObjectOutputStream oos;
	public ObjectInputStream ois;
	public boolean isConnected = true;

	public Server(Socket s, int id) {
		this.s = s;
		this.id = id;
	}

	@Override
	public void run() {
		try {
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			oos.writeObject(StartServer.clientIds);
			oos.writeInt(id);
			oos.flush();
			while (isConnected) {
				command = ois.readUTF();
				switch (command) {
					case StartServer.connectCommand -> {
						int idToSwap = ois.readInt();
						List<Vehicle> vehiclesList1 = (ArrayList<Vehicle>) ois.readObject();
						StartServer.clients.get(idToSwap).oos.writeUTF("swap1");
						StartServer.clients.get(idToSwap).oos.writeObject(vehiclesList1);
						StartServer.clients.get(idToSwap).oos.writeInt(id);
						StartServer.clients.get(idToSwap).oos.flush();
					}
					case StartServer.swapCommand -> {
						List<Vehicle> vehiclesList2 = (ArrayList<Vehicle>) ois.readObject();
						int idToSwap = ois.readInt();
						StartServer.clients.get(idToSwap).oos.writeUTF("swap2");
						StartServer.clients.get(idToSwap).oos.writeObject(vehiclesList2);
						StartServer.clients.get(idToSwap).oos.flush();
					}
					case StartServer.disconnectCommand -> {
						System.out.println("Отключение от сервера");
						disconnect();
					}
					default -> System.out.println("Неверная команда");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconnect();
			StartServer.updateAll();
		}
	}

	public void update() {
		try {
			oos.writeUTF("updateListOfClients");
			List<Integer> newClientIds = new ArrayList<>(StartServer.clientIds);
			oos.writeObject(newClientIds);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			isConnected = false;
			ois.close();
			oos.close();
			s.close();
			StartServer.clients.remove(id);
			for (int i = 0; i < StartServer.clientIds.size(); i++) {
				if (StartServer.clientIds.get(i) == id) {
					StartServer.clientIds.remove(i);
					i--;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

