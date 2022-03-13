package de.dhbw.ledcontroller;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;
import de.dhbw.ledcontroller.connection.CommandGenerator;
import de.dhbw.ledcontroller.payload.LedColorRGB;
import de.dhbw.ledcontroller.payload.LedColorRGBW;

public class CommandTest {

	@Test
	void test() {
		assertThat(CommandGenerator.colorRGB(new LedColorRGB(1, 2, 3)), is("F1 1 2 3"));
		assertThat(CommandGenerator.colorRGBW(new LedColorRGBW(1, 2, 3, 4)), is("F2 1 2 3 4"));
		assertThat(CommandGenerator.colorNeoRGB(new LedColorRGB(1, 2, 3)), is("X1 1 2 3"));
		assertThat(CommandGenerator.off(), is("OFF"));
	}

}
