package br.odb.derelict.core.commands;

import org.jetbrains.annotations.NotNull;

import br.odb.derelict.core.Astronaut;
import br.odb.gameapp.ApplicationClient;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Item;
import br.odb.gameworld.Place;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;
import br.odb.gameworld.exceptions.ItemNotFoundException;

public class UseWithCommand extends DerelictUserCommandLineAction {

	@Override
	public void run(Place level, CharacterActor actor, String operand,
					ApplicationClient client) throws ItemNotFoundException {

		String[] tokens = operand.trim().split("[ ]+");
		Astronaut hero = (Astronaut) actor;

		try {
			Item item1 = hero.getItem(tokens[1]);
			Item item2 = hero.getLocation().getItem(tokens[0]);
			item2.useWith(item1);
			client.alert("item " + tokens[1] + " used on item " + tokens[0]);

			client.playMedia(item1.getUseItemSound(), "*click*");
			client.playMedia(item2.getUsedOnSound(), "*click*");

		} catch (ItemActionNotSupportedException e) {
			client.alert("item " + tokens[1] + " cannot be used on item " + tokens[0]);
		}
	}

	@NotNull
	@Override
	public String toString() {

		return "useWith";
	}

	@Override
	public int requiredOperands() {
		return 2;
	}

	@Override
	public String getDescription() {
		return "<object name present on current room> <currently held object name> - apply the latter object into the former, if possible.";
	}

}
