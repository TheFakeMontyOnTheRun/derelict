package br.odb.derelict.core.commands;

import br.odb.derelict.core.Astronaut;
import br.odb.gameapp.ApplicationClient;
import br.odb.gameworld.ActiveItem;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Place;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;
import br.odb.gameworld.exceptions.ItemNotFoundException;

public class ToggleCommand extends DerelictUserCommandLineAction {

    @Override
    public void run(Place level, CharacterActor actor, String operand,
                    ApplicationClient client) throws ItemActionNotSupportedException,
            ItemNotFoundException {

        Astronaut hero = (Astronaut) actor;

        ActiveItem item = hero.toggleItem(operand);
        client.alert("item " + operand + " toggled");
        String sound = item.isActive() ? item.getTurnOnSound() : item.getTurnOffSound();
        client.playMedia(sound, sound);
    }

    @Override
    public String toString() {
        return "toggle";
    }

    @Override
    public String getDescription() {
        return "<currently held object name> - activate or de-activate said object.";
    }

    @Override
    public int requiredOperands() {
        return 1;
    }
}
