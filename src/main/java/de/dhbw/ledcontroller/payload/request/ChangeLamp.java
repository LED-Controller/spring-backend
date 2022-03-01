package de.dhbw.ledcontroller.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeLamp {
    private int id;
    private String name;
    private String modell;
    private boolean isOn;
    private String color;
    private int brightness;
    private String room;
}