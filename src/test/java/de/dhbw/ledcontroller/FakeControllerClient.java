<<<<<<< HEAD:src/test/java/de/dhbw/ledcontroller/FakeControllerClient.java
package de.dhbw.ledcontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import de.dhbw.ledcontroller.connection.ConnectionManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class FakeControllerClient {

	private final String mac;

	private Socket socket;
	private PrintWriter writer;

	private Thread t;

	@SneakyThrows
	public void connect() throws IOException {
		socket = new Socket("localhost", ConnectionManager.PORT);
		InputStream input = socket.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));

		writer = new PrintWriter(socket.getOutputStream(), true);

		Thread.sleep(1 * 1000);

		send("REG " + mac);

		t = new Thread(() -> {
			while (true) {
				String res = "";
				while (true) {
					char data;
					try {
						data = (char) reader.read();
						if (data == '\n') {
							break;
						}
						res += data;
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
				}
				System.out.println("CLIENT " + mac + "  IN: " + res);
				send("OK");
			}
		});
		t.start();

	}

	private void send(String data) {
		System.out.println("CLIENT " + mac + " OUT: " + data);

		writer.write(data + "\n");
		writer.flush();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static ArrayList<String> macs = new ArrayList<String>(Arrays.asList("04:E0:9A:06:5D:85", "C2:EF:63:E9:6D:62", "3D:8F:99:6C:1B:E9", "19:E4:F0:32:3E:C4", "76:E2:39:85:9C:B1", "F7:33:C6:D9:B5:C5", "7A:30:CC:6C:75:10", "0E:F7:8C:88:D4:9C"));

	public static void main(String[] args) throws IOException {
		Collections.shuffle(macs);
		FakeControllerClient fakeController = new FakeControllerClient(macs.get(0));
		fakeController.connect();
	}

}
=======
package de.dhbw.ledcontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import de.dhbw.ledcontroller.connection.ConnectionManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class FakeControllerClient {

	private final String mac;

	private Socket socket;
	private PrintWriter writer;

	private Thread t;

	@SneakyThrows
	public void connect() throws IOException {
		socket = new Socket("localhost", ConnectionManager.PORT);
		InputStream input = socket.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));

		writer = new PrintWriter(socket.getOutputStream(), true);

		Thread.sleep(1 * 1000);

		send("REG " + mac);

		t = new Thread(() -> {
			while (true) {
				String res = "";
				while (true) {
					char data;
					try {
						data = (char) reader.read();
						if (data == '\n') {
							break;
						}
						res += data;
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
				}
				System.out.println("CLIENT " + mac + "  IN: " + res);
				
				if(res.equals("ping")) {
					send("pong");
				} else {
					send("ok");
				}
			}
		});
		t.start();

	}

	private void send(String data) {
		System.out.println("CLIENT " + mac + " OUT: " + data);

		writer.write(data + "\n");
		writer.flush();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static ArrayList<String> macs = new ArrayList<String>(Arrays.asList("04:E0:9A:06:5D:85", "C2:EF:63:E9:6D:62", "3D:8F:99:6C:1B:E9", "19:E4:F0:32:3E:C4", "76:E2:39:85:9C:B1", "F7:33:C6:D9:B5:C5", "7A:30:CC:6C:75:10", "0E:F7:8C:88:D4:9C"));

	public static void main(String[] args) throws IOException {
		Collections.shuffle(macs);
		FakeControllerClient fakeController = new FakeControllerClient(macs.get(0));
		fakeController.connect();
	}

}
>>>>>>> fda4ab42f5e6990ca4f33e5a699aa6262aa32ccd:test/de/dhbw/ledcontroller/FakeControllerClient.java
