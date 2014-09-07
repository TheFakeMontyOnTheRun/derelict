package br.odb.derelict.core.items;

import br.odb.gameworld.Item;

public class ItemContainer extends Item {

	Item contains;

	public ItemContainer(String name, Item contained) {
		super(name);
		// Reads like prose!
		this.contains = contained;
	}

	void dropItem() {
		if ( contains != null ) {			
			location.addItem(contains);
		}
		contains = null;
	}
}
