package de.dhbw.ledcontroller.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import de.dhbw.ledcontroller.util.Logger;

@Component
public class ConnectionManager {

	private static final int PORT = 18533;
	private static Logger logger = new Logger();

	private static boolean alreadyRunning = false;
	
	@EventListener(ApplicationReadyEvent.class)
	public void startConnectionManager() {
		if(alreadyRunning) {
			logger.log("Server wurde NICHT erneut gestartet.");
		} else {
			alreadyRunning = true;
			startSocketThread();
			startPingThread();
			logger.log("Server wurde gestartet.");
		}
	}

	private Thread pingThread;
	private Thread socketThread;

	private void startSocketThread() {
		socketThread = new Thread(() -> {
			try {
				try (ServerSocket serverSocket = new ServerSocket(PORT)) {
					while (true) {
						Socket socket = serverSocket.accept();
						logger.log("IP " + socket.getInetAddress().getHostAddress() + " hat sich verbunden.");
						LightStripConnection stripConnection = new LightStripConnection(socket);
						stripConnection.startThread();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		socketThread.start();
	}

	private void startPingThread() {
		pingThread = new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(1000 * 15);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				logger.log("Es sind aktuell " + LightStripConnection.connectionList.size() + " Streifen verbunden.");

				for (LightStripConnection connection : LightStripConnection.connectionList) {
					connection.sendPing();
				}
			}

		});
		pingThread.start();
	}

}