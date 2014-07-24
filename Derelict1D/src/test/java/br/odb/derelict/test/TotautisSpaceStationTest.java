/**
 * 
 */
package br.odb.derelict.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.odb.derelict.core.Astronaut;
import br.odb.derelict.core.DerelictGame;
import br.odb.derelict.core.items.AtmospherePurifier;
import br.odb.derelict.core.items.BlowTorch;
import br.odb.derelict.core.items.MagneticBoots;
import br.odb.derelict.core.items.MetalPlate;
import br.odb.derelict.core.items.PlasmaGun;
import br.odb.derelict.core.items.PlasmaPellet;
import br.odb.derelict.core.items.TimeBomb;
import br.odb.derelict.core.locations.DaedalusSpaceShip;
import br.odb.derelict.core.locations.LanderShip;
import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.gameworld.ActiveItem;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Direction;
import br.odb.gameworld.Item;
import br.odb.gameworld.Kind;
import br.odb.gameworld.Location;
import br.odb.gameworld.exceptions.InvalidCharacterHandlingException;
import br.odb.gameworld.exceptions.InvalidLocationException;
import br.odb.gameworld.exceptions.InvalidSlotException;
import br.odb.gameworld.exceptions.InventoryManipulationException;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;
import br.odb.gameworld.exceptions.ItemNotFoundException;
import br.odb.utils.ScheduledEvent;
import br.odb.utils.Utils;

/**
 * @author monty
 * 
 */
public class TotautisSpaceStationTest {

