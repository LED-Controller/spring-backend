package de.dhbw.ledcontroller.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.dhbw.ledcontroller.connection.LightStripConnection;

@RestController
public class TestController {

	@RequestMapping("/random")
	public ResponseEntity<?> home() {
		for (LightStripConnection connection : LightStripConnection.connectionList) {
			connection.sendToStrip("random");
		}
		return ResponseEntity.ok().build();
	}

}
