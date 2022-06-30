package br.odb.derelict.core.locations;

import br.odb.gameworld.Location;
import br.odb.gameworld.Place;

class Floor extends Location {

	private final Place place;

	public Floor(String name, Place place) {
		super(name);
		this.place = place;
	}

	public Location addLocation(Location location) {
		return place.addLocation(location).setFloorId(super.getName());
	}

	public Location addNewLocation(String name) {
		return place.addNewLocation(name).setFloorId(super.getName());
	}
}
