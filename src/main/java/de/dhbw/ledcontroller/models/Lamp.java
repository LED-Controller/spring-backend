package de.dhbw.ledcontroller.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import org.springframework.lang.NonNull;

import de.dhbw.ledcontroller.payload.LightType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Lamp {
	@Id
	@Column(columnDefinition = "TEXT")
	private String mac;

	@NonNull
	@Column(columnDefinition = "TEXT")
	private String name;

	@NonNull
	@Enumerated(EnumType.STRING)
	private LightType type;

	@NonNull
	@Column(columnDefinition = "INTEGER")
	private int red;

	@NonNull
	@Column(columnDefinition = "INTEGER")
	private int green;

	@NonNull
	@Column(columnDefinition = "INTEGER")
	private int blue;

	@NonNull
	@Column(columnDefinition = "INTEGER")
	private int brightness;

	@NonNull
	@Column(columnDefinition = "BOOLEAN")
	private boolean isOn;

}