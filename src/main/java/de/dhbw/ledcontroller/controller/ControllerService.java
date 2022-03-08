package de.dhbw.ledcontroller.controller;

import java.util.Optional;

import org.springframework.stereotype.Service;

import de.dhbw.ledcontroller.connection.CommandGenerator;
import de.dhbw.ledcontroller.connection.LightStripConnection;
import de.dhbw.ledcontroller.models.Lamp;
import de.dhbw.ledcontroller.payload.LampRequestResponse;
import de.dhbw.ledcontroller.payload.LedColor;

@Service
public class ControllerService {
    public static boolean sendDataToController(String mac, String data) {
        Optional<LightStripConnection> opt = LightStripConnection.connectionList.stream()
                .filter(c -> c.getMac().equals(mac)).findAny();
        if (opt.isPresent()) {
            opt.get().sendToStrip(data);
            return true;
        }
        return false;
    }

    public static Lamp changeColor(int r, int g, int b, Lamp lamp) {
        lamp.setRed(r);
        lamp.setGreen(g);
        lamp.setBlue(b);
        return lamp;
    }

    public static Lamp changeColor(int r, int g, int b, int w, Lamp lamp) {
        lamp.setRed(r);
        lamp.setGreen(g);
        lamp.setBlue(b);
        lamp.setWhite(w);
        return lamp;
    }

    public static LampRequestResponse generateLampResponseFromLamp(Lamp lamp) {
        return new LampRequestResponse(lamp.getMac(), lamp.getName(), lamp.getArt(),
                new LedColor(lamp.getRed(), lamp.getGreen(), lamp.getBlue(), lamp.getWhite()), lamp.getBrightness(),
                lamp.isOn(), lamp.isOnline());
    }

    public static Lamp editLampFromRequest(Lamp lamp, LampRequestResponse request) {
        lamp.setMac(request.getMac());
        lamp.setName(request.getName());
        lamp.setArt(request.getArt());
        lamp.setRed(request.getColor().getR());
        lamp.setGreen(request.getColor().getG());
        lamp.setBlue(request.getColor().getB());
        lamp.setWhite(request.getColor().getW());
        lamp.setBrightness(request.getBrightness());
        lamp.setOn(request.isOn());
        lamp.setOnline(request.isOnline());
        return lamp;
    }

    public static String getColorCmd(int r, int g, int b, int w, Lamp lamp) {
        switch (lamp.getArt()) {
            case RGB:
                return CommandGenerator.colorRGB(r, g, b);
            case NEOPIXEL:
                return CommandGenerator.colorNeoRGB(r, g, b);
            case RGBW:
                return CommandGenerator.colorRGBW(r, g, b, w);
            default:
                return CommandGenerator.colorRGB(255, 255, 255);
        }
    }
}