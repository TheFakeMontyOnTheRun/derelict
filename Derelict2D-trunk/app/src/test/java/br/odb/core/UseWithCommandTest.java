/**
 * 
 */
package br.odb.core;

import org.junit.Assert;
import org.junit.Test;

import br.odb.derelict.core.Astronaut;
import br.odb.derelict.core.commands.UseWithCommand;
import br.odb.derelict.core.items.BlowTorch;
import br.odb.derelict.core.items.MagneticBoots;
import br.odb.derelict.core.items.MetalPlate;
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
public class UseWithCommandTest {

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

		UseWithCommand cmd = new UseWithCommand();
		
		Assert.assertEquals( 2, cmd.requiredOperands() );
		
		DummyApplicationClient client = new DummyApplicationClient();
		try {

			astro.addItem( ( (BlowTorch) test.getItem("blowtorch") ).toggle() );
			astro.addItem(test.getItem("magboots"));
			((MagneticBoots) astro.getItem("magboots")).toggle();
			testPlace.moveCharacter(astro.getName(), "hangar");
			cmd.run(testPlace, astro, "metal-plate blowtorch", client);
			Assert.assertEquals(
					MetalPlate.PLATE_WEIGHT_GOOD_CUT,
					((MetalPlate) testPlace.getLocation("hangar").getItem(
							"metal-plate")).weight, 1.0f);
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

		try {
			Assert.assertFalse(client.buffer.contains("cannot"));
			cmd.run(testPlace, astro, "metal-plate magboots", client);
			Assert.assertTrue(client.buffer.contains("cannot"));
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}
	}
}
