/**
 * 
 */
package br.odb.derelict.core.items;

import br.odb.gameworld.ActiveItem;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Item;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;

/**
 * @author monty
 * 
 */
public class MetalPlate extends ValuableItem {

	public static final float PLATE_WEIGHT_BAD_CUT = 15.0f;
	public static final float PLATE_WEIGHT_GOOD_CUT = 20.0f;
	public static final float PLATE_WEIGHT_REALLY_BAD_CUT = 10.0f;
	public static final float PLATE_WEIGHT_RAW = 30.0f;
	public static final String NAME = "metal-plate";

	
	@Override
	public String getUsedOnSound() {
		return "clink";
	}
	
	@Override
	public String getUseItemSound() {
		return "clink";
	}
	
	/**
	 * 
	 */
	public MetalPlate( String name ) {
		super( name ); 
		setDescription("A piece of metal that might be valuable."); 
		weight = PLATE_WEIGHT_RAW;
	}
	
	public MetalPlate() {
		this( NAME ); 
	}

	@Override
	public void use(CharacterActor user) throws ItemActionNotSupportedException {
		throw new ItemActionNotSupportedException( "This is too valuable to atempt anything else but selling later." );
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.odb.derelict.core.Item#use(br.odb.derelict.core.Item)
	 */
	@Override
	public void useWith(Item item2) throws ItemActionNotSupportedException {
		
		if (item2 instanceof BlowTorch || item2 instanceof PlasmaGun
				|| item2 instanceof PlasmaPellet) {

			boolean ifActivableIsActive = ( item2 instanceof ActiveItem && ( (ActiveItem) item2 ).isActive() ) || !( item2 instanceof ActiveItem ) ;

			if (!item2.isDepleted() && ifActivableIsActive ) {
				//TODO: rever de acordo com o poder destrutivo do item.
				if (item2 instanceof BlowTorch) {
					weight = PLATE_WEIGHT_GOOD_CUT;
				} else if (item2 instanceof PlasmaGun) {
					weight = PLATE_WEIGHT_BAD_CUT;
				} else if (item2 instanceof PlasmaPellet) {
					weight = PLATE_WEIGHT_REALLY_BAD_CUT;
				}
				super.useWith(item2);
				setPickable(true);
				return;
			}
		}

		throw new ItemActionNotSupportedException( "Don't give me your funny talk. You can't do that, smartass." );
	}

	@Override
	public float getWorth() {
		return weight;
	}
}
