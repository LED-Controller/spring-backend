package de.dhbw.ledcontroller.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Lamp {

	private String mac; // (primary)
	private String name;
	private LightType art;
	private LedColor color;
	private int brightness;
	private boolean isOn;
	private boolean isOnline;

}