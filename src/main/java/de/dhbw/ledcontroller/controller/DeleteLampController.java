package de.dhbw.ledcontroller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import de.dhbw.ledcontroller.models.Lamp;
import de.dhbw.ledcontroller.payload.response.MessageResponse;
import de.dhbw.ledcontroller.repositories.LampRepository;
import de.dhbw.ledcontroller.util.ResponseType;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class DeleteLampController {
	@Autowired
	LampRepository lampRepository;

	@DeleteMapping("/delete/{mac}")
	public ResponseEntity<?> deleteLamp(@PathVariable String mac) {
		if (lampRepository.findByMac(mac).isPresent()) {
			Lamp lamp = lampRepository.findByMac(mac).get();
			lampRepository.delete(lamp);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.badRequest().body(new MessageResponse("lamp not found", ResponseType.ERROR));
	}
}
