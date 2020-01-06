package br.odb.derelict.core.items;

import br.odb.gameutils.Direction;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Item;
import br.odb.gameworld.Location;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;

public class PlasmaPellet extends Item implements Destructive {

	public static final long REGULAR_SPEED_TO_COVER_ONE_CELL = 1;
	private static final String NAME = "plasma-pellet";
	private final Direction direction;

	public PlasmaPellet(Direction d, Location place) {
		super(NAME);
		setDescription("a fast moving high-energy cluster of particles. Can be tissue damaging if handled. Otherwise, not severely dangerous.");
		this.direction = d;
		this.location = place;
		setPickable(false);
	}

	@Override
	public void wasUsedOn(Item item1) throws ItemActionNotSupportedException {
		throw new ItemActionNotSupportedException("Can't do it!");
	}

	@Override
	public void use(CharacterActor user) throws ItemActionNotSupportedException {
		throw new ItemActionNotSupportedException("Can't do it!");
	}

	@Override
	public void update(long milisseconds) {

		super.update(milisseconds);

		Location newPlace = location.getConnections()[direction.ordinal()];

		if (newPlace == null) {

			for (Item i : location.getCollectableItems()) {
				try {
					i.useWith(this);
				} catch (ItemActionNotSupportedException ignored) {
				}
			}
			this.setIsDepleted(true);
		} else {
			location = newPlace;
		}
	}
}
