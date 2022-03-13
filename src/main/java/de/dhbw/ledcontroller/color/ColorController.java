package de.dhbw.ledcontroller.color;

import java.awt.Color;

import de.dhbw.ledcontroller.payload.LedColorRGB;
import de.dhbw.ledcontroller.payload.LedColorRGBW;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ColorController {

	public LedColorRGB adjustBrightness(LedColorRGB rgb, int percent) {
		if (percent < 0 || percent > 100) {
			throw new RuntimeException("percent not in range");
		}

		float[] hsv = new float[3];
		Color.RGBtoHSB(rgb.getR(), rgb.getG(), rgb.getB(), hsv);

		float newBrightness = 0.01f * percent;
		hsv[2] = newBrightness;

		Color color = new Color(Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]));
		return new LedColorRGB(color.getRed(), color.getGreen(), color.getBlue());
	}

	public LedColorRGBW convertRGBToRGBW(LedColorRGB rgb) {
		int Ri = rgb.getR();
		int Gi = rgb.getG();
		int Bi = rgb.getB();

		float tM = Math.max(Ri, Math.max(Gi, Bi));
		if (tM == 0) {
			return new LedColorRGBW(0, 0, 0, 0);
		}

		float multiplier = 255.0f / tM;
		float hR = Ri * multiplier;
		float hG = Gi * multiplier;
		float hB = Bi * multiplier;

		float M = Math.max(hR, Math.max(hG, hB));
		float m = Math.min(hR, Math.min(hG, hB));
		float Luminance = ((M + m) / 2.0f - 127.5f) * (255.0f / 127.5f) / multiplier;

		int Wo = Math.round(Luminance);
		int Bo = Math.round(Bi - Luminance);
		int Ro = Math.round(Ri - Luminance);
		int Go = Math.round(Gi - Luminance);

		if (Wo < 0)
			Wo = 0;
		if (Bo < 0)
			Bo = 0;
		if (Ro < 0)
			Ro = 0;
		if (Go < 0)
			Go = 0;
		if (Wo > 255)
			Wo = 255;
		if (Bo > 255)
			Bo = 255;
		if (Ro > 255)
			Ro = 255;
		if (Go > 255)
			Go = 255;

		return new LedColorRGBW(Ro, Go, Bo, Wo);
	}

}
