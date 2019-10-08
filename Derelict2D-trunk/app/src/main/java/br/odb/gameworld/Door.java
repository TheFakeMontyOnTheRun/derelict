package br.odb.gameworld;

import br.odb.gameutils.Updatable;
import br.odb.gameworld.exceptions.DoorActionException;

public class Door implements Updatable {

    private boolean open;

    public boolean isOpen() {
        return open;
    }

    private void doOpen() {
        open = true;
    }

    public Door() {
        open = false;

    }

    public void openFor(CharacterActor character) throws DoorActionException {

        doOpen();
    }

    @Override
    public void update(long milisseconds) {
        //TODO check for the idea of implementing the auto-closing mechanism here
    }


}
