package de.dhbw.ledcontroller.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LampListResponse {

    private List<LampRequestResponse> lamps;

}