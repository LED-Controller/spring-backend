package de.dhbw.ledcontroller.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import de.dhbw.ledcontroller.connection.LightStripConnection;
import de.dhbw.ledcontroller.repositories.LampRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UnknownDeviceController {
	@Autowired
	LampRepository lampRepository;

	@GetMapping("/unknown")
	public ResponseEntity<?> getUnknownDevices() {
		List<String> unknownMacs = new ArrayList<>();
		for (LightStripConnection connection : LightStripConnection.connectionList) {
			if (!lampRepository.findByMac(connection.getMac()).isPresent()) {
				unknownMacs.add(connection.getMac());
			}
		}
		return ResponseEntity.ok(unknownMacs);

	}
}
