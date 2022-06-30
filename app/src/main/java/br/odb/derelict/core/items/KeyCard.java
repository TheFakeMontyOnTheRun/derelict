package br.odb.derelict.core.items;

import br.odb.derelict.core.Clearance;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Item;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;

public class KeyCard extends Item implements MagnecticActive {

	public final Clearance clearance;

	public KeyCard(Clearance clearance) {
		super("keycard-for-" + clearance);
		setDescription("clearance for " + clearance + " clearance level.");
		this.clearance = clearance;
		this.weight = 1;
	}

	@Override
	public void use(CharacterActor user) throws ItemActionNotSupportedException {
		throw new ItemActionNotSupportedException("This is not *usable*. You just need to carry it around");
	}

	@Override
	public void useWith(Item item1) throws ItemActionNotSupportedException {
		throw new ItemActionNotSupportedException("This is not *usable*. You just need to carry it around");
	}
}
