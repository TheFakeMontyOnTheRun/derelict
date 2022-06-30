package br.odb.core;

import org.junit.Assert;
import org.junit.Test;

import br.odb.derelict.core.Astronaut;
import br.odb.derelict.core.Clearance;
import br.odb.derelict.core.items.BlowTorch;
import br.odb.derelict.core.items.Equipment;
import br.odb.derelict.core.items.KeyCard;
import br.odb.derelict.core.items.MagneticBoots;
import br.odb.derelict.core.items.MetalPlate;
import br.odb.gameworld.Location;
import br.odb.gameworld.exceptions.InventoryManipulationException;

/**
 * @author monty
 * 
 */
public class AstronautTest {

	/**
	 * Test method for {@link br.odb.derelict.core.Astronaut#isMovable()}.
	 */
	@Test
	public final void testIsMovable() {
		Astronaut astronaut = new Astronaut("hero", "M");
		MagneticBoots boots = new MagneticBoots();
		Assert.assertFalse(astronaut.isMovable());
		try {
			astronaut.addItem("magboots", boots);
		} catch (InventoryManipulationException e) {
			Assert.fail();
		}
		Assert.assertFalse(astronaut.isMovable());
		boots.activate();
		Assert.assertTrue(astronaut.isMovable());
	}

	/**
	 * Test method for {@link br.odb.derelict.core.Astronaut#getClearance()}.
	 */
	@Test
	public final void testGetClearance() {
		KeyCard card;
		Astronaut astronaut = new Astronaut("hero", "M");
		Assert.assertNotNull(astronaut.getClearance());
		Assert.assertEquals(Clearance.DEFAULT_CLEARANCE,
				astronaut.getClearance());
		card = new KeyCard(Clearance.LOW_RANK);
		try {
			astronaut.addItem(card.getName(), card);
		} catch (InventoryManipulationException e) {
			Assert.fail();
		}
		Assert.assertEquals(Clearance.LOW_RANK, astronaut.getClearance());
	}

	/**
	 * Test method for {@link br.odb.derelict.core.Astronaut#getClearance()}.
	 */
	@Test
	public final void testGetClearanceElevation() {
		KeyCard card;
		Astronaut astronaut = new Astronaut("hero", "M");
		Assert.assertNotNull(astronaut.getClearance());
		Assert.assertEquals(Clearance.DEFAULT_CLEARANCE,
				astronaut.getClearance());
		card = new KeyCard(Clearance.LOW_RANK);
		try {
			astronaut.addItem(card.getName(), card);
		} catch (InventoryManipulationException e) {
			Assert.fail();
		}
		Assert.assertEquals(Clearance.LOW_RANK, astronaut.getClearance());
		card = new KeyCard(Clearance.HIGH_RANK);
		try {
			astronaut.addItem(card.getName(), card);
		} catch (InventoryManipulationException e) {
			Assert.fail();
		}
		Assert.assertEquals(Clearance.HIGH_RANK, astronaut.getClearance());
	}

	/**
	 * Test method for
	 * {@link br.odb.derelict.core.Astronaut#setGender(java.lang.String)}.
	 */
	@Test
	public final void testSetGender() {
		Astronaut astronaut = new Astronaut("hero", "M");
		Assert.assertEquals("M", astronaut.getGender());
		astronaut.setGender("F");
		Assert.assertEquals("F", astronaut.getGender());
	}

	/**
	 * Test method for
	 * {@link br.odb.derelict.core.Astronaut#setGender(java.lang.String)}.
	 */
	@Test
	public final void testConstructor() {
		Astronaut astronaut = new Astronaut();
		Assert.assertNotNull(astronaut.getGender());
		Assert.assertNotNull(astronaut.getName());
	}

	/**
	 * Test method for
	 * {@link br.odb.derelict.core.Astronaut#setGender(java.lang.String)}.
	 */
	@Test
	public final void testMaterialWorth() {
		Astronaut astronaut = new Astronaut();
		MetalPlate plate = new MetalPlate();
		Assert.assertEquals(0.0f, astronaut.getMaterialWorth(), 1.0f);
		plate.weight = 100.0f;
		try {
			astronaut.addItem("metal-plate", plate);
		} catch (InventoryManipulationException e) {
			Assert.fail();
		}
		Assert.assertEquals(100.0f, astronaut.getMaterialWorth(), 1.0f);
	}

	
	@Test
	public final void testCarryingLimit() {
		Astronaut astro = new Astronaut();
		int initialCargo = astro.getItems().length;
		
		try {
			for ( int c = 0; c < Astronaut.INVENTORY_LIMIT - initialCargo; ++c ) {
				astro.addItem( new Equipment( "Lab equip. " + c ) );
			}
		} catch (InventoryManipulationException e) {
			Assert.fail();
		}
		
		try {
			astro.addItem( new MetalPlate() );
			Assert.fail();
		} catch (InventoryManipulationException ignored) {
			
		}
	}
	
	@Test
	public final void testToxicity() {
		BlowTorch bw = new BlowTorch( 1000.0f );
		Astronaut astro = new Astronaut();
		bw.toggle();
		try {
			astro.addItem( bw );
		} catch (InventoryManipulationException e) {
			Assert.fail();
		}
		
		
		float tox0 = astro.toxicity;
		astro.update( 1000 );
		float tox1 = astro.toxicity;
		astro.update( 1000 );
		float tox2 = astro.toxicity;
		astro.update( 1000 );
		float tox3 = astro.toxicity;
		
		Assert.assertTrue( tox0 < tox1 );
		Assert.assertTrue( tox1 < tox2 );
		Assert.assertTrue( tox2 < tox3 );
	}

	/**
	 * Test method for
	 * {@link br.odb.derelict.core.Astronaut#setGender(java.lang.String)}.
	 */
	@Test
	public final void testWorldView() {
		Astronaut astronaut = new Astronaut();
		String desc = "test location";
		Location location = new Location("test").setDescription(desc);
		location.addCharacter(astronaut);
		Assert.assertTrue(astronaut.toString().contains(desc));
	}
}
