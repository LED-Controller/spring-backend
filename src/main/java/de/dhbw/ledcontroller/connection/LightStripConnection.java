package de.dhbw.ledcontroller.connection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import de.dhbw.ledcontroller.controller.ControllerService;
import de.dhbw.ledcontroller.controller.LampController;
import de.dhbw.ledcontroller.models.Lamp;
import de.dhbw.ledcontroller.payload.LedColorRGB;
import de.dhbw.ledcontroller.util.Logger;

public class LightStripConnection {

	private static Logger logger = new Logger();

	public static List<LightStripConnection> connectionList = Collections
			.synchronizedList(new ArrayList<LightStripConnection>());

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

		if (data.startsWith("REG ")) {
			sendToStrip("welcome");
		}
	}

	public void sendToStrip(String data) {
		if (!data.equals("ping")) {
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
		if (dataThread != null && dataThread.isAlive()) {
			dataThread.interrupt();
		}
		logger.log(mac + " wurde getrennt.");
	}

	private void add() {
		for (LightStripConnection c : connectionList) {
			if (c.getMac().equals(this.mac)) {
				logger.log(mac + " ist erneut verbunden?");
				connectionList.remove(c);
				break;
			}
		}

		connectionList.add(this);
		logger.log(mac + " wurde verbunden.");

		Optional<Lamp> optional = LampController.getStaticLampController().findByMac(mac);
		if (optional.isPresent()) {
			Lamp lamp = optional.get();
			if (lamp.isOn()) {
				String cmd = ControllerService
						.getColorCmd(new LedColorRGB(lamp.getRed(), lamp.getGreen(), lamp.getBlue()), lamp);
				ControllerService.sendDataToController(lamp.getMac(), cmd);
			}
		}
	}

	public void sendPing() {
		sendToStrip("ping");
	}

	public String getMac() {
		return mac;
	}

}