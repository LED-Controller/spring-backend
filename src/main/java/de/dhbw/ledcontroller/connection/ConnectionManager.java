package de.dhbw.ledcontroller.connection;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import de.dhbw.ledcontroller.util.Logger;

@Component
public class ConnectionManager {

	public static final int PORT = 18533;
	private static Logger logger = new Logger();

	@EventListener(ApplicationReadyEvent.class)
	public void startConnectionManager() {
		startSocketThread();
		startPingThread();
		logger.log("Server wurde gestartet.");
	}

	private void startSocketThread() {
		Thread socketThread = new Thread(() -> {
			try {
				ServerSocket serverSocket = new ServerSocket(PORT);
				while (!Thread.interrupted()) {
					Socket socket = serverSocket.accept();
					LightStripConnection stripConnection = new LightStripConnection(socket);
					stripConnection.startThread();
				}
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		socketThread.start();
	}

	private void startPingThread() {
		Thread pingThread = new Thread(() -> {
			while (!Thread.interrupted()) {
				try {
					Thread.sleep(1000 * 15);
				} catch (InterruptedException e) {
					return;
				}
				printCurrentConnections();
				for (LightStripConnection connection : LightStripConnection.connectionList) {
					connection.sendPing();
				}
			}
		});
		pingThread.start();
	}

	private void printCurrentConnections() {
		int size = LightStripConnection.connectionList.size();
		if (size == 0) {
			logger.log("Es sind aktuell keine Streifen verbunden.");
		} else if (size == 1) {
			logger.log("Es ist aktuell 1 Streifen verbunden.");
		} else {
			logger.log("Es sind aktuell " + size + " Streifen verbunden.");
		}
	}

}