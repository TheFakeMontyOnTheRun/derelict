package br.odb.derelict.core.locations;

import br.odb.derelict.core.Astronaut;
import br.odb.derelict.core.Clearance;
import br.odb.derelict.core.SecuredLocation;
import br.odb.derelict.core.items.AtmospherePurifier;
import br.odb.derelict.core.items.BlowTorch;
import br.odb.derelict.core.items.BombRemoteController;
import br.odb.derelict.core.items.Book;
import br.odb.derelict.core.items.Equipment;
import br.odb.derelict.core.items.KeyCard;
import br.odb.derelict.core.items.MagneticBoots;
import br.odb.derelict.core.items.MetalPlate;
import br.odb.derelict.core.items.Pipe;
import br.odb.derelict.core.items.PlasmaGun;
import br.odb.derelict.core.items.TimeBomb;
import br.odb.gameutils.Direction;
import br.odb.gameutils.Utils;
import br.odb.gameworld.Item;
import br.odb.gameworld.Location;
import br.odb.gameworld.Place;
import br.odb.gameworld.exceptions.InvalidCharacterHandlingException;
import br.odb.gameworld.exceptions.InvalidLocationException;
import br.odb.gameworld.exceptions.ItemNotFoundException;

public class TotautisSpaceStation extends Place {

    private static final int TIME_FOR_ORBITAL_DECAY = 17 * Utils.MINUTE_IN_MILISSECONDS;
    private static final int TIMEBOMB_DEFAULT_FUSE_TIME = 4 * Utils.MINUTE_IN_MILISSECONDS;
    private static final float HEATING_RATE_SECOND = 1000 / Utils.SECOND_IN_MILISSECONDS;
    public static final String WEAK_SPOT = "lab 1";

    private static final String BOOK0_TEXT = "...And so, I realized that when you apply a presure in MicroG...";
    private static final String BOOK2_TEXT = "...Look in the mirror e try laughing. It's going to be hard first, but once you get used to it, it is easy to leave the room with a smile in your face.";
    private static final String BOOK3_TEXT = "...We meet every night in the empty lab, to make out. I asked her for a access key and hid it in the tubes - probably a good place to hide it. Those are inactive for years! If they catch me, I'm scr...";
    private static final String BOOK4_TEXT = "Today, I lost card during dinner. Gonna ask for a new one. Specially disconcerting since it gives access to all bunks. Nobody knows, so nothing to worry, I guess...";
    private static final String BOOK5_TEXT = "...and so you guys could just join in and see whats going on. I hope it is not too instrusive of me. To that, she just gave me a cold stare and...";
    private static final String BOOK6_TEXT = "It's badly burn't. Can't read it.";
    private static final String BOOK7_TEXT = "[Encrypted content. Key needed: PK42C or equivalent]";
    private static final String BOOK8_TEXT = "..." + WEAK_SPOT + " last week mending was a real rush job. Any stronger bump and it might ...";

    private static final String BOOK0_TITLE = "Paper draft";  //lab1
    private static final String BOOK2_TITLE = "One day at a time - how to overcome a great loss"; //pod1
    private static final String BOOK3_TITLE = "Diary";  //pod3 - katya
    private static final String BOOK4_TITLE = "Worklog."; //pod4
    private static final String BOOK5_TITLE = "*unlabeled volume*";
    private static final String BOOK6_TITLE = "Voynich Manuscript Translation."; //lab3
    private static final String BOOK7_TITLE = "[Encrypted text. Key needed: PK42C or equivalent]"; //control room
    private static final String BOOK8_TITLE = "Maintenance Report";

    public int elapsedTime;
    public boolean destroyed;
    public float hullTemperature = 13;

