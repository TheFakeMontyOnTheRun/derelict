/**
 * 
 */
package br.odb.core;

import org.junit.Assert;
import org.junit.Test;

import br.odb.derelict.core.Astronaut;
import br.odb.derelict.core.commands.ItemsCommand;
import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.gameworld.Item;
import br.odb.gameworld.Location;
import br.odb.gameworld.exceptions.InvalidLocationException;
import br.odb.gameworld.exceptions.InventoryManipulationException;

/**
 * @author monty
 * 
 */
public class ItemsCommandTest {

	@Test
	public final void test() {
		
		int initialCargo;
		
		TotautisSpaceStation testPlace = new TotautisSpaceStation();
		Location test = null;
		try {
			test = testPlace.getLocation("LSS DAEDALUS");
		} catch (InvalidLocationException e1) {
			Assert.fail();
		}
		Astronaut astro = new Astronaut();
		
		initialCargo = astro.getItems().length;
		test.addCharacter(astro);

		try {
			for (Item i : test.getCollectableItems()) {
				astro.addItem(i);
			}
		} catch (InventoryManipulationException e) {
			Assert.fail( e.toString() );
		}
		ItemsCommand cmd = new ItemsCommand();
		Assert.assertEquals( 0, cmd.requiredOperands() );
		DummyApplicationClient client = new DummyApplicationClient();
		cmd.run(testPlace, astro, "", client);
		Assert.assertEquals(astro.getItems().length,
				test.getCollectableItems().length + initialCargo );
	}
}