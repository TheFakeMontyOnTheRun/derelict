package br.odb.derelict.core.locations;

import br.odb.gameworld.ActiveItem;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;

public class DaedalusSpaceShip extends Spaceship {


	public static final String DEFAULT_NAME = "LSS DAEDALUS"; 
	public static final int INITIAL_AMMO_AVAILABLE = 50;

	public static class Ignition extends ActiveItem {
		
		public static final String NAME = "ship-ignition-key";



		public Ignition() {
			super( NAME );
			weight = 0.0f;
			setDescription( "token needed to ignite the ship's computer and thrusters" );
		}
		
		
		
		/* (non-Javadoc)
		 * @see br.odb.gamelib.gameworld.Item#use(br.odb.gamelib.gameworld.CharacterActor)
		 */
		@Override
		public void use(CharacterActor user) throws ItemActionNotSupportedException {
			
			turnKey();
			super.use(user);
			
		}



		private void turnKey() {
			if ( carrier.getLocation() instanceof DaedalusSpaceShip ) {
				( ( DaedalusSpaceShip ) carrier.getLocation() ).operative = true;
			}			
		}



		/* (non-Javadoc)
		 * @see br.odb.gamelib.gameworld.ActiveItem#activate()
		 */
		@Override
		public ActiveItem activate() {
			
			turnKey();
			
			return super.activate();
		}
	}

	public DaedalusSpaceShip() {
		super(DEFAULT_NAME);
		setDescription("My trusty old scrap ship. Built it myself. Still leaks fuel like a drunken horse, but it's mine, damn it! Well, at least until some of fines I have to pay results in repo men knocking my door."); 
	}
}
