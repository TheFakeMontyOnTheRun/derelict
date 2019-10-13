package br.odb.derelict.core.items;

import br.odb.gameworld.Item;

class ItemContainer extends Item {

	Item contains;

	ItemContainer(Item contained) {
		super("plastic-pipes");
		this.contains = contained;
	}

	void dropItem() {
		if (contains != null) {
			location.addItem(contains);
		}
		contains = null;
	}
}
