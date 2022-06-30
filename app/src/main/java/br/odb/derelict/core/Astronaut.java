package br.odb.derelict.core;

import org.jetbrains.annotations.NotNull;

import br.odb.derelict.core.items.KeyCard;
import br.odb.derelict.core.items.MagneticBoots;
import br.odb.derelict.core.items.PlasmaGun;
import br.odb.derelict.core.items.PlasmaPellet;
import br.odb.derelict.core.items.Toxic;
import br.odb.derelict.core.items.ValuableItem;
import br.odb.gameutils.Direction;
import br.odb.gameworld.ActiveItem;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Item;
import br.odb.gameworld.exceptions.InventoryManipulationException;
import br.odb.gameworld.exceptions.ItemNotFoundException;

public class Astronaut extends CharacterActor {

	public static final int INVENTORY_LIMIT = 9;
	public float toxicity;
	public Direction direction = Direction.N;
	private String gender;
	private PlasmaGun gun;

	public Astronaut() {
		super("hero");

		init("M");
	}

	public Astronaut(String name, String gender) {
		super(name);

		init(gender);
	}

	private void init(String gender) {
		this.toxicity = 0.0f;
		this.gender = gender;
	}

	public void addItem(Item i)
			throws InventoryManipulationException {
		if (super.getItems().length >= INVENTORY_LIMIT) {
			throw new InventoryManipulationException("Cannot hold more");
		}
		super.addItem(i.getName(), i);

		if (i instanceof PlasmaGun) {
			gun = (PlasmaGun) i;
		}
	}

	public Clearance getClearance() {

		Clearance biggestClearance = null;
		KeyCard card;

		for (Item i : getItems()) {
			if (i instanceof KeyCard) {
				card = (KeyCard) i;

				if (biggestClearance == null
						|| card.clearance.ordinal() > biggestClearance
						.ordinal()) {
					biggestClearance = card.clearance;
				}
			}
		}

		if (biggestClearance != null) {

			return biggestClearance;
		} else {

			return Clearance.DEFAULT_CLEARANCE;
		}
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public float getMaterialWorth() {

		float worth = 0.0f;

		for (Item i : getItems()) {
			if (i instanceof ValuableItem) {
				worth += ((ValuableItem) i).getWorth();
			}
		}
		return worth;
	}

	@Override
	public boolean isMovable() {

		try {

			return ((ActiveItem) getItem(MagneticBoots.NAME))
					.isActive();
		} catch (ItemNotFoundException e) {
			return false;
		}
	}

	public void update(long MS) {
		for (Item i : getItems()) {
			if (i instanceof Toxic) {
				toxicity += ((Toxic) i).getToxicity();
			}

			i.update(MS);
		}
	}

	@NotNull
	@Override
	public String toString() {
		return "Toxicity: " + toxicity + ". \n\nYour world view:\n \n" + getLocation().getName() + ". \n" + getLocation(); //$NON-NLS-2$
	}

	public PlasmaGun getGun() {
		return gun;
	}

	public PlasmaPellet shoot(Direction direction) {
		gun.shootDirection(direction, getLocation());
		return gun.firedPellets.get(gun.firedPellets.size() - 1);
	}
}
