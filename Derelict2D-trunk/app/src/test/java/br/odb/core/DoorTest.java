package br.odb.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.odb.derelict.core.Astronaut;
import br.odb.derelict.core.Clearance;
import br.odb.derelict.core.SecuredDoor;
import br.odb.derelict.core.items.KeyCard;
import br.odb.gameworld.exceptions.InventoryManipulationException;

/**
 * @author monty
 * 
 */
public class DoorTest {

	SecuredDoor door1;
	SecuredDoor door2;
	Astronaut hero;
	KeyCard card1;
	KeyCard card2;

	@Before
	public void setUp() throws Exception {
		hero = new Astronaut("hero", "M");
		door1 = new SecuredDoor(Clearance.LOW_RANK);
		door2 = new SecuredDoor(Clearance.HIGH_RANK);
		card1 = new KeyCard(Clearance.LOW_RANK);
		card2 = new KeyCard(Clearance.HIGH_RANK);
	}

	@Test
	public final void testSecurityRanks() {
		try {
			Assert.assertEquals(Clearance.LOW_RANK, door1.clearance);
			Assert.assertEquals(Clearance.HIGH_RANK, door2.clearance);

			Assert.assertFalse(door1.willOpenFor(hero));
			Assert.assertFalse(door2.willOpenFor(hero));

			hero.addItem("card1", card1);

			Assert.assertTrue(door1.willOpenFor(hero));
			Assert.assertFalse(door2.willOpenFor(hero));

			hero.addItem("card2", card2);
			Assert.assertTrue(door1.willOpenFor(hero));
			Assert.assertTrue(door2.willOpenFor(hero));

		} catch (InventoryManipulationException ignored) {

		}
	}

}
