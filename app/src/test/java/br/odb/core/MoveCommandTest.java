package br.odb.core;

import org.junit.Assert;
import org.junit.Test;

import br.odb.derelict.core.Astronaut;
import br.odb.derelict.core.Clearance;
import br.odb.derelict.core.commands.MoveCommand;
import br.odb.derelict.core.items.KeyCard;
import br.odb.derelict.core.items.MagneticBoots;
import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.gameworld.CharacterIsNotMovableException;
import br.odb.gameworld.Location;
import br.odb.gameworld.exceptions.DoorActionException;
import br.odb.gameworld.exceptions.InvalidLocationException;
import br.odb.gameworld.exceptions.InvalidSlotException;
import br.odb.gameworld.exceptions.InventoryManipulationException;
import br.odb.gameworld.exceptions.ItemNotFoundException;

/**
 * @author monty
 * 
 */
public class MoveCommandTest {

	@Test
	public final void test() {

		TotautisSpaceStation testPlace = new TotautisSpaceStation();
		Location test = null;

		try {
			test = testPlace.getLocation("LSS DAEDALUS");
		} catch (InvalidLocationException e) {
			Assert.fail();
		}

		Astronaut astro = testPlace.getAstronaut();
		MoveCommand cmd = new MoveCommand();
		KeyCard card = new KeyCard(Clearance.HIGH_RANK);
		DummyApplicationClient client = new DummyApplicationClient();
		try {

			astro.addItem(card);
			astro.addItem(test.getItem("magboots"));
			((MagneticBoots) astro.getItem("magboots")).toggle();
			cmd.run(testPlace, astro, "hangar", client);
			cmd.run(testPlace, astro, "North", client);
			cmd.run(testPlace, astro, "E", client);
		} catch (ItemNotFoundException e) {
			Assert.fail();
		} catch (InventoryManipulationException e) {
			Assert.fail();
		} catch (CharacterIsNotMovableException e) {
			Assert.fail();
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (InvalidSlotException e) {
			Assert.fail();
		} catch (DoorActionException e) {
			Assert.fail();
		}
	}
}
