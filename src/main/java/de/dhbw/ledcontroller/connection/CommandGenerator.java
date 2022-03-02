package de.dhbw.ledcontroller.connection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommandGenerator {

	private static final String OFF = "OFF";
	private static final String R_G_B = "{r} {g} {b}";
	private static final String R_G_B_W = R_G_B + " {w}";

	private static final String NORMAL_FILL = "F1 " + R_G_B;
	private static final String NORMAL_FILL_W = "F2 " + R_G_B_W;
	private static final String NEO_FILL = "X1 " + R_G_B;

	public String off() {
		return OFF;
	}

	public String colorRGB(int r, int g, int b) {
		return replace(replace(replace(NORMAL_FILL, 'r', r), 'g', g), 'b', b);
	}

	public String colorRGBW(int r, int g, int b, int w) {
		return replace(replace(replace(replace(NORMAL_FILL_W, 'r', r), 'g', g), 'b', b), 'w', w);
	}

	public String colorNeoRGB(int r, int g, int b) {
		return replace(replace(replace(NEO_FILL, 'r', r), 'g', g), 'b', b);
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

	@Test
	void test() {
		assertThat(CommandGenerator.colorRGB(1, 2, 3), is("F1 1 2 3"));
		assertThat(CommandGenerator.colorRGBW(1, 2, 3, 4), is("F2 1 2 3 4"));
		assertThat(CommandGenerator.colorNeoRGB(1, 2, 3), is("X1 1 2 3"));
		assertThat(CommandGenerator.off(), is("OFF"));
	}

}
