package de.dhbw.ledcontroller.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.dhbw.ledcontroller.models.User;
import de.dhbw.ledcontroller.payload.request.LoginSignupRequest;
import de.dhbw.ledcontroller.payload.response.JwtResponse;
import de.dhbw.ledcontroller.payload.response.MessageResponse;
import de.dhbw.ledcontroller.payload.response.PasswordStatusResponse;
import de.dhbw.ledcontroller.repositories.UserRepository;
import de.dhbw.ledcontroller.security.jwt.JwtUtils;
import de.dhbw.ledcontroller.security.services.UserDetailsImpl;
import de.dhbw.ledcontroller.util.ResponseType;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/login")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginSignupRequest loginRequest) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken("user", loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername()));
	}

	@PostMapping("/password")
	public ResponseEntity<?> registerUser(@Valid @RequestBody LoginSignupRequest signUpRequest) {
		if (userRepository.existsByName("user")) {
			return ResponseEntity.badRequest().body(new MessageResponse("password already set", ResponseType.INFO));
		}

		User user = new User("user", encoder.encode(signUpRequest.getPassword()));
		userRepository.save(user);
		return authenticateUser(signUpRequest);
	}

	@GetMapping("/passwordstatus")
	public ResponseEntity<?> passwordIsAlreadySet() {
		if (userRepository.existsByName("user")) {
			return ResponseEntity.ok(new PasswordStatusResponse(true));
		}
		return ResponseEntity.ok(new PasswordStatusResponse(false));
	}
}