package de.dhbw.ledcontroller.payload.request;

import de.dhbw.ledcontroller.payload.EffectType;
import lombok.Data;

@Data
public class EffectRequest {

	private String mac;
	private EffectType effectType;
}