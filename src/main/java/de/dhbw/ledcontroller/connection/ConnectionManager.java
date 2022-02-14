package main.java.de.dhbw.ledcontroller.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import main.java.de.dhbw.ledcontroller.util.Logger;

@Component
public class ConnectionManager {

	private static final int PORT = 18533;
	private static Logger logger = new Logger();

	private static List<LightStripConnection> connectionList = Collections.synchronizedList(new ArrayList<LightStripConnection>());

	public static void remove(LightStripConnection lightStripConnection) {
		connectionList.remove(lightStripConnection);
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void startConnectionManager() {
		startPingThread();
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			logger.log("listening for connections...");
			while (true) {
				Socket socket = serverSocket.accept();
				logger.log(socket.getInetAddress().getHostAddress() + " connected");
				LightStripConnection stripConnection = new LightStripConnection(socket);
				stripConnection.startThread();
				connectionList.add(stripConnection);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Thread pingThread;

	private void startPingThread() {
		pingThread = new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(1000 * 15);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				logger.log("send ping an " + connectionList.size() + " strips");
				
				for (LightStripConnection connection : connectionList) {
					connection.sendPing();
				}
			}

		});
		pingThread.start();
	}

}