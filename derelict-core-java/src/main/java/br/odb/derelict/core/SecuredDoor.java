package br.odb.derelict.core;

import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Door;
import br.odb.gameworld.exceptions.DoorActionException;

public class SecuredDoor extends Door {
	final public br.odb.derelict.core.Clearance clearance;

	public SecuredDoor(Clearance clearance) {
		super();
		this.clearance = clearance;
	}

	@Override
	public void openFor(CharacterActor character) throws DoorActionException {

		if (!willOpenFor((Astronaut) character)) {
			throw new ClearanceException();
		}

		super.openFor(character);
	}

	public boolean willOpenFor(Astronaut character) {
		return (character.getClearance().ordinal() >= clearance.ordinal());
	}

}
