package br.odb.derelict.core.commands;

import org.jetbrains.annotations.NotNull;

import br.odb.gameapp.ApplicationClient;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Place;

public class ItemsCommand extends DerelictUserMetaCommandLineAction {

	@Override
	public void run(Place level, CharacterActor actor, String operand,
					ApplicationClient client) {
	}

	@NotNull
	@Override
	public String toString() {
		return "items";
	}

	@Override
	public int requiredOperands() {
		return 0;
	}

	@Override
	public String getDescription() {
		return "- list all items currently held.";
	}

}
