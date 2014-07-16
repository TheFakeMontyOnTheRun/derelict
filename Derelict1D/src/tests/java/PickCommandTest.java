package br.odb.derelict.test;

import org.junit.Assert;
import org.junit.Test;

import br.odb.derelict.core.Astronaut;
import br.odb.derelict.core.commands.PickCommand;
import br.odb.derelict.core.items.MagneticBoots;
import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.gameworld.CharacterIsNotMovableException;
import br.odb.gameworld.exceptions.DoorActionException;
import br.odb.gameworld.exceptions.InvalidLocationException;
import br.odb.gameworld.exceptions.InvalidSlotException;
import br.odb.gameworld.exceptions.InventoryManipulationException;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;
import br.odb.gameworld.exceptions.ItemNotFoundException;

public class PickCommandTest {

	@Test
	public final void test() {
		TotautisSpaceStation testPlace = new TotautisSpaceStation();
		Astronaut astro = testPlace.getAstronaut();
		PickCommand cmd = new PickCommand();
		DummyApplicationClient client = new DummyApplicationClient();
		Assert.assertFalse(astro.hasItem("metal-plate"));

		try {
			cmd.run(testPlace, astro, "all", client);
			((MagneticBoots) astro.getItem("magboots")).toggle();
			testPlace.moveCharacter(astro.getName(), "hangar");
			testPlace.getLocation("hangar").getItem("metal-plate")
					.setPickable(true);
			cmd.run(testPlace, astro, "metal-plate", client);
		} catch (InventoryManipulationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		} catch (ItemActionNotSupportedException e) {
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
		Assert.assertTrue(astro.hasItem("metal-plate"));
	}
}