	TotautisSpaceStation station;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		station = new TotautisSpaceStation();
		Astronaut hero = (Astronaut) station.getCharacter("hero");
		MagneticBoots magboots = new MagneticBoots();
		magboots.toggle();
		hero.addItem(magboots);
	}

	@Test
	public final void testCanMove() {
		try {

			TotautisSpaceStation station = new TotautisSpaceStation();

			Astronaut astro = station.getAstronaut();

			Assert.assertEquals(astro.getLocation().getName(),
					DaedalusSpaceShip.DEFAULT_NAME);

			Assert.assertFalse(astro.hasItem("magboots"));

			Assert.assertFalse(station.canMove(astro, "lab 2"));
			
			Assert.assertFalse(station.canMove(astro, null ) );

			Assert.assertFalse(station.canMove(astro,
					"Made up name that doesn't exist. Yeah, no kidding!"));

			Assert.assertFalse(station.canMove(astro, "hangar"));

			MagneticBoots boots = new MagneticBoots();

			astro.addItem(boots);

			Assert.assertFalse(station.canMove(astro, "hangar"));
			
			boots.toggle();
			
			Assert.assertTrue(station.canMove(astro, "hangar"));
			
			astro.setLocation( null );
			
			Assert.assertFalse(station.canMove(astro, "hangar"));
			
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public final void testSpookySound() {

		TotautisSpaceStation station = new TotautisSpaceStation();

		for (ScheduledEvent ev : station.eventManager.events) {
			Assert.assertFalse(ev.wentOff);
		}

		station.update(6 * Utils.MINUTE_IN_MILISSECONDS);

		Assert.assertTrue(station.eventManager.events.get(0).wentOff);

		for (ScheduledEvent ev : station.eventManager.events) {
			if (ev != station.eventManager.events.get(0)) {

				Assert.assertFalse(ev.wentOff);
			}
		}
		station.update(6 * Utils.MINUTE_IN_MILISSECONDS);
		Assert.assertTrue(station.eventManager.events.get(1).wentOff);

		station.update(6 * Utils.MINUTE_IN_MILISSECONDS);
		Assert.assertTrue(station.eventManager.events.get(2).wentOff);

	}

	/**
	 * Test method for
	 * {@link br.odb.derelict.core.locations.TotautisSpaceStation#TotautisSpaceStation()}
	 * .
	 */
	@Test
	public final void testTotautisSpaceStation() {

		LanderShip lander = null;
		DaedalusSpaceShip ship = null;

		try {
			lander = (LanderShip) station.getLocation(new LanderShip()
					.getName());
			ship = (DaedalusSpaceShip) station
					.getLocation(new DaedalusSpaceShip().getName());
			Assert.assertNotNull(station.getLocation("main hall 1"));
			Assert.assertNotNull(station.getLocation("main hall 2"));
			Assert.assertNotNull(station.getLocation("main hall 3"));
			Assert.assertNotNull(station.getLocation("hangar"));
			Assert.assertNotNull(station.getLocation("pod 1"));
			Assert.assertNotNull(station.getLocation("pod 2"));
			Assert.assertNotNull(station.getLocation("pod 3"));
			Assert.assertNotNull(station.getLocation("pod 4"));
			Assert.assertNotNull(station.getLocation("dorms 1"));
			Assert.assertNotNull(station.getLocation("dorms 2"));
			Assert.assertNotNull(station.getLocation("hyggym"));
			Assert.assertNotNull(station.getLocation("dinner room"));
			Assert.assertNotNull(station.getLocation("control room"));
			Assert.assertNotNull(station.getLocation("lounge"));
			Assert.assertNotNull(station.getLocation("lab 1"));
			Assert.assertNotNull(station.getLocation("lab 2"));
			Assert.assertNotNull(station.getLocation("lab 3"));
			Assert.assertNotNull(station.getLocation("elevator-shaft-1"));
			Assert.assertNotNull(station.getLocation("elevator-shaft-2"));
			Assert.assertNotNull(station.getLocation("elevator-shaft-3"));
		} catch (InvalidLocationException e1) {
			Assert.fail();
		}

		try {
			station.getLocation("hangar").getConnectionDirectionForLocation(
					station.getLocation("not a place!"));
			Assert.fail();
		} catch (InvalidSlotException ex) {

		} catch (InvalidLocationException e) {
		}

		try {
			station.getLocation(null);
			Assert.fail();
		} catch (InvalidLocationException ex) {

		}

		try {
			Assert.assertNotNull(station.getLocation("elevator-shaft-3")
					.getConnectionDirectionForLocation(
							station.getLocation("elevator-shaft-2")));
			Assert.assertNotNull(station.getLocation("elevator-shaft-2")
					.getConnectionDirectionForLocation(
							station.getLocation("elevator-shaft-1")));

			Assert.assertNotNull(station.getLocation("elevator-shaft-1")
					.getConnectionDirectionForLocation(
							station.getLocation("elevator-shaft-2")));
			Assert.assertNotNull(station.getLocation("elevator-shaft-2")
					.getConnectionDirectionForLocation(
							station.getLocation("elevator-shaft-3")));

			Assert.assertNotNull(station.getLocation("elevator-shaft-1")
					.getConnectionDirectionForLocation(
							station.getLocation("main hall 1")));
			Assert.assertNotNull(station.getLocation("elevator-shaft-2")
					.getConnectionDirectionForLocation(
							station.getLocation("main hall 2")));
			Assert.assertNotNull(station.getLocation("elevator-shaft-3")
					.getConnectionDirectionForLocation(
							station.getLocation("main hall 3")));

			Assert.assertNotNull(station.getLocation("main hall 1")
					.getConnectionDirectionForLocation(
							station.getLocation("elevator-shaft-1")));
			Assert.assertNotNull(station.getLocation("main hall 2")
					.getConnectionDirectionForLocation(
							station.getLocation("elevator-shaft-2")));
			Assert.assertNotNull(station.getLocation("main hall 3")
					.getConnectionDirectionForLocation(
							station.getLocation("elevator-shaft-3")));

			Assert.assertNotNull(station.getLocation("main hall 1")
					.getConnectionDirectionForLocation(
							station.getLocation("dorms 1")));
			Assert.assertNotNull(station.getLocation("main hall 1")
					.getConnectionDirectionForLocation(
							station.getLocation("hangar")));
			Assert.assertNotNull(station.getLocation("hangar")
					.getConnectionDirectionForLocation(
							station.getLocation("main hall 1")));

			Assert.assertNotNull(station.getLocation("hangar")
					.getConnectionDirectionForLocation(
							station.getLocation(ship.getName())));

			Assert.assertNotNull(station.getLocation("dorms 1")
					.getConnectionDirectionForLocation(
							station.getLocation("main hall 1")));

			Assert.assertNotNull(station.getLocation("dorms 1")
					.getConnectionDirectionForLocation(
							station.getLocation("dorms 2")));
			Assert.assertNotNull(station.getLocation("dorms 2")
					.getConnectionDirectionForLocation(
							station.getLocation("dorms 1")));

			Assert.assertNotNull(station.getLocation("dorms 1")
					.getConnectionDirectionForLocation(
							station.getLocation("pod 2")));
			Assert.assertNotNull(station.getLocation("dorms 1")
					.getConnectionDirectionForLocation(
							station.getLocation("pod 1")));
			Assert.assertNotNull(station.getLocation("pod 2")
					.getConnectionDirectionForLocation(
							station.getLocation("dorms 1")));
			Assert.assertNotNull(station.getLocation("pod 1")
					.getConnectionDirectionForLocation(
							station.getLocation("dorms 1")));

			Assert.assertNotNull(station.getLocation("dorms 2")
					.getConnectionDirectionForLocation(
							station.getLocation("pod 3")));
			Assert.assertNotNull(station.getLocation("dorms 2")
					.getConnectionDirectionForLocation(
							station.getLocation("pod 4")));
			Assert.assertNotNull(station.getLocation("pod 3")
					.getConnectionDirectionForLocation(
							station.getLocation("dorms 2")));
			Assert.assertNotNull(station.getLocation("pod 4")
					.getConnectionDirectionForLocation(
							station.getLocation("dorms 2")));

			Assert.assertNotNull(station.getLocation("dorms 2")
					.getConnectionDirectionForLocation(
							station.getLocation("lounge")));
			Assert.assertNotNull(station.getLocation("lounge")
					.getConnectionDirectionForLocation(
							station.getLocation("dorms 2")));

			Assert.assertNotNull(station.getLocation("main hall 2")
					.getConnectionDirectionForLocation(
							station.getLocation("hyggym")));
			Assert.assertNotNull(station.getLocation("main hall 2")
					.getConnectionDirectionForLocation(
							station.getLocation("control room")));
			Assert.assertNotNull(station.getLocation("main hall 2")
					.getConnectionDirectionForLocation(
							station.getLocation("dinner room")));

			Assert.assertNotNull(station.getLocation("hyggym")
					.getConnectionDirectionForLocation(
							station.getLocation("main hall 2")));
			Assert.assertNotNull(station.getLocation("control room")
					.getConnectionDirectionForLocation(
							station.getLocation("main hall 2")));
			Assert.assertNotNull(station.getLocation("dinner room")
					.getConnectionDirectionForLocation(
							station.getLocation("main hall 2")));

			Assert.assertNotNull(station.getLocation("main hall 3")
					.getConnectionDirectionForLocation(
							station.getLocation("lab 1")));
			Assert.assertNotNull(station.getLocation("main hall 3")
					.getConnectionDirectionForLocation(
							station.getLocation("lab 2")));
			Assert.assertNotNull(station.getLocation("main hall 3")
					.getConnectionDirectionForLocation(
							station.getLocation("lab 3")));

			Assert.assertNotNull(station.getLocation("lab 1")
					.getConnectionDirectionForLocation(
							station.getLocation("main hall 3")));
			Assert.assertNotNull(station.getLocation("lab 2")
					.getConnectionDirectionForLocation(
							station.getLocation("main hall 3")));
			Assert.assertNotNull(station.getLocation("lab 3")
					.getConnectionDirectionForLocation(
							station.getLocation("main hall 3")));

			Assert.assertNotNull(station.getLocation("hangar")
					.getConnectionDirectionForLocation(lander));
			Assert.assertNotNull(lander
					.getConnectionDirectionForLocation(station
							.getLocation("hangar")));

			Assert.assertNotNull(ship.getConnectionDirectionForLocation(station
					.getLocation("hangar")));
			Assert.assertNotNull(station.getLocation("hangar")
					.getConnectionDirectionForLocation(ship));
			Assert.assertTrue(station.getLocation("hangar").containsItem(
					"metal-plate"));

			Assert.assertNotNull(station.getCharacter("hero"));
			Assert.assertEquals(ship, station.getCharacter("hero")
					.getLocation());
			Assert.assertTrue(ship.containsCharacter("hero"));
			Assert.assertNotNull(station.getAstronaut());
		} catch (InvalidSlotException e) {
			Assert.fail();
		} catch (InvalidLocationException e) {
			Assert.fail();
		}
	}

	/**
	 * Test method for {@link br.odb.derelict.core.Level#Level()}.
	 */
	@Test
	public final void testLevel() {
		Assert.assertNotNull(station.getLocations());
	}

	/**
	 * Test method for
	 * {@link br.odb.derelict.core.Level#moveCharacter(java.lang.String, java.lang.String)}
	 * .
	 */
	// NÃO É UM BOM TESTE...
	// @Test
	// public final void testMoveCharacter() {
	//
	// Character c = station.getCharacters()[0];
	// Location origin;
	// Location destination;
	//
	// for ( Direction d : Direction.values()) {
	//
	// origin = c.getLocation();
	//
	// if (origin.getConnections()[d.ordinal()] != null) {
	// destination = origin.getConnections()[d.ordinal()];
	//
	// try {
	// station.moveCharacter(c.getName(), destination.getName());
	// } catch (CharacterIsNotMovableException e) {
	// // 
	// e.printStackTrace();
	// } catch (InvalidLocationException e) {
	// // 
	// e.printStackTrace();
	// } catch (InvalidSlotException e) {
	// // 
	// e.printStackTrace();
	// } catch (DoorActionException e) {
	// // 
	// e.printStackTrace();
	// }
	// Assert.assertEquals(destination, c.getLocation());
	// }
	// }
	// }

	/**
	 * Test method for
	 * {@link br.odb.derelict.core.Level#addLocation(java.lang.String, br.odb.derelict.core.Location)}
	 * .
	 */
	@Test
	public final void testAddLocation() {
		Location l = new Location("test");
		Assert.assertNotNull(station.addLocation(l));
		try {
			Assert.assertEquals(l, station.getLocation(l.getName()));
		} catch (InvalidLocationException e) {
			Assert.fail();
		}
	}

	/**
	 * Test method for
	 * {@link br.odb.derelict.core.Level#addCharacter(java.lang.String, br.odb.derelict.core.CharacterActor)}
	 * .
	 */
	@Test
	public final void testAddCharacter() {
		CharacterActor plinket = new CharacterActor("Mr. Plinket", new Kind(
				"movies reviewer"));
		try {
			Assert.assertNotNull(station.addCharacter(plinket));
		} catch (InvalidCharacterHandlingException e) {
			Assert.fail();
		}
		Assert.assertEquals(plinket, station.getCharacter("Mr. Plinket"));
	}

	/**
	 * Test method for
	 * {@link br.odb.derelict.core.Level#addNewCharacter(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public final void testAddNewCharacter() {
		CharacterActor c = null;
		try {
			c = station.addNewCharacter("Han Solo", "Scoundrel");
		} catch (InvalidCharacterHandlingException e) {
			Assert.fail();
		}
		Assert.assertNotNull(c);
		Assert.assertEquals(c, station.getCharacter(c.getName()));
	}

	/**
	 * Test method for
	 * {@link br.odb.derelict.core.Level#addNewLocation(java.lang.String)}.
	 */
	@Test
	public final void testAddNewLocation() {
		Location l = station.addNewLocation("Niterói");
		Assert.assertNotNull(l);
		try {
			Assert.assertEquals(l, station.getLocation(l.getName()));
		} catch (InvalidLocationException e) {
			Assert.fail();
		}
	}

	@Test
	public final void testBombDetonation() {

		TimeBomb bomb;

		bomb = new TimeBomb(15 * Utils.MINUTE_IN_MILISSECONDS);
		bomb.use(null);
		Assert.assertFalse(bomb.isDepleted());
		bomb.update(20);
		Assert.assertFalse(bomb.isDepleted());
		bomb.update(20 * Utils.MINUTE_IN_MILISSECONDS);
		Assert.assertTrue(bomb.isDepleted());

		bomb = new TimeBomb(15 * Utils.MINUTE_IN_MILISSECONDS);
		bomb.use(null);
		Assert.assertFalse(bomb.isDepleted());
		bomb.update(15 * Utils.MINUTE_IN_MILISSECONDS);
		Assert.assertTrue(bomb.isDepleted());

		bomb = new TimeBomb(15 * Utils.MINUTE_IN_MILISSECONDS);
		Assert.assertFalse(bomb.isDepleted());
		bomb.update(15 * Utils.MINUTE_IN_MILISSECONDS);
		Assert.assertFalse(bomb.isDepleted());

		bomb = new TimeBomb(15 * Utils.MINUTE_IN_MILISSECONDS);
		Assert.assertFalse(bomb.isDepleted());
		bomb.update(20 * Utils.MINUTE_IN_MILISSECONDS);
		Assert.assertFalse(bomb.isDepleted());

		bomb = new TimeBomb(15 * Utils.MINUTE_IN_MILISSECONDS);
		try {
			station.getLocation(DerelictGame.BOMBING_TARGET).addItem(bomb);
		} catch (InvalidLocationException e) {
			Assert.fail();
		}
		Assert.assertFalse(bomb.isDepleted());
		bomb.use(null);
		bomb.update(20 * Utils.MINUTE_IN_MILISSECONDS);
		Assert.assertTrue(bomb.isDepleted());
		Assert.assertTrue(station.destroyed);
	}

	@Test
	public final void testUsingPlasmaGun() {

		PlasmaGun gun = new PlasmaGun(1);
		MetalPlate plate = new MetalPlate();
		plate.setPickable(false);
		Astronaut astro = station.getAstronaut();
		int ammoBefore = gun.getAmmo();		
		gun.use( astro );
		Assert.assertEquals( ammoBefore, gun.getAmmo() );
		Assert.assertEquals( PlasmaGun.CLANK_SOUND, gun.getUseItemSound() );
		gun.toggle();
		Assert.assertEquals( PlasmaGun.SHOT_SOUND, gun.getUseItemSound() );

		Assert.assertTrue(gun.getAmmo() == 1);
		Assert.assertFalse(plate.isPickable());

		try {
			plate.useWith(gun);
		} catch (ItemActionNotSupportedException e) {
			Assert.fail();
		}
		Assert.assertTrue( ammoBefore != gun.getAmmo() );

		Assert.assertTrue(plate.isPickable());
		Assert.assertTrue(gun.getAmmo() == 0);
		Assert.assertTrue(gun.isDepleted());
		Assert.assertTrue(plate.isPickable());
		Assert.assertEquals(MetalPlate.PLATE_WEIGHT_BAD_CUT, plate.weight, 1.0f);
		plate = new MetalPlate();
		plate.setPickable(false);

		try {
			gun.useWith(plate);
		} catch (ItemActionNotSupportedException e) {
			Assert.fail();
		}
		Assert.assertFalse(plate.isPickable());
		try {
			gun.wasUsedOn( plate );
		} catch (ItemActionNotSupportedException e) {
			Assert.fail();
		}
		Assert.assertFalse(plate.isPickable());
		Assert.assertTrue(gun.getAmmo() == 0);
		Assert.assertTrue(gun.isDepleted());

	}

	@Test
	public final void testBlowtorch() {
		BlowTorch bw;
		BlowTorch bw2;
		MetalPlate plate;
		plate = new MetalPlate();

		try {
			plate.useWith(plate);
			Assert.fail();
		} catch (ItemActionNotSupportedException e) {
		}

		bw = new BlowTorch(200);
		bw2 = new BlowTorch(200);
		bw.toggle();
		
		Assert.assertNotSame( bw.getToxicity(), bw2.getToxicity() );
		Assert.assertNotNull( bw.getTurnOnSound() );
		Assert.assertTrue( Math.abs( bw.getFuel() - bw2.getFuel() ) < 0.1f );
		bw.update( 1 );
		Assert.assertFalse( Math.abs( bw.getFuel() - bw2.getFuel() ) < 0.1f );
		Assert.assertFalse(bw.isDepleted());
		try {
			plate.useWith(bw);
		} catch (ItemActionNotSupportedException e) {
			Assert.fail();
		}
		Assert.assertFalse(bw.isDepleted());
		Assert.assertEquals(MetalPlate.PLATE_WEIGHT_GOOD_CUT, plate.weight,
				1.0f);

		bw = new BlowTorch(0);
		Assert.assertEquals(0.0f, bw.getFuel(), 0.1f);
		Assert.assertTrue(bw.isDepleted());
		try {
			plate.useWith(bw);
			Assert.fail();
		} catch (ItemActionNotSupportedException e) {
		}

		bw = new BlowTorch(-1);
		Assert.assertEquals(0.0f, bw.getFuel(), 0.1f);
		Assert.assertTrue(bw.isDepleted());
	}

	@Test
	public final void testShootingPlasmaGun() {

		PlasmaGun gun = new PlasmaGun(1);
		Assert.assertTrue(gun.getAmmo() == 1);
		Assert.assertFalse(gun.isDepleted());
		PlasmaPellet pellet = null;
		MetalPlate plate = new MetalPlate();
		Astronaut astro = station.getAstronaut();

		try {
			astro.addItem(gun.getName(), gun);
		} catch (InventoryManipulationException e1) {
			Assert.fail();
		}

		plate.setPickable(false);

		Assert.assertTrue(gun.getAmmo() == 1);
		Assert.assertFalse(plate.isPickable());
		try {
			station.getLocation("elevator-shaft-1").addItem(plate);
			station.getLocation("main hall 1").addItem(new MagneticBoots());
		} catch (InvalidLocationException e1) {
			Assert.fail();
		}

		try {
			astro.getGun().toggle();
			pellet = astro.shoot(Direction.N);
			Assert.assertFalse(pellet.isPickable());
			station.getLocation("elevator-shaft-1").addItem(pellet);
		} catch (ItemNotFoundException e) {
			Assert.fail();
		} catch (InvalidLocationException e) {
			Assert.fail();
		}

		Assert.assertNotNull(pellet);

		int steps = 0;

		while (steps < 50 && !pellet.isDepleted()) {

			pellet.update(PlasmaPellet.REGULAR_SPEED_TO_COVER_ONE_CELL);
			++steps;
		}
		try {
			Assert.assertEquals(station.getLocation("elevator-shaft-1"),
					pellet.location);
		} catch (InvalidLocationException e) {
			Assert.fail();
		}

		Assert.assertTrue(plate.isPickable());
		Assert.assertEquals(MetalPlate.PLATE_WEIGHT_REALLY_BAD_CUT,
				plate.weight, 1.0f);
		Assert.assertTrue(gun.getAmmo() == 0);
		Assert.assertTrue(gun.isDepleted());
		Assert.assertTrue(plate.isPickable());

		gun = new PlasmaGun(-1);
		Assert.assertTrue(gun.getAmmo() == 0);
		Assert.assertTrue(gun.isDepleted());

		ActiveItem interferenceSource = null;

		try {
			interferenceSource = (ActiveItem) station
					.getItem("electric-experiment");
			pellet = astro.shoot(Direction.N);
			interferenceSource.location.addItem(pellet);
			pellet.useWith(interferenceSource);
			Assert.assertFalse(interferenceSource.isDepleted());
			interferenceSource.useWith(pellet);
			Assert.assertTrue(interferenceSource.isDepleted());

		} catch (ItemNotFoundException e) {
			Assert.fail();
		} catch (ItemActionNotSupportedException e) {
			e.printStackTrace();
		}

		Assert.assertNotNull(pellet);

	}

	@Test
	public void testDrop() {
		Item token = new Item("token");
		Astronaut astro = station.getAstronaut();
		try {
			astro.addItem("token", token);
		} catch (InventoryManipulationException e) {
			Assert.fail();
		}

		astro.removeItem(token);

		try {
			astro.getItem("token");
			Assert.fail();
		} catch (ItemNotFoundException e) {
		}
	}

	@Test
	public void testPipesAndMagnets() {
		TotautisSpaceStation station = new TotautisSpaceStation();
		try {
			station.getLocation("hyggym").addCharacter(station.getAstronaut());
			MagneticBoots boots = new MagneticBoots();
			station.getAstronaut().addItem(boots);
			boots.setActive(false);
			Assert.assertFalse(station.getLocation("hyggym").containsItem(
					"keycard"));
			station.getLocation("hyggym").getItem("plastic-pipes")
					.useWith(boots);
			Assert.assertFalse(station.getLocation("hyggym").containsItem(
					"keycard"));
			boots.setActive(true);
			station.getLocation("hyggym").getItem("plastic-pipes")
					.useWith(boots);
			Assert.assertTrue(station.getLocation("hyggym").containsItem(
					"keycard-for-root-access"));
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (InventoryManipulationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		} catch (ItemActionNotSupportedException e) {
			Assert.fail();
		}
	}

	@Test
	public void testPipesAndPellets() {
		TotautisSpaceStation station = new TotautisSpaceStation();
		try {
			station.getLocation("hyggym").addCharacter(station.getAstronaut());
			PlasmaGun gun = new PlasmaGun(5);
			gun.toggle();
			station.getAstronaut().addItem(gun);
			Assert.assertFalse(station.getLocation("hyggym").containsItem(
					"keycard"));
			PlasmaPellet pellet = station.getAstronaut().shoot(Direction.N);
			station.getLocation("hyggym").getItem("plastic-pipes")
					.useWith(pellet);
			Assert.assertTrue(station.getLocation("hyggym").containsItem(
					"keycard-for-root-access"));
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (InventoryManipulationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		} catch (ItemActionNotSupportedException e) {
			Assert.fail();
		}
	}

	@Test
	public void testUpdate() {
		TotautisSpaceStation station = new TotautisSpaceStation();
		station.update(5);
		Assert.assertEquals(5, station.elapsedTime);
		station.update(0);
		Assert.assertEquals(5, station.elapsedTime);
		station.update(-5);
		Assert.assertEquals(5, station.elapsedTime);
	}

	@Test
	public void testOrbitalDecay() {
		TotautisSpaceStation station1 = new TotautisSpaceStation();
		TotautisSpaceStation station2 = new TotautisSpaceStation();
		TotautisSpaceStation station3 = new TotautisSpaceStation();

		station1.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY / 2);
		station2.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY);
		station3.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY + 2);

		Assert.assertFalse(station1.isOrbitDecayed());
		Assert.assertTrue(station2.isOrbitDecayed());
		Assert.assertTrue(station3.isOrbitDecayed());
	}

	@Test
	public void testToxicity() {
		TotautisSpaceStation station = new TotautisSpaceStation();
		Astronaut hero = station.getAstronaut();
		AtmospherePurifier suit = new AtmospherePurifier();
		Assert.assertTrue(hero.toxicity >= -0.1f && hero.toxicity <= 0.1f);
		station.update(1000);
		Assert.assertTrue(hero.toxicity >= 0.9f && hero.toxicity <= 1.1f);
		try {
			hero.addItem(suit);
		} catch (InventoryManipulationException e) {
			Assert.fail();
		}
		suit.setActive(false);
		station.update(1000);
		Assert.assertTrue(hero.toxicity >= 1.9f && hero.toxicity <= 2.1f);
		suit.setActive(true);
		station.update(1000);
		Assert.assertTrue(hero.toxicity >= 1.4f && hero.toxicity <= 1.6f);
	}
};
