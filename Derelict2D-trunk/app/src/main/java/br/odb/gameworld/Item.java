package br.odb.gameworld;

import br.odb.gameutils.Updatable;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;

public class Item implements Updatable {

	public static final String PICK_DENIAL_MESSAGE = "Object is not pickable. It's either stuck or it is completely impossible to pick it up.";
	public static final String TOGGLE_DENIAL_MESSAGE = "Maybe you should revise your thoughts on this object. Are you sure you want to 'activate' this?";
	private final String name;
	public float weight;
	public Location location;
	protected CharacterActor carrier;
	private String description;
	private boolean depleted;
	private boolean pickable;

	public Item(String name) {
		this.name = name;
		pickable = true;
	}

	public String getDescription() {
		return description;
	}

	public Item setDescription(String description) {
		this.description = description;

		return this;
	}

	public String getUseItemSound() {
		return "click";
	}

	public String getDropSound() {
		return "drop";
	}

	public String getPickSound() {
		return "pick";
	}

	public String getUsedOnSound() {
		return "click";
	}

	@Override
	public String toString() {
		return getName() + " - " + description;
	}

	protected void setIsDepleted(boolean depleted) {
		this.depleted = depleted;
	}

	public String getName() {
		return name;
	}

	public void use(CharacterActor user) throws ItemActionNotSupportedException {

	}

	public boolean isDepleted() {
		return depleted;
	}

	public boolean isPickable() {
		return pickable;
	}

	public Item setPickable(boolean pickable) {
		this.pickable = pickable;
		return this;
	}

	public void wasUsedOn(Item item1) throws ItemActionNotSupportedException {
	}

	public void useWith(Item item1) throws ItemActionNotSupportedException {

		if (item1 == this) {
			throw new ItemActionNotSupportedException("Using something on itself sounds kinda crazy, you know? This reminds me of a old joke about putting something into itself, so you can use it while you're using it.");
		}

		item1.wasUsedOn(this);
	}

	@Override
	public void update(long milisseconds) {

	}

}
