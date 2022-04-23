package de.dhbw.ledcontroller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.Test;

import de.dhbw.ledcontroller.color.ColorController;
import de.dhbw.ledcontroller.payload.LedColorRGB;
import de.dhbw.ledcontroller.payload.LedColorRGBW;

public class ColorControllerTest {

    @Test
    void testWhiteRGBToWhiteRGBW() {
        LedColorRGB rgbWhite = new LedColorRGB(255, 255, 255);
        LedColorRGBW rgbwWhite = new LedColorRGBW(0, 0, 0, 255);
        assertThat(ColorController.convertRGBToRGBW(rgbWhite), is(rgbwWhite));
    }

    @Test
    void testCorrectBrightness() {
        LedColorRGB rgb = new LedColorRGB(200, 255, 255);
        int brightness = 50;
        LedColorRGB rgbCorrected = new LedColorRGB(100, 128, 128);
        assertThat(ColorController.adjustBrightness(rgb, brightness), is(rgbCorrected));
    }
}
