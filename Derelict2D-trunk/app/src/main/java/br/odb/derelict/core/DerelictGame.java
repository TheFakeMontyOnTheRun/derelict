package br.odb.derelict.core;

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
import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.gameworld.CharacterIsNotMovableException;
import br.odb.gameworld.Item;
import br.odb.gameworld.Location;
import br.odb.gameworld.exceptions.InvalidLocationException;
import br.odb.gameworld.exceptions.InvalidSlotException;
import br.odb.gameworld.exceptions.InventoryManipulationException;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;
import br.odb.gameworld.exceptions.ItemNotFoundException;


public class DerelictGame extends ConsoleApplication {

    public final static String[] finalMessage = new String[32];
    public static final String GAME_RULES = "Remember: You can only carry "
            + Astronaut.INVENTORY_LIMIT
            + " objects at once. The more you have, the slower you move (it gets hard to pass through doors). \n "
            + "And of course, you must either carry the object or leave it at your ship for it to count as salvage.";
    public static final String GAME_STORY1 = "I'm a space junker. There is no day I curse more this fate.\n What else can I say? They can't stand me anymore. They keep saying that if you are not happy, you go looking for something else.\n Now it seems almost like a revenge. Lucas had to warn me so late about the misscalculation?!\n I only got some 15 minutes or so to do this one. It is my stuff in the line and I might lose my license.\n At least he had the decency to warn me about some possible gas leakage\n and the lack of artificial gravity (not invented by then, it seems). Hope this blowtorch is enough to cut this old metal. And that I'm all alone in here...";
    public static final String GAME_STORY2 = "I must admit: I love going through other people's stuff.\nI see whole lives unfolding in front of me. Almost like a diorama or a shrine. And I tend to keep the best stuff to myself, \ndespite what it might worth. And then, blowing up the station - almost like a special burial.\n I'm doing this people a favor. I tend to uncover the mysteries they never could in life, \nsimply by going through other people's stuff. It's a requiem.";
    private static final int DEFAULT_ELAPSED_BASE_TIME = 1000;
    private static final String BOMBING_TARGET = TotautisSpaceStation.WEAK_SPOT;

    static {
        finalMessage[0] = " ...But yeah, you lost your license. And a few people are probably dead, from the enormous space station falling over their heads.";
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

    private boolean checkGameContinuityConditions() {

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
            bombArmed = timeBomb.isActive();
            bombWentOff = timeBomb.isDepleted()
                    || (bombArmed && DAEDALUS.operative);

            astronautPlacement = station.getAstronaut().getLocation();

            bombAtShip = (timeBomb.location == BOHR || timeBomb.location == DAEDALUS);
            astroAtShip = (DAEDALUS == astronautPlacement || BOHR == astronautPlacement);
            stationDecayed = station.isOrbitDecayed();
            astroAtSamePlaceAsBomb = (astronautPlacement == timeBomb.location);
            bombAtTarget = (timeBomb.location == lab2);
            astroAtTarget = (astronautPlacement == lab2);
            astroSomewhereElse = !astroAtShip && !astroAtTarget;
            bombSomewhereElse = !bombAtShip && !bombAtTarget;

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
        return getCommandList().values()
                .toArray(new UserCommandLineAction[]{});
    }

    public Item[] getCollectableItems() {
        return hero.getLocation().getCollectableItems();
    }

    public Item[] getCollectedItems() {
        return hero.getItems();
    }

    public String[] getConnectionNames() {

        ArrayList<String> names = new ArrayList<>();

        if (hero.getLocation().getConnections()[hero.direction.ordinal()] != null) {
            names.add(hero.getLocation().getConnections()[hero.direction
                    .ordinal()].getName());
        }

        for (Location l : hero.getLocation().getConnections()) {

            if (l == hero.getLocation().getConnections()[hero.direction
                    .ordinal()]) {
                continue;
            }

            if (l != null) {
                names.add(l.getName());
            }
        }
        return names.toArray(new String[]{});
    }

    private float getFinalScore() {
        float materialsOnShipWorth;
        float bonus = 0.0f;

        if (station.destroyed) {
            bonus += 10000.0f;
        }

        Location ship;

        materialsOnShipWorth = 0.0f;

        // só conta a LSS DAEDALUS se o jogador estiver nela. Ele pode ter
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

        int minutes = ((int) (station.elapsedTime / (1000.0f * 60.0f)));
        int seconds = ((int) (station.elapsedTime / (1000.0f)));

        seconds -= minutes * 60;

        String toReturn = "";

        if (minutes > 0) {
            toReturn += "" + minutes + " minute";

            if (minutes > 1) {
                toReturn += "s";
            }
        }

        if (seconds > 0) {
            if (minutes > 0) {
                toReturn += ", ";
            }
            toReturn += "" + seconds + " second";

            if (seconds > 1) {
                toReturn += "s";
            }
        }

        return toReturn;
    }

    private void initApp() {

        continueRunning = true;

        for (UserCommandLineAction cmd : new UserCommandLineAction[]{
                new MoveCommand(), new ToggleCommand(), new UseCommand(),
                new UseWithCommand(), new ItemsCommand(), new PickCommand(),
                new DropCommand(), new QuitCommand(),
                new TurnToDirectionCommand()}) {

            getCommandList().put(cmd.toString(), cmd);
        }
    }

    private void initGame() {

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

        String[] tokens = entry.trim().split("[ ]+");
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


    public String getTextOutput() {

        String output = "Mission status:\nTime elapsed: "
                + getFormatedElapsedTime() + ". ";

        output += ("\nStation outer hull temperature: "
                + station.hullTemperature + " C. ");

        output += ("\nValue collected: " + getFinalScore() + " $CRNV. ");

        output += ("\nYou're facing: " + station.getAstronaut().direction);
        output += (". \nFloor: "
                + station.getAstronaut().getLocation().getFloor() + ". ");

        output += "\n" + hero.toString();

        if (((int) station.getAstronaut().toxicity) % 20 == 3) {
            output += ("*cough*");
        }

        return output;
    }

    public interface EndGameListener {
        void onGameEnd(int ending);
    }
}
