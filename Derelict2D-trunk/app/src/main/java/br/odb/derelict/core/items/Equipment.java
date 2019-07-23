package br.odb.derelict.core.items;

import br.odb.gameworld.ActiveItem;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Item;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;

public class Equipment extends ActiveItem implements EletroMagnecticActive {

	public Equipment(String name) {
		super(name);
		
		setActive( true );
		setDescription( "All these equipment looks the same. Doesn't look valuable for me." );
	}

	@Override
	public void useWith(Item item) {
		if (item instanceof Destructive ) {
			this.setActive(false);
			this.setIsDepleted(true);
		}
	}
	
	@Override
	public void use(CharacterActor user) throws ItemActionNotSupportedException {
		throw new ItemActionNotSupportedException( "Don't know how. Maybe you should stick with your mission." );
	}	
}
