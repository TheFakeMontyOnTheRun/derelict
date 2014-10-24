/**
 * 
 */
package br.odb.derelict.test;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import br.odb.derelict.core.DerelictGame;
import br.odb.derelict.core.items.MagneticBoots;
import br.odb.derelict.core.items.MetalPlate;
import br.odb.derelict.core.items.TimeBomb;
import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.gameworld.CharacterIsNotMovableException;
import br.odb.gameworld.Item;
import br.odb.gameworld.Location;
import br.odb.gameworld.exceptions.DoorActionException;
import br.odb.gameworld.exceptions.InvalidLocationException;
import br.odb.gameworld.exceptions.InvalidSlotException;
import br.odb.gameworld.exceptions.InventoryManipulationException;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;
import br.odb.gameworld.exceptions.ItemNotFoundException;
import br.odb.utils.Direction;

/**
 * @author monty
 * 
 */
public class DerelictGameTest {

	DerelictGame game;
	DummyApplicationClient client;

	@Test
	public final void checkPrintPreamble() {

		DummyApplicationClient dgc = new DummyApplicationClient();
		DerelictGame game0;

		dgc.buffer = "";
		game0 = new DerelictGame();
		game0.setApplicationClient(dgc);
		game0.printPreamble();
		Assert.assertTrue(dgc.buffer.contains("copyright"));
	}

	@Test
	public final void testGameStartsValid() {

		DerelictGame game = new DerelictGame();

		Assert.assertNotNull(game.hero);
		Assert.assertNotNull(game.station);
	}

	@Test
	public final void testGameDescriptionContainsTime() {

		DerelictGame game = new DerelictGame();
		DummyApplicationClient client = new DummyApplicationClient();

		game.setApplicationClient(client);
		game.showUI();
		Assert.assertTrue(client.buffer.toLowerCase().contains("time"));
	}

	@Test
	public final void testGameTimeAdvance() {

		game = new DerelictGame();
		client = new DummyApplicationClient();

		game.setApplicationClient(client);
		Assert.assertTrue(game.continueRunning);
		Assert.assertEquals(0, game.station.elapsedTime);
		game.sendData(null);
		Assert.assertEquals(0, game.station.elapsedTime);
		Assert.assertTrue(game.continueRunning);
		game.sendData("");
		Assert.assertEquals(0, game.station.elapsedTime);
		Assert.assertTrue(game.continueRunning);
		game.sendData("bogus");
		Assert.assertEquals(1000, game.station.elapsedTime);
		Assert.assertTrue(game.continueRunning);
	}

