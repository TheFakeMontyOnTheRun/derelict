package br.odb.core;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import br.odb.derelict.core.DerelictGame;
import br.odb.derelict.core.items.MagneticBoots;
import br.odb.derelict.core.items.TimeBomb;
import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.gameutils.Direction;
import br.odb.gameworld.CharacterIsNotMovableException;
import br.odb.gameworld.Item;
import br.odb.gameworld.Location;
import br.odb.gameworld.exceptions.DoorActionException;
import br.odb.gameworld.exceptions.InvalidLocationException;
import br.odb.gameworld.exceptions.InvalidSlotException;
import br.odb.gameworld.exceptions.InventoryManipulationException;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;
import br.odb.gameworld.exceptions.ItemNotFoundException;


/**
 * @author monty
 *
 */
public class DerelictGameTest {

	DerelictGame game;
	DummyApplicationClient client;

	@Test
	public final void testProperEnding() {
		DummyApplicationClient dgc = new DummyApplicationClient();
		DerelictGame game0;

		dgc.buffer = "";
		game0 = new DerelictGame(); game0.init();
		game0.setApplicationClient(dgc);
		game0.init();
		Assert.assertTrue( game0.checkGameContinuityConditions() );

		game0.sendData( "pick all" );
		game0.sendData( "toggle atmosphere-purifier" );
		game0.sendData( "toggle magboots" );
		game0.sendData( "move n" );
		game0.sendData( "move n" );
		game0.sendData( "move n" );
		game0.sendData( "move d" );
		game0.sendData( "move s" );
		game0.sendData( "move e" );
		game0.sendData( "useWith plastic-pipes magboots" );
		game0.sendData( "pick keycard-for-root-access" );
		game0.sendData( "move w" );
		game0.sendData( "move n" );
		game0.sendData( "move d" );
		game0.sendData( "move s" );
		game0.sendData( "move e" );
		game0.sendData( "toggle time-bomb" );
		game0.sendData( "drop time-bomb" );
		game0.sendData( "move w" );
		game0.sendData( "move n" );
		game0.sendData( "move u" );
		game0.sendData( "move u" );
		game0.sendData( "move s" );
		game0.sendData( "move s" );
		game0.sendData( "move s" );

		game0.endGameListener = new DerelictGame.EndGameListener() {
			public void onGameEnd(int ending) {
				Assert.assertEquals( 16, ending );
			}
		};


		game0.sendData( "toggle ship-ignition-key" );

		Assert.assertFalse( game0.checkGameContinuityConditions() );
	}

	@Test
	public final void testGameStopAfterOrbitalDecay() {
		game = new DerelictGame();
		client = new DummyApplicationClient();

		game.setApplicationClient(client);
		game.init();
		game.station.update(TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY);
		Assert.assertTrue(game.continueRunning);
		game.sendData("bogus");
		Assert.assertEquals(DerelictGame.DEFAULT_ELAPSED_BASE_TIME
				+ TotautisSpaceStation.TIME_FOR_ORBITAL_DECAY,
				game.station.elapsedTime);
		Assert.assertFalse(game.continueRunning);
	}


	@Test
	public final void testCollectedItems() {

		game = new DerelictGame();
		client = new DummyApplicationClient();

		game.setApplicationClient(client);
		game.init();
		ArrayList<Item> tmp;

		tmp = new ArrayList<>();

		tmp.addAll(Arrays.asList(game.getCollectableItems()));

		for (Item i : game.hero.getLocation().getCollectableItems()) {
			if (!tmp.contains(i)) {
				Assert.fail();
			}
		}

		tmp.clear();

		tmp.addAll(Arrays.asList(game.getCollectedItems()));

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

		ArrayList<UserCommandLineAction> cmds = new ArrayList<>();

		cmds.addAll(Arrays.asList(game.getAvailableCommands()));
		for (UserCommandLineAction ucl : game.getCommandList().values()) {
			if (!cmds.contains(ucl)) {
				Assert.fail();
			}
		}
	}

	// /--------condi������������es de vit������ria---------

	@Test
	public final void testBombingOnSpot() {

		TimeBomb tb;
		game = new DerelictGame();
		client = new DummyApplicationClient();

		game.setApplicationClient(client);
		game.init();

		//FACEPALM
		tb =  new TimeBomb(TotautisSpaceStation.TIMEBOMB_DEFAULT_FUSE_TIME);

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
			game0 = new DerelictGame(); game0.init();
			game0.init();
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
			game0.init();
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
			game0 = new DerelictGame(); game0.init();
			game0.init();
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
			game0 = new DerelictGame(); game0.init();
			game0.init();
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
			game0 = new DerelictGame(); game0.init();
			game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
			game0 = new DerelictGame(); game0.init();
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
}
