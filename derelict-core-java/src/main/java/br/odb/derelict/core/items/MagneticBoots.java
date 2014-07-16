package br.odb.derelict.core.items;

import br.odb.gameworld.ActiveItem;
import br.odb.gameworld.Item;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;

public class MagneticBoots extends ActiveItem implements MagnecticActive {

	public static final String NAME = "magboots";
	final float chanceToFail = 0.1f;
	
	@Override
	public String getTurnOnSound() {
		return "magbootson";
	}
	
	@Override
	public String getUsedOnSound() {
	
		return "magbootsuse";
	}
	
	@Override
	public String getUseItemSound() {

		return "magbootsuse";
	}
	
	
	@Override
	public void wasUsedOn(Item item1) throws ItemActionNotSupportedException {
		if ( isActive() ) {
			
			super.wasUsedOn(item1);
		} else {
			throw new ItemActionNotSupportedException( "nothing happens");
		}
	}
	
	
	@Override
	public String getTurnOffSound() {
		return "magbootsoff";
	}

	public MagneticBoots() {
		super( NAME ); 
		setDescription("boots with strong electro-magnets. Ideal for walking on low-gravity situations - as long as the surface in question is metallic (like most of the surfaces here)."); 
	}
}
