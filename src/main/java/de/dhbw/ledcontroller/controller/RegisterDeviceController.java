package de.dhbw.ledcontroller.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.dhbw.ledcontroller.color.ColorController;
import de.dhbw.ledcontroller.connection.CommandGenerator;
import de.dhbw.ledcontroller.models.Lamp;
import de.dhbw.ledcontroller.payload.LampRequestResponse;
import de.dhbw.ledcontroller.payload.response.MessageResponse;
import de.dhbw.ledcontroller.repositories.LampRepository;
import de.dhbw.ledcontroller.util.ResponseType;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class RegisterDeviceController {
	@Autowired
	LampRepository lampRepository;

	@PostMapping("/register")
	public ResponseEntity<?> registerLamp(@Valid @RequestBody LampRequestResponse request) {
		if (!lampRepository.findByMac(request.getMac()).isPresent()) {
			Lamp lamp = ControllerService.editLampFromRequest(new Lamp(), request);
			lampRepository.save(lamp);

			// updateLamp
			ColorController.correctRequest(request);

			String cmd = ControllerService.getColorCmd(request.getColor(), lamp);

			boolean successColor = true;
			if (request.isOn()) {
				successColor = ControllerService.sendDataToController(request.getMac(), cmd);
			}

			boolean successOnState = true;
			if (!request.isOn())
				successOnState = ControllerService.sendDataToController(request.getMac(), CommandGenerator.off());
			if (successColor && successOnState) {
				lamp = ControllerService.editLampFromRequest(lamp, request);
			}

			return ResponseEntity.ok().build();
		}
		return ResponseEntity.badRequest().body(new MessageResponse("lamp already registered", ResponseType.ERROR));
	}
}
