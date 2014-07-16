package br.odb.derelict.core;

import java.io.IOException;
import java.util.ArrayList;

import br.odb.derelict.core.commands.DerelictUserMetaCommandLineAction;
import br.odb.derelict.core.commands.DropCommand;
import br.odb.derelict.core.commands.ItemsCommand;
import br.odb.derelict.core.commands.MoveCommand;
import br.odb.derelict.core.commands.PickCommand;
import br.odb.derelict.core.commands.QuitCommand;
import br.odb.derelict.core.commands.ToggleCommand;
import br.odb.derelict.core.commands.TurnToDirectionCommand;
import br.odb.derelict.core.commands.UseCommand;
import br.odb.derelict.core.commands.UseWithCommand;
import br.odb.derelict.core.items.TimeBomb;
import br.odb.derelict.core.items.ValuableItem;
import br.odb.derelict.core.locations.DaedalusSpaceShip;
import br.odb.derelict.core.locations.LanderShip;
import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.gameapp.ApplicationClient;
import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.gameworld.CharacterIsNotMovableException;
import br.odb.gameworld.Item;
import br.odb.gameworld.Location;
import br.odb.gameworld.exceptions.InvalidLocationException;
import br.odb.gameworld.exceptions.InvalidSlotException;
import br.odb.gameworld.exceptions.InventoryManipulationException;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;
import br.odb.gameworld.exceptions.ItemNotFoundException;
import br.odb.utils.ScheduledEvent;
import br.odb.utils.Utils;

public class DerelictGame extends ConsoleApplication {

	public interface EndGameListener {
		void onGameEnd(int ending);
	}

	public static final int DEFAULT_ELAPSED_BASE_TIME = 1000;
	public final static String finalMessage[] = new String[32];
	public static final String BOMBING_TARGET = TotautisSpaceStation.WEAK_SPOT;
	private static final String GAME_STORY1 = "I'm space junker. There is no day I curse more this fate.\n What else can I say? They can't stand me anymore. They keep saying that if you are not happy, you go looking for something else.\n Now it seems almost like a revenge. Lucas had to warn me so late about the misscalculation?!\n I only got some 15 minutes or so to do this one. It is my stuff in the line and I might lose my license.\n At least he had the decency to warn me about some possible gas leakage\n and the lack of artificial gravity (not invented by then, it seems). Hope this blowtorch is enough to cut this old metal. And that I'm all alone in here...";
	private static final String GAME_STORY2 = "I must admit: I love going through other people's stuff.\nI see whole lives unfolding in front of me. Almost like a diorama or a shrine. And I tend to keep the best stuff to myself, \ndespite what it might worth. And then, blowing up the station - almost like a special burial.\n I'm doing this people a favor. I tend to uncover the mysteries they never could in life, \nsimply by going through other people's stuff. It's a requiem.";

