package de.dhbw.ledcontroller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.dhbw.ledcontroller.payload.response.LampListResponse;
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
		return ResponseEntity.ok(new LampListResponse(lampRepository.findAllByOrderByNameAsc()));
	}

	@GetMapping("/{mac}")
	public ResponseEntity<?> getLamp(@PathVariable String mac) {
		if (lampRepository.findByMac(mac).isPresent()) {
			return ResponseEntity.ok(lampRepository.findByMac(mac));
		}
		return ResponseEntity.badRequest().body(new MessageResponse("lamp not found", ResponseType.ERROR));
	}
}