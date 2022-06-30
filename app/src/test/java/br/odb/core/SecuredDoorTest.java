package br.odb.core;

import org.junit.Assert;
import org.junit.Test;

import br.odb.derelict.core.Astronaut;
import br.odb.derelict.core.Clearance;
import br.odb.derelict.core.SecuredDoor;
import br.odb.derelict.core.items.KeyCard;
import br.odb.gameworld.exceptions.DoorActionException;
import br.odb.gameworld.exceptions.InventoryManipulationException;

/**
 * @author monty
 * 
 */
public class SecuredDoorTest {

	@Test
	public final void testOpenFor() {
		Astronaut char0 = new Astronaut("hero0", "F");
		Astronaut char1 = new Astronaut("hero1", "M");
		Astronaut char2 = new Astronaut("hero2", "F");

		KeyCard card1 = new KeyCard(Clearance.LOW_RANK);
		KeyCard card2 = new KeyCard(Clearance.HIGH_RANK);
		SecuredDoor s0 = new SecuredDoor(Clearance.DEFAULT_CLEARANCE);
		SecuredDoor s1 = new SecuredDoor(Clearance.LOW_RANK);
		SecuredDoor s2 = new SecuredDoor(Clearance.HIGH_RANK);

		Assert.assertEquals(Clearance.DEFAULT_CLEARANCE, char0.getClearance());
		Assert.assertEquals(Clearance.DEFAULT_CLEARANCE, char1.getClearance());
		Assert.assertEquals(Clearance.DEFAULT_CLEARANCE, char2.getClearance());

		Assert.assertTrue(s0.willOpenFor(char0));
		Assert.assertTrue(s0.willOpenFor(char1));
		Assert.assertTrue(s0.willOpenFor(char2));

		try {
			s0.openFor(char0);
		} catch (DoorActionException ignored) {
		}

		try {
			s1.openFor(char1);
			Assert.fail();
		} catch (DoorActionException ignored) {
		}

		Assert.assertFalse(s1.willOpenFor(char0));
		Assert.assertFalse(s1.willOpenFor(char1));
		Assert.assertFalse(s1.willOpenFor(char2));

		Assert.assertFalse(s2.willOpenFor(char0));
		Assert.assertFalse(s2.willOpenFor(char1));
		Assert.assertFalse(s2.willOpenFor(char2));

		try {
			char2.addItem(card2.getName(), card2);
		} catch (InventoryManipulationException e) {
			Assert.fail();
		}
		Assert.assertFalse(s2.willOpenFor(char0));
		Assert.assertFalse(s2.willOpenFor(char1));
		Assert.assertTrue(s2.willOpenFor(char2));

		try {
			char1.addItem(card1.getName(), card1);
		} catch (InventoryManipulationException e) {
			Assert.fail();
		}
		Assert.assertFalse(s1.willOpenFor(char0));
		Assert.assertTrue(s1.willOpenFor(char1));
		Assert.assertTrue(s1.willOpenFor(char2));

		String str;
		for (Clearance c : Clearance.values()) {
			c.toString();
			str = c.toString();
			Assert.assertNotNull(str);
		}
	}
}
