package de.dhbw.ledcontroller.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordStatusResponse {
	private boolean passwordIsAlreadySet;
}
