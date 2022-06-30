package br.odb.core;

import org.junit.Assert;
import org.junit.Test;

import br.odb.derelict.core.Astronaut;
import br.odb.derelict.core.commands.ToggleCommand;
import br.odb.derelict.core.items.MagneticBoots;
import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.gameworld.Location;
import br.odb.gameworld.exceptions.InventoryManipulationException;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;
import br.odb.gameworld.exceptions.ItemNotFoundException;

public class ToggleCommandTest {

	@Test
	public final void test() {
		TotautisSpaceStation testPlace = new TotautisSpaceStation();
		Location test = testPlace.addNewLocation("location");
		Astronaut astro = new Astronaut();
		test.addCharacter(astro);
		MagneticBoots boots = new MagneticBoots();
		try {
			astro.addItem(boots);
		} catch (InventoryManipulationException e1) {
			Assert.fail();
		}
		test.addItem(boots);
		ToggleCommand cmd = new ToggleCommand();
		DummyApplicationClient client = new DummyApplicationClient();
		Assert.assertFalse(boots.isActive());

		try {
			cmd.run(testPlace, astro, boots.getName(), client);
		} catch (ItemActionNotSupportedException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}
		Assert.assertTrue(boots.isActive());

		try {
			cmd.run(testPlace, astro, boots.getName(), client);
		} catch (ItemActionNotSupportedException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}
		Assert.assertFalse(boots.isActive());		
	}
}
