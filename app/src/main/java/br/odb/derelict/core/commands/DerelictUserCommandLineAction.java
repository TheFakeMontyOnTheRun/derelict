package br.odb.derelict.core.commands;

import br.odb.derelict.core.DerelictGame;
import br.odb.gameapp.ApplicationClient;
import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Place;


public abstract class DerelictUserCommandLineAction extends
		UserCommandLineAction {

	@Override
	public void run(ConsoleApplication application, String operand)
			throws Exception {

		DerelictGame game = (DerelictGame) application;
		Place place = game.station;
		CharacterActor actor = game.hero;
		ApplicationClient client = game.getClient();

		run(place, actor, operand, client);

	}


	protected abstract void run(Place level, CharacterActor actor, String operand,
								ApplicationClient client) throws Exception;
}
