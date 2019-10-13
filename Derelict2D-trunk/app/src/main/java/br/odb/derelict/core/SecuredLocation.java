package br.odb.derelict.core;

import br.odb.gameutils.Direction;
import br.odb.gameworld.Location;

public class SecuredLocation extends Location {

	private final Clearance rank = Clearance.DEFAULT_CLEARANCE;

	public SecuredLocation(String name) {
		super(name);
	}

	@Override
	public void setConnected(Direction d, Location location) {
		setConnected(d, location, rank);
	}

	public void setConnected(Direction slot, Location location,
							 Clearance clearance) {


		getConnections()[slot.ordinal()] = location;
		getDoors()[slot.ordinal()] = new SecuredDoor(clearance);

	}
}
