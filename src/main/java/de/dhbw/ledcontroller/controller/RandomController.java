package de.dhbw.ledcontroller.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.dhbw.ledcontroller.models.Lamp;
import de.dhbw.ledcontroller.payload.LightType;
import de.dhbw.ledcontroller.payload.response.MessageResponse;
import de.dhbw.ledcontroller.repositories.LampRepository;
import de.dhbw.ledcontroller.util.ResponseType;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class RandomController {
	@Autowired
	LampRepository lampRepository;

	@PostMapping("/random")
	public ResponseEntity<?> random(@Valid @RequestBody String mac) {
		if (lampRepository.findByMac(mac).isPresent()) {
			Lamp lamp = lampRepository.findByMac(mac).get();

			int r = rand(30, 255);
			int g = rand(30, 255);
			int b = rand(30, 255);
			int w = rand(30, 255);

			String cmd = ControllerService.getColorCmd(r, g, b, w, lamp);

			boolean success = ControllerService.sendDataToController(mac, cmd);
			if (success) {
				// Farbe geändert, Objekt jetzt noch manipulieren
				if (lamp.getType() == LightType.RGBW) {
					lamp = ControllerService.changeColor(r, g, b, w, lamp);
				} else {
					lamp = ControllerService.changeColor(r, g, b, lamp);
				}
				lampRepository.save(lamp);
				return ResponseEntity.ok(ControllerService.generateLampResponseFromLamp(lamp));
			}
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.badRequest().body(new MessageResponse("lamp not found", ResponseType.ERROR));
	}

	private int rand(int min, int max) {
		int range = max - min + 1;
		return (int) (Math.random() * range) + min;
	}
}