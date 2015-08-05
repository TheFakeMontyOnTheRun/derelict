/**
 * 
 */
package br.odb.derelict.core;

import br.odb.gameworld.Location;
import br.odb.utils.Direction;

/**
 * @author monty
 * 
 */
public class SecuredLocation extends Location {

	public Clearance rank = Clearance.DEFAULT_CLEARANCE;

	public SecuredLocation(String name) {
		super(name);
	}

	@Override
	public Location setConnected(Direction d, Location location) {
		return setConnected(d, location, rank);
	}

	public Location setConnected(Direction slot, Location location,
			Clearance clearance) {
		
		
		getConnections()[slot.ordinal()] = location;
		getDoors()[slot.ordinal()] = new SecuredDoor(clearance);

		return this;
	}

	public void setRank(Clearance rank) {
		this.rank = rank;

		for (Direction d : Direction.values()) {

			if (getDoors()[d.ordinal()] != null) {

				getDoors()[d.ordinal()] = new SecuredDoor(rank);
			}
		}
	}
}
