package br.odb.derelict.core.locations;

import br.odb.gameworld.Location;

public class Spaceship extends Location {

	int crew;

	float fuel;
	float fuelCapacity;
	public boolean operative;
	float thrust;
	float tonnage;

	Spaceship(String shipName) {
		super(shipName);
	}
}
