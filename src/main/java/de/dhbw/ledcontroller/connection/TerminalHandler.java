package de.dhbw.ledcontroller.connection;

import java.util.Scanner;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TerminalHandler {

	private boolean running = true;
	
	@EventListener(ApplicationReadyEvent.class)
	public void startConnectionManager() {
		new Thread(() -> {
			Scanner scanner = new Scanner(System.in);
			while (running) {
				String line = scanner.nextLine();
				LightStripConnection.connectionList.stream().forEach(c -> {
					c.sendToStrip(line);
				});
			}
			scanner.close();
		}).start();
	}

}
