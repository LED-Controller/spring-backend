package de.dhbw.ledcontroller.controller;

import java.util.Optional;

import org.springframework.stereotype.Service;

import de.dhbw.ledcontroller.color.ColorController;
import de.dhbw.ledcontroller.connection.CommandGenerator;
import de.dhbw.ledcontroller.connection.LightStripConnection;
import de.dhbw.ledcontroller.models.Lamp;
import de.dhbw.ledcontroller.payload.LampRequestResponse;
import de.dhbw.ledcontroller.payload.LedColorRGB;

@Service
public class ControllerService {
	public static boolean sendDataToController(String mac, String data) {
		Optional<LightStripConnection> opt = LightStripConnection.connectionList.stream().filter(c -> c.getMac().equals(mac)).findAny();
		if (opt.isPresent()) {
			opt.get().sendToStrip(data);
			return true;
		}
		return false;
	}

	public static Lamp changeColor(LedColorRGB rgb, Lamp lamp) {
		lamp.setRed(rgb.getR());
		lamp.setGreen(rgb.getG());
		lamp.setBlue(rgb.getB());
		return lamp;
	}

	public static LampRequestResponse generateLampResponseFromLamp(Lamp lamp) {
		boolean lampIsOnline = LightStripConnection.connectionList.stream().filter(c -> c.getMac().equals(lamp.getMac())).findAny().isPresent();
		return new LampRequestResponse(lamp.getMac(), lamp.getName(), lamp.getType(), new LedColorRGB(lamp.getRed(), lamp.getGreen(), lamp.getBlue()), lamp.getBrightness(), lamp.isOn(), lampIsOnline);
	}

	public static Lamp editLampFromRequest(Lamp lamp, LampRequestResponse request) {
		lamp.setMac(request.getMac());
		lamp.setName(request.getName());
		lamp.setType(request.getType());
		lamp.setRed(request.getColor().getR());
		lamp.setGreen(request.getColor().getG());
		lamp.setBlue(request.getColor().getB());
		lamp.setBrightness(request.getBrightness());
		lamp.setOn(request.isOn());
		return lamp;
	}

	public static String getColorCmd(LedColorRGB rgb, Lamp lamp) {
		switch (lamp.getType()) {
		case RGB:
			return CommandGenerator.colorRGB(rgb);
		case NEOPIXEL:
			return CommandGenerator.colorNeoRGB(rgb);
		case RGBW:
			return CommandGenerator.colorRGBW(ColorController.convertRGBToRGBW(rgb));
		}
		return null;
	}
}