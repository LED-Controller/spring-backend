package main.java.de.dhbw.ledcontroller.connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class LightStripConnection {

	private Thread dataThread;
	private DataOutputStream out;
	private BufferedReader in;

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
				ConnectionManager.remove(this);
				e.printStackTrace();
				return;
			}
		}
	}

	private void handleData(String data) {
		// TODO
		System.out.println(data);
		sendToStrip("ok");
	}

	public void sendToStrip(String data) {
		data = data + "\n";
		try {
			out.writeBytes(data);
			out.flush();
		} catch (IOException e) {
			ConnectionManager.remove(this);
			e.printStackTrace();
		}
	}

	public void sendPing() {
		sendToStrip("ping");
	}

}