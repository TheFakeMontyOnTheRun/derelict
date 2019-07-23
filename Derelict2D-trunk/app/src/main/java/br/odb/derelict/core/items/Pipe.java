package br.odb.derelict.core.items;

import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Item;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;

public class Pipe extends ItemContainer {


    public Pipe(Item contained) {
        super(contained);

        setDescription("Just a regular pipe, taking something somewhere.");

        setPickable(false);
    }

    @Override
    public void use(CharacterActor user) throws ItemActionNotSupportedException {
        throw new ItemActionNotSupportedException("Can't do it!");
    }

    @Override
    public void useWith(Item item1) throws ItemActionNotSupportedException {
        if (item1 instanceof Destructive
                || ((item1 instanceof MagnecticActive) && (contains instanceof MagnecticActive))) {
            dropItem();
        } else {
            super.useWith(item1);
        }
    }
}