	@Test
	public final void testGameStopAfterOrbitalDecay() {
		game = new DerelictGame();
		client = new DummyApplicationClient();

		game.setApplicationClient(client);

		game.station.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY);
		Assert.assertTrue(game.continueRunning);
		game.sendData("bogus");
		Assert.assertEquals(DerelictGame.DEFAULT_ELAPSED_BASE_TIME
				+ TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY,
				game.station.elapsedTime);
		Assert.assertFalse(game.continueRunning);
	}

	@Test
	public final void testCommandParsing() {

		TimeBomb tb = null;
		game = new DerelictGame();
		client = new DummyApplicationClient();

		game.setApplicationClient(client);
		Assert.assertNotNull( game.getAvailableCommandNames() );
		Assert.assertTrue( game.getAvailableCommandNames().length > 0 );
		client.buffer = "";
		game.sendData("move hangar");
		Assert.assertTrue( client.buffer != null && client.buffer.length() > 0 );

		boolean foundBoots;
		foundBoots = false;
		
		for ( String name : game.getCollectableItemNames() ) {
			if (name.contains( "magboots" ) ) {
				foundBoots = true;
			}
		}
		Assert.assertTrue( foundBoots );

		game.sendData("pick magboots");
		game.sendData("toggle magboots");
		
		Assert.assertEquals( 1, game.getCollectedItemNames().length );
		
		foundBoots = false;
		
		for ( String name : game.getCollectedItemNames() ) {
			if (name.contains( "magboots" ) ) {
				foundBoots = true;
			}
		}

		client.buffer = "";
		game.sendData("move made up place");
		Assert.assertTrue(client.buffer.contains("You can't move there!"));

		client.buffer = "";
		game.sendData("move hangar");
		game.sendData("move main hall 1");
		Assert.assertTrue( client.buffer != null && client.buffer.length() > 0 );
		Assert.assertEquals(1, game.getCollectedItems().length);
		Location hangar;
		try {
			hangar = game.station.getLocation("hangar");
			Assert.assertEquals(hangar, game.hero.getLocation());
			hangar.getItem("metal-plate").setPickable(true);
			float score1 = game.getFinalScore();
			game.sendData("pick metal-plate");
			float score2 = game.getFinalScore();
			Assert.assertTrue( score2 > score1);
			String t0 = game.getFormatedElapsedTime();
			game.sendData("do nothing");
			String t1 = game.getFormatedElapsedTime();
			game.sendData("move s");
			game.station.getLocation( "LSS DAEDALUS" ).addItem( new MetalPlate() );
			float score3 = game.getFinalScore();
			game.sendData("move n");
			Assert.assertTrue( score2 < score3);
			Assert.assertFalse(t0.equalsIgnoreCase(t1));
		} catch (InvalidLocationException e1) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}

		
		Assert.assertTrue(game.getConnectionNames()[Direction.N.ordinal()]
				.equals("main hall 1"));

		client.buffer = "";
		game.sendData("move elevator 1");
		Assert.assertTrue(client.buffer.contains("You can't move there!"));

		client.buffer = "";
		game.sendData("move Down");
		Assert.assertTrue(client.buffer
				.contains("You can't move in that direction!"));

		client.buffer = "";
		game.sendData("drop dead");
		Assert.assertTrue(client.buffer.contains("Item not found"));

		game.hero.getLocation().addItem(new MagneticBoots());

		client.buffer = "";
		game.sendData("toggle metal-plate");
		Assert.assertTrue(client.buffer
				.contains( Item.TOGGLE_DENIAL_MESSAGE ));

		client.buffer = "";
		game.sendData("pick magboots");
		Assert.assertTrue(client.buffer.contains("You can't do that"));

		client.buffer = "";
		game.sendData("pick excalibur");
		Assert.assertTrue(client.buffer.contains("Item not found"));

		ArrayList<Item> tmp;

		tmp = new ArrayList<Item>();

		for (Item i : game.getCollectableItems()) {
			tmp.add(i);
		}

		for (Item i : game.hero.getLocation().getCollectableItems()) {
			if (!tmp.contains(i)) {
				Assert.fail();
			}
		}

		tmp.clear();

		for (Item i : game.getCollectedItems()) {
			tmp.add(i);
		}

		for (Item i : game.hero.getItems()) {
			if (!tmp.contains(i)) {
				Assert.fail();
			}
		}

		try {
			tb = ((TimeBomb) game.station.getItem("time-bomb"));
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}

		try {
			game.station.getLocation(DerelictGame.BOMBING_TARGET).addItem(tb);
		} catch (InvalidLocationException e) {
			Assert.fail();
		}

		Assert.assertTrue(game.continueRunning);
		try {
			tb.use(game.hero);
		} catch (ItemActionNotSupportedException e) {
			Assert.fail();
		}
		game.station.update(TotautisSpaceStation.TIMEBOMB_DEFAULT_FUSE_TIME);
		game.sendData("bogus");
		Assert.assertFalse(game.continueRunning);

		ArrayList<UserCommandLineAction> cmds = new ArrayList<UserCommandLineAction>();

		for (UserCommandLineAction ucl : game.getAvailableCommands()) {
			cmds.add(ucl);
		}
		for (UserCommandLineAction ucl : game.getCommandList().values()) {
			if (!cmds.contains(ucl)) {
				Assert.fail();
			}
		}
	}

	@Test
	public final void testCollectedItems() {

		game = new DerelictGame();
		client = new DummyApplicationClient();

		game.setApplicationClient(client);

		ArrayList<Item> tmp;

		tmp = new ArrayList<Item>();

		for (Item i : game.getCollectableItems()) {
			tmp.add(i);
		}

		for (Item i : game.hero.getLocation().getCollectableItems()) {
			if (!tmp.contains(i)) {
				Assert.fail();
			}
		}

		tmp.clear();

		for (Item i : game.getCollectedItems()) {
			tmp.add(i);
		}

		for (Item i : game.hero.getItems()) {
			if (!tmp.contains(i)) {
				Assert.fail();
			}
		}
	}

	@Test
	public final void testGameCommands() {

		game = new DerelictGame();
		client = new DummyApplicationClient();

		game.setApplicationClient(client);

		ArrayList<UserCommandLineAction> cmds = new ArrayList<UserCommandLineAction>();

		for (UserCommandLineAction ucl : game.getAvailableCommands()) {
			cmds.add(ucl);
		}
		for (UserCommandLineAction ucl : game.getCommandList().values()) {
			if (!cmds.contains(ucl)) {
				Assert.fail();
			}
		}
	}

	// /--------condi������������es de vit������ria---------

	@Test
	public final void testBombingOnSpot() {

		TimeBomb tb = null;
		game = new DerelictGame();
		client = new DummyApplicationClient();

		game.setApplicationClient(client);

		try {
			tb = ((TimeBomb) game.station.getItem("time-bomb"));
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}

		try {
			game.station.getLocation("lab 2").addItem(tb);
		} catch (InvalidLocationException e) {
			Assert.fail();
		}

		Assert.assertTrue(game.continueRunning);
		try {
			tb.use(game.hero);
		} catch (ItemActionNotSupportedException e) {
			Assert.fail();
		}
		game.station.update(TotautisSpaceStation.TIMEBOMB_DEFAULT_FUSE_TIME);
		game.sendData("bogus");
		Assert.assertFalse(game.continueRunning);
	}

	@Test
	public void testVictoryConditions() {

		DummyApplicationClient dgc = new DummyApplicationClient();
		DerelictGame game0;
		Location nave;
		Location hall;
		Location lab;

		// situa������������o 1: Jogador na nave, bomba explode na nave
		// final7
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			nave = game0.station.getLocation("LSS DAEDALUS");
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			bomb0.toggle();
			nave.addItem(bomb0);
			game0.station
					.update(TotautisSpaceStation.TIMEBOMB_DEFAULT_FUSE_TIME + 1);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}

		// final1
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			nave = game0.station.getLocation("LSS DAEDALUS");
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			nave.addItem(bomb0);
			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY + 1);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}

		// final4
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			nave = game0.station.getLocation("LSS DAEDALUS");
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			nave.addItem(bomb0);

			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY - 5);

			bomb0.toggle();

			game0.station.update(6);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}

		// final2
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			nave = game0.station.getLocation("LSS DAEDALUS");
			lab = game0.station.getLocation(DerelictGame.BOMBING_TARGET);
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			nave.addItem(bomb0);
			game0.station.getAstronaut().setLocation(lab);
			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY + 1);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}
		// final8
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			nave = game0.station.getLocation("LSS DAEDALUS");
			lab = game0.station.getLocation(DerelictGame.BOMBING_TARGET);
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			nave.addItem(bomb0);
			bomb0.toggle();
			game0.station.getAstronaut().setLocation(lab);
			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY - 5);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}
		// final5
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			nave = game0.station.getLocation("LSS DAEDALUS");
			lab = game0.station.getLocation(DerelictGame.BOMBING_TARGET);
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			nave.addItem(bomb0);
			game0.station.getAstronaut().setLocation(lab);
			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY - 5);

			bomb0.toggle();

			game0.station.update(6);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}
		// final6
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			nave = game0.station.getLocation("LSS DAEDALUS");
			hall = game0.station.getLocation("main hall 1");
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			nave.addItem(bomb0);
			game0.station.getAstronaut().setLocation(hall);
			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY - 5);

			bomb0.toggle();

			game0.station.update(6);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}

		// final3
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			nave = game0.station.getLocation("LSS DAEDALUS");
			hall = game0.station.getLocation("main hall 1");
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			nave.addItem(bomb0);
			game0.station.getAstronaut().setLocation(hall);
			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY + 1);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}

		// situa������������o 2: Jogador na nave, bomba explode fora da nave
		// final25
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			hall = game0.station.getLocation("main hall 1");
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			bomb0.toggle();
			hall.addItem(bomb0);
			game0.station
					.update(TotautisSpaceStation.TIMEBOMB_DEFAULT_FUSE_TIME + 1);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}
		// situa������������o 3: Jogador na nave, bomba explode no laborat������rio
		// final16
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			lab = game0.station.getLocation(DerelictGame.BOMBING_TARGET);
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			bomb0.toggle();
			lab.addItem(bomb0);
			game0.station
					.update(TotautisSpaceStation.TIMEBOMB_DEFAULT_FUSE_TIME + 1);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}
		// situa������������o 4: Jogador fora da nave, bomba explode na nave
		// final9
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			nave = game0.station.getLocation("LSS DAEDALUS");
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			bomb0.toggle();
			nave.addItem(bomb0);
			game0.hero.addItem(new MagneticBoots().toggle());
			game0.station.moveCharacter(game0.hero.getName(), Direction.N);
			game0.station
					.update(TotautisSpaceStation.TIMEBOMB_DEFAULT_FUSE_TIME + 1);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		} catch (InventoryManipulationException e) {
			Assert.fail();
		} catch (CharacterIsNotMovableException e) {
			Assert.fail();
		} catch (InvalidSlotException e) {
			Assert.fail();
		} catch (DoorActionException e) {
			Assert.fail();
		}

		// situa������������o 5: Jogador fora da nave, bomba explode fora da nave
		// final27
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			hall = game0.station.getLocation("main hall 1");
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			bomb0.toggle();
			hall.addItem(bomb0);
			game0.hero.addItem(new MagneticBoots().toggle());
			game0.station.moveCharacter(game0.hero.getName(), Direction.N);
			game0.station
					.update(TotautisSpaceStation.TIMEBOMB_DEFAULT_FUSE_TIME + 1);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		} catch (InventoryManipulationException e) {
			Assert.fail();
		} catch (CharacterIsNotMovableException e) {
			Assert.fail();
		} catch (InvalidSlotException e) {
			Assert.fail();
		} catch (DoorActionException e) {
			Assert.fail();
		}

		// final20
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			game0.hero.addItem(new MagneticBoots().toggle());
			hall = game0.station.getLocation("main hall 1");
			lab = game0.station.getLocation(DerelictGame.BOMBING_TARGET);
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			hall.addItem(bomb0);
			game0.hero.setLocation(lab);
			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY + 1);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		} catch (InventoryManipulationException e) {
			Assert.fail();
		}

		// final21
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			game0.hero.addItem(new MagneticBoots().toggle());
			hall = game0.station.getLocation("main hall 1");

			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			hall.addItem(bomb0);
			game0.station.moveCharacter(game0.hero.getName(), Direction.N);
			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY + 1);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		} catch (InventoryManipulationException e) {
			Assert.fail();
		} catch (CharacterIsNotMovableException e) {
			Assert.fail();
		} catch (InvalidSlotException e) {
			Assert.fail();
		} catch (DoorActionException e) {
			Assert.fail();
		}

		// final22
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			game0.hero.addItem(new MagneticBoots().toggle());
			hall = game0.station.getLocation("main hall 1");

			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			hall.addItem(bomb0);
			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY - 5);
			bomb0.toggle();
			game0.station.update(6);

			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		} catch (InventoryManipulationException e) {
			Assert.fail();
		}

		// final23
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			game0.hero.addItem(new MagneticBoots().toggle());
			hall = game0.station.getLocation("main hall 1");
			lab = game0.station.getLocation(DerelictGame.BOMBING_TARGET);
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			hall.addItem(bomb0);
			game0.hero.setLocation(lab);

			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY - 5);

			bomb0.toggle();

			game0.station.update(6);

			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		} catch (InventoryManipulationException e) {
			Assert.fail();
		}
		// situa������������o 6: Jogador fora da nave, bomba explode no laborat������rio

		// final28
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			game0.hero.addItem(new MagneticBoots().toggle());
			hall = game0.station.getLocation("main hall 1");

			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			hall.addItem(bomb0);
			game0.hero.setLocation(hall);

			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY + 1);

			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		} catch (InventoryManipulationException e) {
			Assert.fail();
		}

		// final29
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			game0.hero.addItem(new MagneticBoots().toggle());
			hall = game0.station.getLocation("main hall 1");

			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			hall.addItem(bomb0);
			game0.hero.setLocation(hall);

			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY - 5);

			bomb0.toggle();

			game0.station.update(6);

			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		} catch (InventoryManipulationException e) {
			Assert.fail();
		}

		// final30
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			game0.hero.addItem(new MagneticBoots().toggle());
			hall = game0.station.getLocation("main hall 1");

			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			hall.addItem(bomb0);
			game0.hero.setLocation(hall);
			bomb0.toggle();
			game0.station
					.update(TotautisSpaceStation.TIMEBOMB_DEFAULT_FUSE_TIME + 1);

			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		} catch (InventoryManipulationException e) {
			Assert.fail();
		}

		// final26
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			game0.hero.addItem(new MagneticBoots().toggle());
			hall = game0.station.getLocation("main hall 1");
			lab = game0.station.getLocation(DerelictGame.BOMBING_TARGET);
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			hall.addItem(bomb0);
			game0.hero.setLocation(lab);

			bomb0.toggle();

			game0.station
					.update(TotautisSpaceStation.TIMEBOMB_DEFAULT_FUSE_TIME);

			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		} catch (InventoryManipulationException e) {
			Assert.fail();
		}

		// final24
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			game0.hero.addItem(new MagneticBoots().toggle());
			hall = game0.station.getLocation("main hall 1");
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			hall.addItem(bomb0);
			game0.station.moveCharacter(game0.hero.getName(), Direction.N);

			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY - 5);

			bomb0.toggle();

			game0.station.update(6);

			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		} catch (InventoryManipulationException e) {
			Assert.fail();
		} catch (CharacterIsNotMovableException e) {
			Assert.fail();
		} catch (InvalidSlotException e) {
			Assert.fail();
		} catch (DoorActionException e) {
			Assert.fail();
		}

		// final18
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			lab = game0.station.getLocation(DerelictGame.BOMBING_TARGET);
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			bomb0.toggle();
			lab.addItem(bomb0);
			game0.hero.addItem(new MagneticBoots().toggle());
			game0.station.moveCharacter(game0.hero.getName(), Direction.N);
			game0.station
					.update(TotautisSpaceStation.TIMEBOMB_DEFAULT_FUSE_TIME + 1);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		} catch (InventoryManipulationException e) {
			Assert.fail();
		} catch (CharacterIsNotMovableException e) {
			Assert.fail();
		} catch (InvalidSlotException e) {
			Assert.fail();
		} catch (DoorActionException e) {
			Assert.fail();
		}

		// situa������������o 7: bomba no lugar, mas n������o ativada. Esta������������o reentra. Jogador
		// na nave
		// final10
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			lab = game0.station.getLocation(DerelictGame.BOMBING_TARGET);
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			lab.addItem(bomb0);
			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY + 1);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}
		// final11
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			lab = game0.station.getLocation(DerelictGame.BOMBING_TARGET);
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			lab.addItem(bomb0);
			game0.station.getAstronaut().setLocation(lab);
			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY + 1);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}

		// final12
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			lab = game0.station.getLocation(DerelictGame.BOMBING_TARGET);
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			lab.addItem(bomb0);
			game0.station.getAstronaut().setLocation(lab);
			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY + 1);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}

		// final15
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			lab = game0.station.getLocation(DerelictGame.BOMBING_TARGET);
			hall = game0.station.getLocation("main hall 1");
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			lab.addItem(bomb0);
			game0.station.getAstronaut().setLocation(hall);
			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY - 5);

			bomb0.toggle();

			game0.station.update(6);
			Assert.assertFalse(bomb0.isDepleted());
			Assert.assertTrue(game0.station.isOrbitDecayed());
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}

		// final17
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			lab = game0.station.getLocation(DerelictGame.BOMBING_TARGET);
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			lab.addItem(bomb0);
			game0.station.getAstronaut().setLocation(lab);
			bomb0.toggle();
			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY - 5);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}

		// final13
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			lab = game0.station.getLocation(DerelictGame.BOMBING_TARGET);
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			lab.addItem(bomb0);

			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY - 5);

			bomb0.toggle();

			game0.station.update(6);
			Assert.assertFalse(bomb0.isDepleted());
			Assert.assertTrue(game0.station.isOrbitDecayed());
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}

		// final14
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			lab = game0.station.getLocation(DerelictGame.BOMBING_TARGET);
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			lab.addItem(bomb0);
			game0.station.getAstronaut().setLocation(lab);

			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY - 5);

			bomb0.toggle();

			game0.station.update(6);
			Assert.assertFalse(bomb0.isDepleted());
			Assert.assertFalse(game0.station.destroyed);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}

		// situa������������o 11: bomba fora do lugar, ativada. Esta������������o reentra. Jogador
		// portando a bomba, no lugar onde ela deveria ser depositada
		// final12
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			lab = game0.station.getLocation(DerelictGame.BOMBING_TARGET);
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			lab.addItem(bomb0);
			game0.hero.addItem(new MagneticBoots().toggle());
			game0.station.moveCharacter(game0.hero.getName(), Direction.N);
			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY + 1);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		} catch (InventoryManipulationException e) {
			Assert.fail();
		} catch (CharacterIsNotMovableException e) {
			Assert.fail();
		} catch (InvalidSlotException e) {
			Assert.fail();
		} catch (DoorActionException e) {
			Assert.fail();
		}
		
