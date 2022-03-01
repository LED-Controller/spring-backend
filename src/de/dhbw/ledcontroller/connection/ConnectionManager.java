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

	@EventListener(ApplicationReadyEvent.class)
	public void startConnectionManager() {
		startPingThread();
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			logger.log("Listening for connections...");
			while (true) {
				Socket socket = serverSocket.accept();
				logger.log(socket.getInetAddress().getHostAddress() + " connected");
				LightStripConnection stripConnection = new LightStripConnection(socket);
				stripConnection.startThread();
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

				logger.log("Send ping an " + LightStripConnection.connectionList.size() + " strips.");

				for (LightStripConnection connection : LightStripConnection.connectionList) {
					connection.sendPing();
				}
			}

		});
		pingThread.start();
	}

}