	// .alert("The station orbit finally decayed.\nThankfully, you were in your ship and was able to escape. Too bad you lost your license.");
	// .alert("You failed to blow the station. But at least you managed to survive. Too bad you gonna lose your operating license.");
	// .alert("You failed to blow the station. And you're toast!.");
	// .alert("The station orbit finally decayed.\nAnd you were still inside it...");
	// .alert("You did it! You blew the station! Too bad you were still inside it...");
	// .alert("You failed to blow the station. But you managed to blow yourself pretty well. At least, you don't have to worry about the loss of your license.");
	static {
		finalMessage[0] = " ...But yeah, you lost your license. And a few people are probably dead, from the enormous space station falling over their head.";
		finalMessage[1] = " The station orbit finally decayed.\nThankfully, you were in your ship and was able to escape. Too bad you lost your license. ";
		finalMessage[2] = " The station orbit finally decayed.\nAnd you were still inside it... ";
		finalMessage[3] = " Forgot something? You had a mission! Pfff...hopeless... ";
		finalMessage[4] = " You almost ALMOST did it right, but you missed a thing or two...But why falt with respect that fine fine ship? ";
		finalMessage[5] = " You mixed up stuff. Read carefully the mission details and try again,  will you? ";
		finalMessage[6] = " Are you sure you got the right spot and the right schedule? You know the orbit decayed and everything went kaput, right? ";
		finalMessage[7] = " Feeling suicidal, eh? You're not suposed to stay around to see the bomb blowing right into your face! ";
		finalMessage[8] = " You mixed up stuff. Read carefully the mission details and try again,  will you? ";
		finalMessage[9] = " You got it all wrong! Try again, but pay atention. It's not that hard. ";
		finalMessage[10] = " That bomb isn't self-operated. If you keep this in mind, you might get it right the next time. ";
		finalMessage[11] = " How hard can it be to operate that timed-bomb?  You almost got it right - but pay more attention to the damn bomb! ";
		finalMessage[12] = " Can't be sure if you took a wild luck guess or what, but you sure did some stuff right, but missed another great oportunity to save your career. Time to learn to flip astro-reconstructed burguers. ";
		finalMessage[13] = " You did it fine, but took it too long. The station fell and you lost your license. Try faster next time. ";
		finalMessage[14] = " Not fast enough. You were doing fine, until that damn gravity came into action. Be faster, you will please? ";
		finalMessage[15] = " This is not a down-in-the-park situation. You got to be faster and pay more attention ";
		finalMessage[16] = " You made it! Congratulations. Now you can claim your prize. Maybe, someday you can finally retire. ";
		finalMessage[17] = " There is no time enough to stop and life, the universe and everything. ";
		finalMessage[18] = " That fuse...oh, it could be a tad longer, right? Too bad it wasn't. At least you died honourably. ";
		finalMessage[19] = " You dozed, right? At least you were in the right place to escape right at the last minute. ";
		finalMessage[20] = " Your hint was warming up. Too bad the station got quite hot quite fast as well ";
		finalMessage[21] = " Having fun? Good. You gotta try it again. This time, why don't you try to be more efficient on your mission? ";
		finalMessage[22] = " Hey, the bomb was a big part of the plan, but then again, you missed some aspects of it. ";
		finalMessage[23] = " Are you drunk or something? ";
		finalMessage[24] = " Let's be honest here: you didn't bother to read ANY text, did you? ";
		finalMessage[25] = " You failed to blow the station. But at least you managed to survive. Too bad you gonna lose your operating license. ";
		finalMessage[26] = " You failed to blow the station. And you're toast!. ";
		finalMessage[27] = " Ho ho ho. Big Boom! Try again. And try surviving next time. ";
		finalMessage[28] = " Having fun? Good. You gotta try it again. This time, why don't you try to be more efficient on your mission? ";
		finalMessage[29] = " That funny moment you finally get some piece of the puzzle, but you run out of time. ";
		finalMessage[30] = " You failed to blow the station. But you managed to blow yourself pretty well. At least, you don't have to worry about the loss of your license. ";
		finalMessage[31] = " As you faint and have a hard time opening your eyes, your thoughts start to wane into oblivion. You died from the poisonous atmosphere in the station.";
	}

	public EndGameListener endGameListener;
	public Astronaut hero;
	public TotautisSpaceStation station;
	private boolean replayMode;

