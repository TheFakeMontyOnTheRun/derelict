package br.odb.derelict.core.items;

import br.odb.gameworld.Item;

public abstract class ValuableItem extends Item {
	ValuableItem(String name) {
		super(name);
	}

	public abstract float getWorth();
}
