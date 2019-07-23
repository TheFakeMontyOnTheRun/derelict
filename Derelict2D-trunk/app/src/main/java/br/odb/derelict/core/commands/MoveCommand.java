package br.odb.derelict.core.commands;

import br.odb.derelict.core.Astronaut;
import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.gameapp.ApplicationClient;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.CharacterIsNotMovableException;
import br.odb.gameworld.Place;
import br.odb.gameworld.exceptions.DoorActionException;
import br.odb.gameworld.exceptions.InvalidLocationException;
import br.odb.gameworld.exceptions.InvalidSlotException;
import br.odb.gameutils.Direction;

public class MoveCommand extends DerelictUserMoveCommandLineAction {

	@Override
	public void run(Place level, CharacterActor actor, String operand,
			ApplicationClient client) throws CharacterIsNotMovableException,
			InvalidLocationException, InvalidSlotException, DoorActionException {

		Direction d;
		TotautisSpaceStation station = (TotautisSpaceStation) level;
		Astronaut hero = (Astronaut) actor;

		if ((d = Direction.getDirectionForSimpleName(operand)) != null) {
			station.moveCharacter(hero.getName(), d);
			hero.direction = d;
		} else if ((d = Direction.getDirectionForPrettyName(operand)) != null) {
			station.moveCharacter(hero.getName(), d);
			hero.direction = d;
		} else if (hero.getLocation().hasConnection(operand)) {
			station.moveCharacter(hero.getName(), operand);
//			d = hero.getLocation().getConnectionDirectionForLocation( station.getLocation( operand ) );
//			hero.direction = d;
		} else {
			throw new InvalidLocationException();
		}
	}

	@Override
	public String toString() {
		return "move"; 
	}

	@Override
	public String getDescription() {
		return "<room name, direction name or direction initial letter> - walk into said room, if possible.";
	}

	@Override
	public int requiredOperands() {
		return 1;
	}
}
