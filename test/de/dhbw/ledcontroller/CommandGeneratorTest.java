package de.dhbw.ledcontroller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.dhbw.ledcontroller.connection.CommandGenerator;

public class CommandGeneratorTest {

	@Test
	void test() {
		assertThat(CommandGenerator.colorRGB(1, 2, 3), is("F1 1 2 3"));
		assertThat(CommandGenerator.colorRGBW(1, 2, 3, 4), is("F2 1 2 3 4"));
		assertThat(CommandGenerator.colorNeoRGB(1, 2, 3), is("X1 1 2 3"));
		assertThat(CommandGenerator.off(), is("OFF"));
	}

}
