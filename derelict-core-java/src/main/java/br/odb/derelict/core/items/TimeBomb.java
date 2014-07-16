package br.odb.derelict.core.items;

import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.gameworld.ActiveItem;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Item;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;
import br.odb.utils.Updatable;

public class TimeBomb extends ActiveItem implements Updatable {

	private static final String NAME = "time-bomb";
	int timeToGoOff;

	public TimeBomb(int timeToGoOff) {
		super( NAME ); 
		setDescription("time-programmable Halogen bomb."); 
		this.timeToGoOff = timeToGoOff;
		this.weight = 20.0f;
	}

	public void blow() {
		this.setIsDepleted(true);

		if (location != null) {

			((TotautisSpaceStation) this.location.place).destroyed = true;
		}
	}
	
	@Override
	public void wasUsedOn(Item item1) throws ItemActionNotSupportedException {
		throw new ItemActionNotSupportedException( "No point in doing so" );
	}


	@Override
	public void update(long milisseconds) {

		if (!isActive()) {
			return;
		}

		timeToGoOff -= milisseconds;

		if (timeToGoOff <= 0) {
			blow();
		}
	}

	@Override
	public void use(CharacterActor user) {
		super.use(user);

		toggle();
	}
}
