/**
 * 
 */
package br.odb.derelict.core;

import br.odb.gameworld.Direction;
import br.odb.gameworld.Location;

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

		connections[slot.ordinal()] = location;
		door[slot.ordinal()] = new SecuredDoor(clearance);

		return this;
	}

	public void setRank(Clearance rank) {
		this.rank = rank;

		for (Direction d : Direction.values()) {

			if (door[d.ordinal()] != null) {

				door[d.ordinal()] = new SecuredDoor(rank);
			}
		}
	}
}
