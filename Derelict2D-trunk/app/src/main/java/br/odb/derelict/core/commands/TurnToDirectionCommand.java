package br.odb.derelict.core.commands;

import br.odb.derelict.core.Astronaut;
import br.odb.gameapp.ApplicationClient;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Place;
import br.odb.gameutils.Direction;

public final class TurnToDirectionCommand extends DerelictUserMoveCommandLineAction {

	@Override
	public void run(Place level, CharacterActor actor, String operand,
			ApplicationClient client) {
		
		Direction d;
		Astronaut hero = (Astronaut) actor;

		if ((d = Direction.getDirectionForSimpleName(operand)) != null) {
			hero.direction = d;
		} else if ((d = Direction.getDirectionForPrettyName(operand)) != null) {
			hero.direction = d;
		}
	}

	@Override
	public String toString() {
		return "turnTo";
	}
	
	@Override
	public String getDescription() {
		return "<direction name or direction initial letter> - turn player to said direction.";
	}

	@Override
	public int requiredOperands() {
		return 1;
	}
	
}
