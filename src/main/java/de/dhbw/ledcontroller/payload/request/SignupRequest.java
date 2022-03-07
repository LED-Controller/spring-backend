package de.dhbw.ledcontroller.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class SignupRequest {
	@NotBlank
	@Size(min = 6, max = 40)
	private String password;
}