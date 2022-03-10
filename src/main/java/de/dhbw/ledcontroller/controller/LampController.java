package de.dhbw.ledcontroller.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.dhbw.ledcontroller.models.Lamp;
import de.dhbw.ledcontroller.payload.LampRequestResponse;
import de.dhbw.ledcontroller.payload.response.MessageResponse;
import de.dhbw.ledcontroller.repositories.LampRepository;
import de.dhbw.ledcontroller.util.ResponseType;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/lamps")
public class LampController {
	@Autowired
	LampRepository lampRepository;

	@GetMapping("")
	public ResponseEntity<?> getAllLamps() {
		List<LampRequestResponse> lampResponses = new ArrayList<>();
		for (Lamp lamp : lampRepository.findAllByOrderByNameAsc()) {
			lampResponses.add(ControllerService.generateLampResponseFromLamp(lamp));
		}
		return ResponseEntity.ok(lampResponses);
	}

	@GetMapping("/{mac}")
	public ResponseEntity<?> getLamp(@PathVariable String mac) {
		if (lampRepository.findByMac(mac).isPresent()) {
			return ResponseEntity
					.ok(ControllerService.generateLampResponseFromLamp(lampRepository.findByMac(mac).get()));
		}
		return ResponseEntity.badRequest().body(new MessageResponse("lamp not found", ResponseType.ERROR));
	}
}