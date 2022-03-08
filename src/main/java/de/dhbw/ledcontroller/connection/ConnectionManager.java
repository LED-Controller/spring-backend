package de.dhbw.ledcontroller.connection;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.UUID;

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
		changeSessionFile();
		startSessionThread();
		startSocketThread();
		startPingThread();
		logger.log("Server wurde gestartet.");
	}

	private File sessionFile;

	private void changeSessionFile() {
		try {
			sessionFile = new File("session.lock");
			sessionFile.createNewFile();
			session = UUID.randomUUID().toString();
			Files.write(sessionFile.toPath(), session.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String session;

	private ServerSocket serverSocket;

	private Thread pingThread;
	private Thread socketThread;
	private Thread sessionThread;

	private void startSessionThread() {
		sessionThread = new Thread(() -> {
			while (!Thread.interrupted()) {
				try {
					Thread.sleep(2 * 1000);
					String currentSession = Files.readAllLines(sessionFile.toPath()).get(0);
					if (!currentSession.equals(session)) {
						logger.log("Reload erkannt, stoppe alle Threads.");

						if (serverSocket != null && !serverSocket.isClosed()) {
							serverSocket.close();
						}
						pingThread.interrupt();
						socketThread.interrupt();
						sessionThread.interrupt();
					}
				} catch (InterruptedException e) {
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		sessionThread.start();
	}

	private void startSocketThread() {
		try {
			Thread.sleep(4 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		socketThread = new Thread(() -> {
			try {
				serverSocket = new ServerSocket(PORT);
				while (true) {
					Socket socket = serverSocket.accept();
					LightStripConnection stripConnection = new LightStripConnection(socket);
					stripConnection.startThread();
				}
			} catch (IOException e) {
				if (e.getMessage().equals("socket closed")) {
					return;
				}
				e.printStackTrace();
			}
		});
		socketThread.start();
	}

	private void startPingThread() {
		pingThread = new Thread(() -> {
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