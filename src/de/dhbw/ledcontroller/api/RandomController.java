package de.dhbw.ledcontroller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.dhbw.ledcontroller.connection.LightStripConnection;

@RestController
public class RandomController {

	@RequestMapping("/random")
	public ResponseEntity<?> home() {
		for(LightStripConnection connection : LightStripConnection.connectionList) {
			connection.sendToStrip("random");
		}
		return ResponseEntity.ok().build(); 
	}

}
