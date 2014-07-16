/**
 * 
 */
package br.odb.derelict.tests;

import org.junit.Assert;
import org.junit.Test;

import br.odb.derelict.core.Astronaut;
import br.odb.derelict.core.commands.ExamineCommand;
import br.odb.derelict.core.items.MagneticBoots;
import br.odb.derelict.core.items.TimeBomb;
import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.gameworld.Location;
import br.odb.gameworld.exceptions.InventoryManipulationException;

/**
 * @author monty
 * 
 */
public class ExamineCommandTest {

	@Test
	public final void test() {
		TotautisSpaceStation testPlace = new TotautisSpaceStation();
		Location test = testPlace.addNewLocation("location");
		Astronaut astro = new Astronaut();
		test.addCharacter(astro);
		TimeBomb bomb = new TimeBomb(1);
		MagneticBoots boots = new MagneticBoots();

		try {
			astro.addItem(bomb);
			astro.addItem(boots);
		} catch (InventoryManipulationException e1) {
			Assert.fail();
		}
		test.addItem(bomb);
		ExamineCommand cmd = new ExamineCommand();
		DummyApplicationClient client = new DummyApplicationClient();

		String before = "" + client.buffer;
		try {
			cmd.run(testPlace, astro, boots.getName(), client);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertTrue(before.equals(client.buffer));
		}

		try {
			cmd.run(testPlace, astro, bomb.getName(), client);
		} catch (Exception e) {
			Assert.fail();
		}

		Assert.assertTrue(client.buffer.contains("time-limit"));
	}
}
