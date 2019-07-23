package br.odb.derelict.core.commands;

import br.odb.derelict.core.Astronaut;
import br.odb.gameapp.ApplicationClient;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Item;
import br.odb.gameworld.Place;
import br.odb.gameworld.exceptions.InventoryManipulationException;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;
import br.odb.gameworld.exceptions.ItemNotFoundException;

public class PickCommand extends DerelictUserMoveCommandLineAction {

	@Override
	public void run(Place level, CharacterActor actor, String operand,
			ApplicationClient client) throws InventoryManipulationException,
			ItemNotFoundException, ItemActionNotSupportedException {

		Astronaut hero = (Astronaut) actor;

		if (operand.equalsIgnoreCase("all")) { 

			for (Item i : hero.getLocation().getCollectableItems()) {

				hero.getLocation().giveItemTo(i.getName(), hero);
			}
			client.alert("picked all items"); 

		} else {
			Item item = hero.getLocation().giveItemTo(operand, hero);
			client.alert( "item " + operand + " picked" );
			client.playMedia( item.getPickSound(), "*click*" );
		}

	}

	@Override
	public String toString() {
		return "pick"; 
	}
	
	@Override
	public String getDescription() {
		return "<item name> - pick object present into the room.";
	}

	@Override
	public int requiredOperands() {
		return 1;
	}	
}
