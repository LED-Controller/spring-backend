package de.dhbw.ledcontroller.payload.response;

import de.dhbw.ledcontroller.util.ResponseType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponse {
	private String message;
	private ResponseType type;
}
