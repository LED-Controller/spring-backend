package de.dhbw.ledcontroller.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.dhbw.ledcontroller.connection.CommandGenerator;
import de.dhbw.ledcontroller.payload.response.MessageResponse;
import de.dhbw.ledcontroller.repositories.LampRepository;
import de.dhbw.ledcontroller.util.ResponseType;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class BlinkController {
	@Autowired
	LampRepository lampRepository;

	@PostMapping("/blink")
	public ResponseEntity<?> random(@Valid @RequestBody String mac) {
		boolean success = ControllerService.sendDataToController(mac, CommandGenerator.test());
		if (success) {
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.badRequest().body(new MessageResponse("mac not found", ResponseType.ERROR));
	}

}