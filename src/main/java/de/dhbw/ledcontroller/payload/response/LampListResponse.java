package de.dhbw.ledcontroller.payload.response;

import java.util.List;

import de.dhbw.ledcontroller.models.Lamp;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LampListResponse {
	private List<Lamp> lamps;
}