    public TotautisSpaceStation() {

        super();

        LanderShip lander;
        DaedalusSpaceShip ship;

        Floor floor1 = (Floor) addNewFloor("floor1");
        Floor floor2 = (Floor) addNewFloor("floor2");
        Floor floor3 = (Floor) addNewFloor("floor3");

        try {
            addCharacter(
                    "hero", new Astronaut("hero", "M"));
        } catch (InvalidCharacterHandlingException e) {
            // merda
            e.printStackTrace();
        }

        floor1.addNewLocation("main hall 1")
                .setCollectables(
                        new Item[]{new Equipment("comm-system").setPickable(false)}).setDescription("A well lit hall, with doors. It's the main hub of the station. Despite being right next to the hangar and the control room, it's rather quiet.");

        floor2.addNewLocation("main hall 2")
                .setCollectables(
                        new Item[]{new Equipment("comm-system").setPickable(false)}).setDescription("Not as imponent as the main hall from Level1, this hall has a busier feel. Here you see objects thrown all over the place, as if someone was in the middle of a day-to-day routine and had to quickly run.");

        floor3.addNewLocation("main hall 3")
                .setCollectables(
                        new Item[]{new MetalPlate("metal-sheet").setPickable(false), new Equipment("comm-system").setPickable(false)}).setDescription("This was a restricted area, so it's rather sparce. Mostly labs and equipment. A constant hum from the generaters can be heard");

        floor1.addNewLocation("hangar")
                .setDescription("The station main hangar is rather unremarkable. The only thing you notice is a slight yellow tint of the air, as if a mist slides next to the floor. It's very faint. Your ship's computer tells you this is harmless (as if those readings were worth the trust). Unfortunately, no useful tools around here. Around the corner, near the escape pod entrance, there is deactivated ship reactor.")
                .addItem(new MetalPlate().setPickable(false)).addItem(new Pipe(null));

        floor1.addNewLocation("pod 1")
                .setCollectables(
                        new Item[]{new Book(BOOK2_TITLE,
                                BOOK2_TEXT)}).setDescription("A male living pod. Looks like from one of the scientists. It's messy, but as if it's occupant would easily find his belongings in there. There are a few cracks in the glass already.");

        floor1.addNewLocation("pod 2")
                .setDescription("A empty living pod. Looks as if it was never ever used. If can even see some of the factory stickers in it.");

        floor1.addNewLocation("pod 3")
                .setCollectables(
                        new Item[]{new Book(BOOK3_TITLE,
                                BOOK3_TEXT)}).setDescription("A young woman's pod. You do recognize a few items, but its badly mixed up. It's hard to make the age of girl, but she was young. From the pictures of the former glory, the carpet matched the curtains.");

        floor1.addNewLocation("pod 4")
                .setCollectables(
                        new Item[]{new Book(BOOK4_TITLE,
                                BOOK4_TEXT)}).setDescription("A scientists pod, for sure. It's neat, clean and organized. Not much around. He had a strange fixation on redheads.");

        floor1.addNewLocation("dorms 1")
                .setDescription("Part of the dorms hallway. There are some (busted) control panels for ejecting the pods. Some pieces of cloth and broken plastic on the floor, but nothing really useful or valuable.");

        floor1.addNewLocation("dorms 2")
                .setDescription("Anonther part of the dorms hallway. On those, the panels were visibly well. There is a skylight. These parts of the quarters were probably the luxury ones.");

        floor2.addNewLocation("hyggym")
                .setDescription("This is where they used to workout to keep their health and where they would stay clean. Smells like dry sweat. Ick.")
                .addItem(new Pipe(new KeyCard(Clearance.LAB_RANK)));

        floor2.addNewLocation("dinner room")
                .setDescription("Empty stomach makes no science. Those thinkers were really into fancy stuff. Too bad it all went bad a long time ago.")
                .addItem(new KeyCard(Clearance.HIGH_RANK)).addItem(new Book(BOOK5_TITLE, BOOK5_TEXT));

        floor2.addNewLocation("control room")
                .setDescription("Lots of old equiptment.").addItem(new Book(BOOK7_TITLE, BOOK7_TEXT)).addItem(new Equipment("computer-stand").setDescription("JACKPOT! A very valuable vintage rare-and-in-working-conditions computer rack!"));

        floor1.addNewLocation("lounge")
                .setDescription("Here, it seems like a relaxation place. You gaze at the stars and the planet. Very nice.");

        floor3.addNewLocation("lab 1")
                .setDescription("A micro-g-hydrostatic lab. Lots of old equipments. There must be something valuable here.").addItem(new Book(BOOK0_TITLE, BOOK0_TEXT)).addItem(new MetalPlate("metal-scrap").setPickable(false));

        floor3.addNewLocation("lab 2")
                .setDescription("A low-atmosphere-electricity lab. Lots of strange equipment. Looks dangerous.")
                .addItem(
                        new Equipment("electric-experiment")).addItem(new Book(BOOK8_TITLE, BOOK8_TEXT));

        floor3.addNewLocation("lab 3")
                .setDescription("Looks like this was a chemistry lab. Looks badly destroyed. I was told this was due to space-trash. That's why they got us! On the left wall, there are remnants of a 3D periodic table. If only this was in once piece, it could make some good cash.").addItem(new Book(BOOK6_TITLE, BOOK6_TEXT)).addItem(new Pipe(null));

        floor1.addNewLocation("elevator-level-1").setDescription("Going down? The elevator no longer works. It seems to be stuck in level 3. You have to navegate the shaft by yourself.");

        floor2.addNewLocation("elevator-level-2").setDescription("Going up or down? Looking down, you can clearly see the elevator cabin in level 3.");

        floor3.addNewLocation("elevator-level-3").setDescription("Going up? Fortunately, the escape hatch is open and this allows for access. The cabin itself is unremarkable.");

        ship = new DaedalusSpaceShip();
        TimeBomb bomb = new TimeBomb(TIMEBOMB_DEFAULT_FUSE_TIME);
        floor1.addLocation(ship)
                .setCollectables(
                        new Item[]{
                                new BombRemoteController(bomb),
                                bomb,
                                new MagneticBoots(),
                                new AtmospherePurifier(),
                                new KeyCard(Clearance.LOW_RANK),
                                new BlowTorch(100.0f),
                                new DaedalusSpaceShip.Ignition(),
                                new PlasmaGun(
                                        DaedalusSpaceShip.INITIAL_AMMO_AVAILABLE)});

        lander = new LanderShip();
        floor1.addLocation(lander);

        try {
            getLocation("elevator-level-3").setConnected(Direction.CEILING,
                    getLocation("elevator-level-2"));
            getLocation("elevator-level-2").setConnected(Direction.CEILING,
                    getLocation("elevator-level-1"));

            getLocation("elevator-level-1").setConnected(Direction.FLOOR,
                    getLocation("elevator-level-2"));
            getLocation("elevator-level-2").setConnected(Direction.FLOOR,
                    getLocation("elevator-level-3"));

            getLocation("elevator-level-1").setConnected(Direction.S,
                    getLocation("main hall 1"));
            getLocation("elevator-level-2").setConnected(Direction.S,
                    getLocation("main hall 2"));
            getLocation("elevator-level-3").setConnected(Direction.S,
                    getLocation("main hall 3"));

            getLocation("main hall 1").setConnected(Direction.N,
                    getLocation("elevator-level-1"));

            getLocation("main hall 2").setConnected(Direction.N,
                    getLocation("elevator-level-2"));
            getLocation("main hall 3").setConnected(Direction.N,
                    getLocation("elevator-level-3"));

            getLocation("main hall 1").setConnected(Direction.E,
                    getLocation("dorms 1"));
            ((SecuredLocation) getLocation("main hall 1")).setConnected(
                    Direction.S,
                    getLocation("hangar"), Clearance.LOW_RANK);
            ((SecuredLocation) getLocation("hangar")).setConnected(Direction.N,
                    getLocation("main hall 1"), Clearance.LOW_RANK);
            getLocation("dorms 1").setConnected(Direction.W,
                    getLocation("main hall 1"));

            getLocation("dorms 1").setConnected(Direction.E,
                    getLocation("dorms 2"));
            getLocation("dorms 2").setConnected(Direction.W,
                    getLocation("dorms 1"));

            ((SecuredLocation) getLocation("dorms 1")).setConnected(
                    Direction.S,
                    getLocation("pod 2"), Clearance.HIGH_RANK);
            ((SecuredLocation) getLocation("dorms 1")).setConnected(
                    Direction.N,
                    getLocation("pod 1"), Clearance.HIGH_RANK);
            getLocation("pod 2").setConnected(Direction.N,
                    getLocation("dorms 1"));
            getLocation("pod 1").setConnected(Direction.S,
                    getLocation("dorms 1"));

            ((SecuredLocation) getLocation("dorms 2")).setConnected(
                    Direction.N,
                    getLocation("pod 3"), Clearance.HIGH_RANK);
            ((SecuredLocation) getLocation("dorms 2")).setConnected(
                    Direction.S,
                    getLocation("pod 4"), Clearance.HIGH_RANK);
            getLocation("pod 3").setConnected(Direction.S,
                    getLocation("dorms 2"));

            getLocation("pod 4").setConnected(Direction.N,
                    getLocation("dorms 2"));

            getLocation("dorms 2").setConnected(Direction.E,
                    getLocation("lounge"));
            getLocation("lounge").setConnected(Direction.W,
                    getLocation("dorms 2"));

            getLocation("main hall 2").setConnected(Direction.E,
                    getLocation("hyggym"));

            ((SecuredLocation) getLocation("main hall 2")).setConnected(Direction.S,
                    getLocation("control room"), Clearance.HIGH_RANK);


            getLocation("main hall 2").setConnected(Direction.W,
                    getLocation("dinner room"));

            getLocation("hyggym").setConnected(Direction.W,
                    getLocation("main hall 2"));
            getLocation("control room").setConnected(Direction.N,
                    getLocation("main hall 2"));
            getLocation("dinner room").setConnected(Direction.E,
                    getLocation("main hall 2"));

            ((SecuredLocation) getLocation("main hall 3")).setConnected(Direction.E,
                    getLocation("lab 1"), Clearance.LAB_RANK);
            ((SecuredLocation) getLocation("main hall 3")).setConnected(Direction.S,
                    getLocation("lab 2"), Clearance.LAB_RANK);
            ((SecuredLocation) getLocation("main hall 3")).setConnected(Direction.W,
                    getLocation("lab 3"), Clearance.LAB_RANK);

            getLocation("lab 1").setConnected(Direction.W,
                    getLocation("main hall 3"));
            getLocation("lab 2").setConnected(Direction.N,
                    getLocation("main hall 3"));
            getLocation("lab 3").setConnected(Direction.E,
                    getLocation("main hall 3"));

            getLocation("hangar").setConnected(Direction.E, lander);
            lander.setConnected(Direction.W,
                    getLocation("hangar"));

            ship.setConnected(Direction.N,
                    getLocation("hangar"));
            getLocation("hangar").setConnected(Direction.S, ship);

        } catch (InvalidLocationException e) {
            // TODO Die the hell out of this.
            e.printStackTrace();
        }

        ship.addCharacter(getCharacter("hero"));
    }

    private Location addNewFloor(String floorName) {
        return addLocation(new Floor(floorName, this));
    }

    @Override
    public Location addNewLocation(String name) {

        return addLocation(new SecuredLocation(name));
    }

    public Astronaut getAstronaut() {
        return (Astronaut) getCharacter("hero");
    }

    public boolean isOrbitDecayed() {
        return elapsedTime >= TIME_FOR_ORBITAL_DECAY;
    }

    @Override
    public void update(long milisseconds) {

        super.update(milisseconds);

        if (milisseconds > 0) {
            hullTemperature += HEATING_RATE_SECOND;
            elapsedTime += milisseconds;
            updateAstronautToxicityLevel();
        }
    }

    private void updateAstronautToxicityLevel() {

        float delta = 1.0f;

        try {
            AtmospherePurifier suit = (AtmospherePurifier) getAstronaut()
                    .getItem(AtmospherePurifier.SUIT_NAME);
            if (suit.isActive()) {
                delta = -0.5f;
            }
        } catch (ItemNotFoundException ignored) {
        }


        getAstronaut().toxicity = Utils.clamp(getAstronaut().toxicity + delta,
                0.0f, 100.0f);
    }
}