	public boolean checkGameContinuityConditions() {

		final boolean bombWentOff;
		final boolean bombArmed;
		final boolean bombAtShip;
		final boolean astroAtShip;
		final boolean stationDecayed;
		final boolean astroAtSamePlaceAsBomb;
		final boolean bombAtTarget;
		final boolean astroAtTarget;
		final boolean astroSomewhereElse;
		final boolean bombSomewhereElse;
		final boolean shipKeyActive;

		final Location astronautPlacement;
		final TimeBomb timeBomb;
		final LanderShip BOHR;
		final Location lab2;
		final DaedalusSpaceShip DAEDALUS;

		int finalNum = -1;

		try {
			BOHR = (LanderShip) station.getLocation(LanderShip.DEFAULT_NAME);
			DAEDALUS = (DaedalusSpaceShip) station
					.getLocation(DaedalusSpaceShip.DEFAULT_NAME);
			timeBomb = (TimeBomb) station.getItem("time-bomb");
			lab2 = station.getLocation(BOMBING_TARGET);
			shipKeyActive = ( ( DaedalusSpaceShip.Ignition )station.getItem( DaedalusSpaceShip.Ignition.NAME ) ).isActive();
			bombArmed = timeBomb.isActive();
			bombWentOff = timeBomb.isDepleted()
					|| (bombArmed && DAEDALUS.operative);

			astronautPlacement = station.getAstronaut().getLocation();

			bombAtShip = (timeBomb.location == BOHR || timeBomb.location == DAEDALUS);
			astroAtShip = (DAEDALUS == astronautPlacement || BOHR == astronautPlacement);
			stationDecayed = station.isOrbitDecayed()
					|| (!bombArmed && DAEDALUS.operative) || ( shipKeyActive && astroAtShip );
			astroAtSamePlaceAsBomb = (astronautPlacement == timeBomb.location);
			bombAtTarget = (timeBomb.location == lab2);
			astroAtTarget = (astronautPlacement == lab2);
			astroSomewhereElse = !astroAtShip && !astroAtTarget;
			bombSomewhereElse = !bombAtShip && !bombAtTarget;

			// -----checagem rastreada (ver planilha)------

			if (bombAtShip && !bombArmed && !bombWentOff && astroAtShip
					&& stationDecayed) {
				finalNum = 1;
			}

			if (bombAtShip && !bombArmed && !bombWentOff && astroAtTarget
					&& stationDecayed) {
				finalNum = 2;
			}

			if (bombAtShip && !bombArmed && !bombWentOff && astroSomewhereElse
					&& stationDecayed) {
				finalNum = 3;
			}

			if (bombAtShip && bombArmed && !bombWentOff && astroAtShip
					&& stationDecayed) {
				finalNum = 4;
			}

			if (bombAtShip && bombArmed && !bombWentOff && astroAtTarget
					&& stationDecayed) {
				finalNum = 5;
			}

			if (bombAtShip && bombArmed && !bombWentOff && astroSomewhereElse
					&& stationDecayed) {
				finalNum = 6;
			}

			if (bombAtShip && bombArmed && bombWentOff && astroAtShip
					&& !stationDecayed) {
				finalNum = 7;
			}

			if (bombAtShip && bombArmed && bombWentOff && astroAtTarget
					&& !stationDecayed) {
				finalNum = 8;
			}

			if (bombAtShip && bombArmed && bombWentOff && astroSomewhereElse
					&& !stationDecayed) {
				finalNum = 9;
			}

			if (bombAtTarget && !bombArmed && !bombWentOff && astroAtShip
					&& stationDecayed) {
				finalNum = 10;
			}

			if (bombAtTarget && !bombArmed && !bombWentOff && astroAtTarget
					&& stationDecayed) {
				finalNum = 11;
			}

			if (bombAtTarget && !bombArmed && !bombWentOff
					&& astroSomewhereElse && stationDecayed) {
				finalNum = 12;
			}

			if (bombAtTarget && bombArmed && !bombWentOff && astroAtShip
					&& stationDecayed) {
				finalNum = 13;
			}

			if (bombAtTarget && bombArmed && !bombWentOff && astroAtTarget
					&& stationDecayed) {
				finalNum = 14;
			}

			if (bombAtTarget && bombArmed && !bombWentOff && astroSomewhereElse
					&& stationDecayed) {
				finalNum = 15;
			}

			if (bombAtTarget && bombArmed && bombWentOff && astroAtShip
					&& !stationDecayed) {
				finalNum = 16;
			}

			if (bombAtTarget && bombArmed && bombWentOff && astroAtTarget
					&& !stationDecayed) {
				finalNum = 17;
			}

			if (bombAtTarget && bombArmed && bombWentOff && astroSomewhereElse
					&& !stationDecayed) {
				finalNum = 18;
			}

			if (bombSomewhereElse && !bombArmed && !bombWentOff && astroAtShip
					&& stationDecayed) {
				finalNum = 19;
			}

			if (bombSomewhereElse && !bombArmed && !bombWentOff
					&& astroAtTarget && stationDecayed) {
				finalNum = 20;
			}

			if (bombSomewhereElse && !bombArmed && !bombWentOff
					&& astroSomewhereElse && stationDecayed) {
				finalNum = 21;
			}

			if (bombSomewhereElse && bombArmed && !bombWentOff && astroAtShip
					&& stationDecayed) {
				finalNum = 22;
			}

			if (bombSomewhereElse && bombArmed && !bombWentOff && astroAtTarget
					&& stationDecayed) {
				finalNum = 23;
			}

			if (bombSomewhereElse && bombArmed && !bombWentOff
					&& astroSomewhereElse && stationDecayed) {
				finalNum = 24;
			}

			if (bombSomewhereElse && bombArmed && bombWentOff && astroAtShip
					&& !stationDecayed) {
				finalNum = 25;
			}

			if (bombSomewhereElse && bombArmed && bombWentOff && astroAtTarget
					&& !stationDecayed) {
				finalNum = 26;
			}

			if (bombSomewhereElse && bombArmed && bombWentOff
					&& astroSomewhereElse && !stationDecayed) {
				finalNum = 27;
			}

			if (bombSomewhereElse && !bombArmed && !bombWentOff
					&& astroAtSamePlaceAsBomb && stationDecayed) {
				finalNum = 28;
			}

			if (bombSomewhereElse && bombArmed && !bombWentOff
					&& astroAtSamePlaceAsBomb && stationDecayed) {
				finalNum = 29;
			}

			if (bombSomewhereElse && bombArmed && bombWentOff
					&& astroAtSamePlaceAsBomb && !stationDecayed) {
				finalNum = 30;
			}

			if (station.getAstronaut().toxicity >= 100.0f) {
				finalNum = 31;
			}

			if (finalNum != -1) {
				if (endGameListener != null) {
					endGameListener.onGameEnd(finalNum);
				} else {
					getClient().alert(finalMessage[finalNum]);
				}
				return false;
			}

			if (DAEDALUS.operative && finalNum == -1) {

				if (endGameListener != null) {
					endGameListener.onGameEnd(0);
				} else {

					getClient().alert(finalMessage[0]);
				}
				return false;
			}

			return finalNum == -1;

		} catch (InvalidLocationException e) {
			e.printStackTrace();
		} catch (ItemNotFoundException e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public UserCommandLineAction[] getAvailableCommands() {
		return getCommandList().values().toArray(new UserCommandLineAction[] {});
	}

	public Item[] getCollectableItems() {
		Item[] toReturn = hero.getLocation().getCollectableItems();
		return toReturn;
	}

	public Item[] getCollectedItems() {
		Item[] toReturn = hero.getItems();
		return toReturn;
	}

	public String[] getConnectionNames() {

		ArrayList<String> names = new ArrayList<String>();
		
		if ( hero.getLocation().getConnections()[ hero.direction.ordinal() ] != null ) {
			names.add( hero.getLocation().getConnections()[ hero.direction.ordinal() ].getName() );
		}

		for (Location l : hero.getLocation().getConnections()) {
			
			if ( l == hero.getLocation().getConnections()[ hero.direction.ordinal() ] ) {
				continue;
			}
			
			if (l != null) {
				names.add(l.getName());
			}
		}
		return names.toArray(new String[] {});
	}

	public float getFinalScore() {
		float materialsOnShipWorth;
		float bonus = 0.0f;

		if (station.destroyed) {
			bonus += 10000.0f;
		}

		Location ship;

		materialsOnShipWorth = 0.0f;

		// s�� conta a LSS DAEDALUS se o jogador estiver nela. Ele pode ter
		// fugido pela capsula.
		try {
			ship = station.getLocation("LSS DAEDALUS");
			if (ship == station.getAstronaut().getLocation()) {
				for (Item i : ship.getCollectableItems()) {
					if (i instanceof ValuableItem) {
						materialsOnShipWorth += ((ValuableItem) i).getWorth();
					}
				}
			}
		} catch (InvalidLocationException e) {
			e.printStackTrace();
		}

		return station.getAstronaut().getMaterialWorth() + materialsOnShipWorth
				+ bonus;
	}

	public String getFormatedElapsedTime() {

		return (station.elapsedTime / 1000.0f) + " seconds";
	}

	public void initApp() {

		continueRunning = true;

		for (UserCommandLineAction cmd : new UserCommandLineAction[] {
				new MoveCommand(), new ToggleCommand(), new UseCommand(),
				new UseWithCommand(), new ItemsCommand(), new PickCommand(),
				new DropCommand(), new QuitCommand(),
				new TurnToDirectionCommand() }) {

			getCommandList().put(cmd.toString(), cmd);
		}
	}

	public void initGame() {

		station = new TotautisSpaceStation();
		hero = ((Astronaut) station.getCharacter("hero"));
	}

	@Override
	public ConsoleApplication init() {

		initApp();
		initGame();

		return super.init();
	}

	@Override
	public void onDataEntered(final String entry) {

		if (entry == null || entry.length() == 0) {
			return;
		}

		super.onDataEntered(entry);

		String[] tokens = Utils.tokenize(entry.trim(), " ");
		String operator = tokens[0];
		String operand = entry.replace(operator, "").trim();

		try {

			UserCommandLineAction cmd = getCommandList().get(tokens[0]);

			if (cmd != null) {

				if (!(replayMode && cmd instanceof DerelictUserMetaCommandLineAction)) {

					if (cmd.requiredOperands() <= tokens.length - 1) {
						cmd.run(this, operand);
					} else {
						getClient().alert(
								"This command requires "
										+ cmd.requiredOperands()
										+ " operand(s).");
					}
				}
			}
		} catch (InvalidSlotException e) {
			getClient().alert("You can't move in that direction!");
		} catch (ItemNotFoundException e) {
			getClient().alert("Item not found.");
		} catch (ItemActionNotSupportedException e) {
			getClient().alert(e.toString());
		} catch (InventoryManipulationException e) {
			getClient().alert("You can't do that: " + e.toString());
		} catch (CharacterIsNotMovableException e) {
			getClient().alert("You can't seem to move! " + e.getMessage());
		} catch (InvalidLocationException e) {
			getClient().alert("You can't move there!");
		} catch (ClearanceException e) {
			getClient().alert("You are not allowed to move there.");
		} catch (Exception e) {
			e.printStackTrace();
		}

		station.update(DEFAULT_ELAPSED_BASE_TIME
				+ (75 * hero.getItems().length));

		this.continueRunning = continueRunning
				&& checkGameContinuityConditions();
	}

	@Override
	public ConsoleApplication printPreamble() {

		super.printPreamble();
		getClient().printNormal("intro:");
		getClient().printNormal(GAME_STORY1);
		getClient().printNormal("");
		getClient().printNormal(GAME_STORY2);
		getClient().printNormal("");
		getClient().printNormal(
				"Ok, enough rambling. It's junk time.\nAvailable commands:");

		for (String ucla : getCommandList().keySet()) {
			getClient().printNormal(
					" * "
							+ ucla
							+ " "
							+ ((UserCommandLineAction) getCommandList().get(ucla))
									.getHelp());
		}

		getClient().printNormal("");
		getClient()
				.printNormal(
						"Remember: You can only carry "
								+ Astronaut.INVENTORY_LIMIT
								+ " objects at once. The more you have, the slower you move (it gets hard to pass through doors).");
		getClient()
				.printNormal(
						"And of course, you must either carry the object or leave it at your ship for it to count as salvage.");
		getClient().printNormal("");

		return this;
	}

	@Override
	public ConsoleApplication showUI() {

		getClient().printNormal(getTextOutput());

		return super.showUI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.odb.gamelib.gameapp.ConsoleApplication#setApplicationClient(br.odb
	 * .gamelib.gameapp.ApplicationClient)
	 */
	@Override
	public ConsoleApplication setApplicationClient(
			final ApplicationClient client) {

		super.setApplicationClient(client);

		for (ScheduledEvent ev : station.eventManager.events) {

			if (ev instanceof ScheduledSpookySound) {

				((ScheduledSpookySound) ev).client = client;
			}
		}

		return this;
	}

	protected void doQuit() {

		if (getFinalScore() > 0) {

			doQuit();
		} else {
			getClient()
					.printNormal(
							"I feel obliged to inform you - you didn't score. That's not how you make a living out of space junk.");
		}
		askForReplay();
		getClient()
				.printNormal(
						"Thank you for playing. Check out http://montyprojects.com/scoreboard/leaderboard-index.php to see the scores.");
	}

	public void onQuit() {
		try {
			sendHighScore((long) getFinalScore());
		} catch (IOException e) {
			getClient().alert(
					"It was not possible to send your score. I'm trully sorry. The reason? "
							+ e.toString());
		}
	}

	private void sendHighScore(final long score) throws IOException {

		String name = getClient().getInput(
				"please enter your three letters (empty for cancel). Score: "
						+ score).trim();

		if (name.length() > 0) {

			name = name.substring(0, 3);

			String url = "http://montyprojects.com/submit-score.php?player="
					+ name + "&score=" + score + "&game=2";

			getClient().openHTTP(url);
		}

	}

	public void askForReplay() {

		if (getClient()
				.chooseOption(
						"Do you want a replay?",
						new String[] {
								"Yes, please. Show me the story I just went through. I love my left profile on the screen.",
								"No, thank you. But perhaps a cup of tea, later on?" }) == 1) {

			initGame();

			setShouldSaveHistory(false);
			replayMode = true;

			getClient().printVerbose("Replay comencing now");

			for (String cmd : getCommandHistory()) {
				getClient().printVerbose("entered: " + cmd);
				sendData(cmd);
				getClient().shortPause();
			}
		}
	}

	public String[] getAvailableCommandNames() {
		UserCommandLineAction[] cmds = getCommandList().values().toArray(
				new UserCommandLineAction[] {});

		String[] toReturn = new String[this.getCommandList().values().size()];

		for (int c = 0; c < cmds.length; ++c) {
			toReturn[c] = cmds[c].toString();
		}

		return toReturn;
	}

	public String[] getCollectableItemNames() {
		Item[] items = this.getCollectableItems();

		String[] toReturn = new String[items.length];

		for (int c = 0; c < items.length; ++c) {
			toReturn[c] = items[c].toString();
		}

		return toReturn;
	}

	public String[] getCollectedItemNames() {
		Item[] items = this.getCollectedItems();

		String[] toReturn = new String[items.length];

		for (int c = 0; c < items.length; ++c) {
			toReturn[c] = items[c].toString();
		}

		return toReturn;
	}

	@Override
	public void log(final String tag, final String string) {

		getClient().printVerbose(tag + ":" + string);
	}

	public String getTextOutput() {

		String output = "Mission status:\nTime elapsed: "
				+ getFormatedElapsedTime() + ". ";

		output += ("\nAprox. station outer hull temperature: "
				+ station.hullTemperature + " C. ");

		output += ("\nAprox. value collected: " + getFinalScore() + " $CRNV. ");

		output += ("\nYou're facing: " + station.getAstronaut().direction);
		output += (". \nFloor: "
				+ station.getAstronaut().getLocation().getFloor() + ". ");

		output += "\n" + hero.toString();

		if (((int) station.getAstronaut().toxicity) % 10 == 3) {
			output += ("*cough*");
		}

		return output;
	}
}
