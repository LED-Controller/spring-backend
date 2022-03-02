package de.dhbw.ledcontroller.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LedColor {

	int r = -1;
	int g = -1;
	int b = -1;
	int w = -1;

}
