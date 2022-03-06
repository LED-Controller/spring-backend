package de.dhbw.ledcontroller.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LedColor {
	public LedColor(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	int r = -1;
	int g = -1;
	int b = -1;
	int w = -1;

}
