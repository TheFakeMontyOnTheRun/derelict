package br.odb.core;

import org.junit.Assert;
import org.junit.Test;

import br.odb.derelict.core.Astronaut;
import br.odb.derelict.core.commands.DropCommand;
import br.odb.derelict.core.items.MagneticBoots;
import br.odb.derelict.core.items.MetalPlate;
import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.gameworld.Location;
import br.odb.gameworld.exceptions.InventoryManipulationException;
import br.odb.gameworld.exceptions.ItemNotFoundException;

/**
 * @author monty
 * 
 */
public class DropCommandTest {

	@Test
	public final void test() {
		TotautisSpaceStation testPlace = new TotautisSpaceStation();
		Location test = testPlace.addNewLocation("location");
		Astronaut astro = new Astronaut();
		test.addCharacter(astro);
		MetalPlate plate = new MetalPlate();
		MagneticBoots boots = new MagneticBoots();
		try {
			astro.addItem(plate);
			astro.addItem(boots);
		} catch (InventoryManipulationException e) {
			Assert.fail();
		}
		DropCommand cmd = new DropCommand();
		DummyApplicationClient client = new DummyApplicationClient();
		Assert.assertTrue(astro.hasItem("metal-plate"));
		try {
			cmd.run(testPlace, astro, "metal-plate", client);
			Assert.assertFalse(astro.hasItem("metal-plate"));
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}
		Assert.assertTrue(astro.hasItem(boots.getName()));
		try {
			cmd.run(testPlace, astro, "all", client);
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}
		Assert.assertFalse(astro.hasItem(boots.getName()));
	}
}
