package br.odb.derelict.core.items;

import br.odb.gameworld.ActiveItem;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Item;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;

public class BombRemoteController extends ActiveItem {

	private static final String NAME = "bomb-remote-controller";
	private final TimeBomb bomb;

	public BombRemoteController(TimeBomb bomb) {
		super( BombRemoteController.NAME ); 
		setDescription("The remote controller allows you to instantly detonate the bomb from very far (empirical evidence tells it works from as far as 0.5AU)."); 
		this.bomb = bomb;
	}

	@Override
	public void use(CharacterActor user) throws ItemActionNotSupportedException {

		super.use(user);
		triggerBomb();
	}

	@Override
	public void wasUsedOn(Item item1) throws ItemActionNotSupportedException {
		throw new ItemActionNotSupportedException( "Nonsense" );
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see br.odb.gamelib.gameworld.ActiveItem#activate()
	 */
	@Override
	public ActiveItem activate() {

		ActiveItem interferenceSource = null;

		for (Item i : bomb.location.getCollectableItems()) {
			if (i instanceof EletroMagnecticActive) {
				interferenceSource = (ActiveItem) i;
			}
		}

		if (interferenceSource == null || !interferenceSource.isActive()) {

			triggerBomb();
		}
		return super.activate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.odb.gamelib.gameworld.ActiveItem#toggle()
	 */
	@Override
	public ActiveItem toggle() {
		triggerBomb();
		return super.toggle();
	}

	private void triggerBomb() {
		bomb.blow();
	}
}
