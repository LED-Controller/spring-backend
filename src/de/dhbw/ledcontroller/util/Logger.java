package de.dhbw.ledcontroller.util;

public class Logger {

	private String componentName;

	public Logger() {
		try {
			this.componentName = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName()).getSimpleName();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			componentName = "???";
		}
	}

	public void log(String msg) {
		System.out.println("[" + componentName + "] " + msg);
	}

}
