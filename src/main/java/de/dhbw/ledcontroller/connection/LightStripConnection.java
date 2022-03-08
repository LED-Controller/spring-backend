package de.dhbw.ledcontroller.connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.dhbw.ledcontroller.util.Logger;

public class LightStripConnection {

	private static Logger logger = new Logger();

	public static List<LightStripConnection> connectionList = Collections.synchronizedList(new ArrayList<LightStripConnection>());

	private Thread dataThread;
	private DataOutputStream out;
	private BufferedReader in;

	private String mac;

	public LightStripConnection(Socket socketConnection) {
		try {
			in = new BufferedReader(new InputStreamReader(socketConnection.getInputStream()));
			out = new DataOutputStream(socketConnection.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startThread() {
		dataThread = new Thread(() -> {
			handleIncomingData();
		});
		dataThread.start();
		while (!handleInputActive) {
		}
	}

	boolean handleInputActive = false;

	private void handleIncomingData() {
		handleInputActive = true;
		while (true) {
			try {
				String data = in.readLine();
				handleData(data);
			} catch (IOException e) {
				remove();
				return;
			}
		}
	}

	private void handleData(String data) {
		if (data.startsWith("REG ")) {
			String[] split = data.split(" ");
			mac = split[1];
			add();
		}

		if (!data.equals("pong")) {
			logger.log("FROM " + mac + " | " + data);
		}
	}

	public void sendToStrip(String data) {
		if(!data.equals("ping")) {
			logger.log("TO   " + mac + " | " + data);
		}
		data = data + '\n';
		try {
			out.writeBytes(data);
			out.flush();
		} catch (IOException e) {
			remove();
			e.printStackTrace();
		}
	}

	private void remove() {
		connectionList.remove(this);
		logger.log(mac + " wurde getrennt.");
	}

	private void add() {
		connectionList.add(this);
		logger.log(mac + " wurde verbunden.");
	}

	public void sendPing() {
		sendToStrip("ping");
	}

	public String getMac() {
		return mac;
	}

}