package de.dhbw.ledcontroller.connection;

import de.dhbw.ledcontroller.payload.LedColorRGB;
import de.dhbw.ledcontroller.payload.LedColorRGBW;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommandGenerator {

	private static final String OFF = "OFF";
	private static final String TEST = "OFF";
	private static final String R_G_B = "{r} {g} {b}";
	private static final String R_G_B_W = R_G_B + " {w}";

	private static final String NORMAL_FILL = "F1 " + R_G_B;
	private static final String NORMAL_FILL_W = "F2 " + R_G_B_W;
	private static final String NEO_FILL = "X1 " + R_G_B;
	
	private static final String EFFECT = "EFFECT";

	public String off() {
		return OFF;
	}

	public String test() {
		return TEST;
	}
	
	public String effect(String effectName) {
		return EFFECT + " " + effectName;
	}

	public String colorRGB(LedColorRGB rgb) {
		return replace(replace(replace(NORMAL_FILL, 'r', rgb.getR()), 'g', rgb.getG()), 'b', rgb.getB());
	}

	public String colorRGBW(LedColorRGBW rgbw) {
		return replace(replace(replace(replace(NORMAL_FILL_W, 'r', rgbw.getR()), 'g', rgbw.getG()), 'b', rgbw.getB()), 'w', rgbw.getW());
	}

	public String colorNeoRGB(LedColorRGB rgb) {
		return replace(replace(replace(NEO_FILL, 'r', rgb.getR()), 'g', rgb.getG()), 'b', rgb.getB());
	}

	// util
	private String replace(String input, char placeholder, int replace) {
		return replace(input, placeholder, String.valueOf(replace));
	}

	private String replace(String input, char placeholder, String replace) {
		String toReplace = "{" + placeholder + "}";
		input = input.replace(toReplace, replace);
		return input;
	}

}
