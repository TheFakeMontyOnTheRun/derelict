/**
 * 
 */
package br.odb.derelict.core.locations;

import java.util.ArrayList;
import java.util.HashMap;

import br.odb.gameworld.Location;
import br.odb.gameworld.Place;

/**
 * @author monty
 * 
 */
public class Floor extends Location {

	HashMap<Location, Floor> connections = new HashMap<Location, Floor>();
	ArrayList<Location> contained = new ArrayList<Location>();
	private Place place;

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
