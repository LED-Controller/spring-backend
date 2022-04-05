package de.dhbw.ledcontroller.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.dhbw.ledcontroller.connection.CommandGenerator;
import de.dhbw.ledcontroller.payload.request.EffectRequest;
import de.dhbw.ledcontroller.payload.response.MessageResponse;
import de.dhbw.ledcontroller.repositories.LampRepository;
import de.dhbw.ledcontroller.util.ResponseType;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class EffectController {
	@Autowired
	LampRepository lampRepository;

	@PostMapping("/effect")
	public ResponseEntity<?> random(@Valid @RequestBody EffectRequest request) {
		if (lampRepository.findByMac(request.getMac()).isPresent()) {
			String cmd = CommandGenerator.effect(request.getEffectType().name());
			boolean success = ControllerService.sendDataToController(request.getMac(), cmd);
			if (success) {
				return ResponseEntity.ok().body(new MessageResponse("effect activated", ResponseType.SUCCESS));
			}
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.badRequest().body(new MessageResponse("lamp not found", ResponseType.ERROR));
	}
}