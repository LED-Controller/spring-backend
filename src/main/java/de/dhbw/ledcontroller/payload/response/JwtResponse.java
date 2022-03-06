package de.dhbw.ledcontroller.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
	private String token;
	private final String type = "Bearer";
	private int id;
	private String username;
}