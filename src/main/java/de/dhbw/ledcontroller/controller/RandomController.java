package de.dhbw.ledcontroller.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.dhbw.ledcontroller.connection.CommandGenerator;
import de.dhbw.ledcontroller.connection.LightStripConnection;

@RestController
public class RandomController {

	private CommandGenerator commandGenerator = new CommandGenerator();

	@PostMapping("/random")
	public ResponseEntity<?> random(@RequestParam String mac) {
		int r = rand(30, 255);
		int g = rand(30, 255);
		int b = rand(30, 255);

		String cmd = commandGenerator.colorRGB(r, g, b);

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
