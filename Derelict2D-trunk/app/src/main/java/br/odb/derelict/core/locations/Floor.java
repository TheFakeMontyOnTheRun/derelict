package br.odb.derelict.core.locations;

import java.util.ArrayList;
import java.util.HashMap;

import br.odb.gameworld.Location;
import br.odb.gameworld.Place;

/**
 * @author monty
 * 
 */
class Floor extends Location {

	HashMap<Location, Floor> connections = new HashMap<>();
	ArrayList<Location> contained = new ArrayList<>();
	private final Place place;

	public Floor(String name, Place place) {
		super(name);
		this.place = place;
	}

	public Location addLocation(Location location) {
		return place.addLocation(location).setFloorId( super.getName() );
	}

	public Location addNewLocation(String name) {
		return place.addNewLocation(name).setFloorId( super.getName() );
	}
}
