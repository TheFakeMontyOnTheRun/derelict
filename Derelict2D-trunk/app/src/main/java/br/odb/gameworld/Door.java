package br.odb.gameworld;

import br.odb.gameworld.exceptions.DoorActionException;

public class Door {

	private boolean open;

	public Door() {
		open = false;

	}

	private void doOpen() {
		open = true;
	}

	public void openFor(CharacterActor character) throws DoorActionException {
		doOpen();
	}
}
