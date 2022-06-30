package br.odb.derelict.core.commands;

import org.jetbrains.annotations.NotNull;

import br.odb.derelict.core.Astronaut;
import br.odb.gameapp.ApplicationClient;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Item;
import br.odb.gameworld.Place;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;
import br.odb.gameworld.exceptions.ItemNotFoundException;

public class UseCommand extends DerelictUserCommandLineAction {

	@Override
	public void run(Place level, CharacterActor actor, String operand,
					ApplicationClient client) throws ItemNotFoundException, ItemActionNotSupportedException {

		Astronaut hero = (Astronaut) actor;

		Item item = hero.useItem(operand);
		client.alert("item " + operand + " used");
		client.playMedia(item.getUseItemSound(), "*click*");
	}

	@NotNull
	@Override
	public String toString() {
		return "use";
	}

	@Override
	public String getDescription() {
		return "<currnetly held object name> - use said object (depending on context).";
	}

	@Override
	public int requiredOperands() {
		return 1;
	}
}
