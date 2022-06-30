package br.odb.derelict.core.commands;

import org.jetbrains.annotations.NotNull;

import br.odb.gameapp.ApplicationClient;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Place;

public class QuitCommand extends DerelictUserMetaCommandLineAction {

	@Override
	public void run(Place level, CharacterActor actor, String operand,
					ApplicationClient client) {

		client.sendQuit();
	}

	@Override
	public int requiredOperands() {
		return 0;
	}

	@NotNull
	@Override
	public String toString() {
		return "quit";
	}

	@Override
	public String getDescription() {
		return "- Quit the game and (if applicable) send highscore to the online leaderboard.";
	}
}
