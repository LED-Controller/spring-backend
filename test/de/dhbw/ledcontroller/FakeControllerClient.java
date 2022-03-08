package de.dhbw.ledcontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

		send("REGISTER " + mac);

		t = new Thread(() -> {
			while(true) {
				String res = "";
				while (true) {
					char data;
					try {
						data = (char) reader.read();
						if(data == '\n') {
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

	public static void main(String[] args) throws IOException {
		FakeControllerClient fakeController = new FakeControllerClient("AC:12:DS:22:21");
		fakeController.connect();
	}
	
}
