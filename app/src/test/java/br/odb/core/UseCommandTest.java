package br.odb.core;

import org.junit.Assert;
import org.junit.Test;

import br.odb.derelict.core.Astronaut;
import br.odb.derelict.core.commands.UseCommand;
import br.odb.derelict.core.items.TimeBomb;
import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.gameworld.Location;
import br.odb.gameworld.exceptions.InventoryManipulationException;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;
import br.odb.gameworld.exceptions.ItemNotFoundException;

/**
 * @author monty
 * 
 */
public class UseCommandTest {

	@Test
	public final void test() {
		TotautisSpaceStation testPlace = new TotautisSpaceStation();
		Location test = testPlace.addNewLocation("location");
		Astronaut astro = new Astronaut();
		test.addCharacter(astro);
		TimeBomb bomb = new TimeBomb(1);

		try {
			astro.addItem(bomb);
		} catch (InventoryManipulationException e1) {
			Assert.fail();
		}
		test.addItem(bomb);
		UseCommand cmd = new UseCommand();
		DummyApplicationClient client = new DummyApplicationClient();
		Assert.assertFalse(bomb.isDepleted());

		try {
			cmd.run(testPlace, astro, bomb.getName(), client);
			bomb.update(10);
		} catch (ItemNotFoundException e) {
			Assert.fail();
		} catch (ItemActionNotSupportedException e) {
			Assert.fail();
		}

		Assert.assertTrue(bomb.isDepleted());
	}

}