//        if (bombSomewhereElse && !bombArmed && !bombWentOff && astroAtShip
//                && stationDecayed) {
//            finalNum = 19;
//        }
		
		// final19
		try {
			dgc.buffer = "";
			game0 = new DerelictGame();
			hall = game0.station.getLocation("main hall 1");
			TimeBomb bomb0 = (TimeBomb) game0.station.getItem("time-bomb");
			game0.setApplicationClient(dgc);
			hall.addItem(bomb0);
			game0.station
					.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY + 1);
			Assert.assertFalse(game0.checkGameContinuityConditions());
		} catch (InvalidLocationException e) {
			Assert.fail();
		} catch (ItemNotFoundException e) {
			Assert.fail();
		}		
	}
	
	@Test
	public final void testLog() {
		DerelictGame game = new DerelictGame();
		game.start();		
		DummyApplicationClient dac = new DummyApplicationClient();
		game.setApplicationClient( dac );
		Assert.assertTrue( dac.buffer.length() == 0 );
		game.log( "test", "test" );
		Assert.assertTrue( dac.buffer.length() != 0 );		
	}
	
	@Test
	public final void testOnQuit() {
		DerelictGame game = new DerelictGame();
		DummyApplicationClient dac = new DummyApplicationClient();
		game.setApplicationClient( dac );
		game.start();

		game.sendData( null );
		game.sendData( "" );
		game.sendData( "quit" );
		Assert.assertTrue( dac.buffer.length() > 0 );
		dac.buffer = "";
		try {
			game.station.getAstronaut().addItem( new MetalPlate() );
		} catch (InventoryManipulationException e) {
			Assert.fail();
		}
		
		game.sendData( "quit" );
		Assert.assertTrue( dac.buffer.length() > 0 );
	}
}
