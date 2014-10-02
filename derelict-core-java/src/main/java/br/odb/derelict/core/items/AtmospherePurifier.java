package br.odb.derelict.core.items;

import br.odb.gameworld.ActiveItem;
import br.odb.gameworld.Item;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;

public class AtmospherePurifier extends ActiveItem {

	public static final String SUIT_NAME = "atmosphere-purifier";

	public AtmospherePurifier() {
		super( SUIT_NAME ); 
		
		setDescription( "atmosphere purifier - makes sure the air entering your gear is the best. Or how that was, 50 years ago, when this was manufactured.");
	}
	
	@Override
	public String getTurnOffSound() {	
		return "atmosphereoff";
	}
	
	@Override
	public String getTurnOnSound() {		
		return "atmosphereon";
	}
	
	@Override
	public void wasUsedOn(Item item1) throws ItemActionNotSupportedException {
		throw new ItemActionNotSupportedException( "Can't do it!" );
	}
}