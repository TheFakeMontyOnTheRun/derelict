package br.odb.derelict.core.items;

import java.util.ArrayList;

import br.odb.derelict.core.Astronaut;
import br.odb.gameutils.Direction;
import br.odb.gameworld.ActiveItem;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Item;
import br.odb.gameworld.Location;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;

public class PlasmaGun extends ActiveItem implements Destructive {

    private static final String CLANK_SOUND = "clank";
    private static final String SHOT_SOUND = "shot";
    private static final String NAME = "plasma-gun";
    private int ammo;
    public final ArrayList<PlasmaPellet> firedPellets = new ArrayList<>();

    public PlasmaGun(int initialAmmo) {
        super(NAME);

        ammo = initialAmmo;
        weight = 10.0f;
        updateAmmoStatus();
    }

    @Override
    public void update(long milisseconds) {

        ArrayList<PlasmaPellet> toDelete = new ArrayList<>();

        for (PlasmaPellet pp : firedPellets) {
            if (!pp.isDepleted()) {
                pp.update(milisseconds);
            } else {
                toDelete.add(pp);
            }
        }

        for (PlasmaPellet pp : toDelete) {
            firedPellets.remove(pp);
        }
    }

    @Override
    public String getUseItemSound() {

        return isActive() ? SHOT_SOUND : CLANK_SOUND;
    }

    private boolean performShooting() {
        if (isActive()) {

            --ammo;
            updateAmmoStatus();

            return true;
        }

        return false;
    }

    @Override
    public void use(CharacterActor user) throws ItemActionNotSupportedException {

        if (isActive()) {

            super.use(user);
            shootDirection(((Astronaut) user).direction, user.getLocation());
        } else {
            throw new ItemActionNotSupportedException("It's inactive!");
        }
    }

    private PlasmaPellet shootDirection(Direction d, Location place) {

        PlasmaPellet pellet = null;

        if (performShooting()) {

            pellet = new PlasmaPellet(d, place);
            firedPellets.add(pellet);
        }
        return pellet;
    }

    private void updateAmmoStatus() {

        if (ammo < 0) {
            ammo = 0;
        }

        setIsDepleted(ammo <= 0);
        setDescription("A mostly harmless gun. Useful for heating surfaces and light defense. (ammo: "
                + ammo + ").");
    }

    @Override
    public void wasUsedOn(Item item1) throws ItemActionNotSupportedException {

        if (isDepleted() || !isActive()) {
            return;
        }

        super.wasUsedOn(item1);

        performShooting();
    }
}
