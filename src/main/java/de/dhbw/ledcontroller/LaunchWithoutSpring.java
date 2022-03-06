package de.dhbw.ledcontroller;

import de.dhbw.ledcontroller.connection.ConnectionManager;
import de.dhbw.ledcontroller.connection.TerminalHandler;

public class LaunchWithoutSpring {

	public static void main(String[] args) {
		TerminalHandler terminalHandler = new TerminalHandler();
		terminalHandler.startConnectionManager();

		ConnectionManager connectionManager = new ConnectionManager();
		connectionManager.startConnectionManager();
	}

}
