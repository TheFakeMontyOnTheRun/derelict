package br.odb.derelict.core.items;

import br.odb.gameworld.Item;

public abstract class ValuableItem extends Item {
    public abstract float getWorth();

    ValuableItem(String name) {
        super(name);
    }
}
