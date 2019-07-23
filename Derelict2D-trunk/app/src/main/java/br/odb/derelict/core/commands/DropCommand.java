package br.odb.derelict.core.commands;

import br.odb.derelict.core.Astronaut;
import br.odb.gameapp.ApplicationClient;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Item;
import br.odb.gameworld.Place;
import br.odb.gameworld.exceptions.ItemNotFoundException;

public class DropCommand extends DerelictUserCommandLineAction {

    @Override
    public void run(Place level, CharacterActor actor, String operand,
                    ApplicationClient client) throws ItemNotFoundException {

        Astronaut hero = (Astronaut) actor;

        if (operand.equalsIgnoreCase("all")) {

            for (Item i : hero.getItems()) {

                hero.getLocation().takeItemFrom(i.getName(), hero);
            }
            client.alert("dropped all items");

        } else {

            Item item = hero.getLocation().takeItemFrom(operand, hero);
            client.alert("item " + operand + " dropped");
            client.playMedia(item.getDropSound(), "*click*");
        }
    }

    @Override
    public String toString() {
        return "drop";
    }

    @Override
    public int requiredOperands() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "<currently held item name> - drops the item in the current room.";
    }
}
