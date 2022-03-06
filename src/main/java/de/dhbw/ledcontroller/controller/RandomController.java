package de.dhbw.ledcontroller.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.dhbw.ledcontroller.connection.CommandGenerator;
import de.dhbw.ledcontroller.connection.LightStripConnection;

@RestController
public class RandomController {

	// TODO
	// Sollte soweit funktionieren, allerdings wird derzeit nichts mit einem Lamp-Objekt gemacht.
	// Das Lamp-Objekt muss anhand der MAC aus der Datenbank geladen werden.
	// Anschließend mit den Zufallswerten überschrieben werden.
	// Dann die Daten an den Controller übertragen (über globale Methode, welche einfach das Lamp Objekt bekommt?)
	// Zuletzt das neue Lamp Objekt zurück in die Datenbank (auch über die (selbe) globale Methode?
	
	@PostMapping("/random")
	public ResponseEntity<?> random(@RequestParam String mac) {
		int r = rand(30, 255);
		int g = rand(30, 255);
		int b = rand(30, 255);

		String cmd = CommandGenerator.colorRGB(r, g, b);
		boolean success = send(mac, cmd);
		if (success) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

	private int rand(int min, int max) {
		int range = max - min + 1;
		return (int) (Math.random() * range) + min;
	}

	// send util
	private boolean send(String mac, String data) {
		Optional<LightStripConnection> opt = LightStripConnection.connectionList.stream().filter(c -> c.getMac().equals(mac)).findAny();
		if (opt.isPresent()) {
			opt.get().sendToStrip(data);
			return true;
		}
		return false;
	}

}
