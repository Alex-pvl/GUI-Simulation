package nstu.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class StartServer {
	public static int id = 1;
	public static List<Integer> clientIds = new ArrayList<>();
	public static Map<Integer, Server> clients = new HashMap<>();
	public final static String connectCommand = "connectCommand";
	public final static String swapCommand = "swapCommand";
	public final static String disconnectCommand = "disconnectCommand";

	public static void main(String[] args) {
		try {
			System.out.println("Сервер запущен");
			ServerSocket ss = new ServerSocket(8030);
			while (true) {
				Socket s = ss.accept();
				Server server = new Server(s, id);
				clientIds.add(id);
				clients.put(id, server);
				id++;
				server.start();
				update();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void update() {
		for (int i = 0; i < clients.size() - 1; i++) {
			clients.get(clientIds.get(i)).update();
		}
	}

	public static void updateAll() {
		for (int i = 0; i < clients.size(); i++) {
			clients.get(clientIds.get(i)).update();
		}
	}
}

