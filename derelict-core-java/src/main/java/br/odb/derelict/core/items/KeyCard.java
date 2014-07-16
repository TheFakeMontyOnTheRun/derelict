package br.odb.derelict.core.items;

import br.odb.derelict.core.Clearance;
import br.odb.gameworld.Item;

public class KeyCard extends Item implements MagnecticActive {

	public final Clearance clearance;

	public KeyCard(Clearance clearance) {
		super( "keycard-for-" + clearance); 
		setDescription( "clearance for " + clearance +  " clearance level." );
		this.clearance = clearance;
		this.weight = 1;
	}
}
