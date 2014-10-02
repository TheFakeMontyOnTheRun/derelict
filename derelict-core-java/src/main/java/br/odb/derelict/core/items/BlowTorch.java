package br.odb.derelict.core.items;

import br.odb.gameworld.ActiveItem;
import br.odb.gameworld.Item;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;
import br.odb.utils.Utils;

public class BlowTorch extends ActiveItem implements Toxic, Destructive {

	private static final String NAME = "blowtorch";

	float fuel;

	String originalDescription = "precision vintage-but-rather-well-kept metal cutter (fuel: %d).";

	public BlowTorch(float initialFluel) {
		super( BlowTorch.NAME );

		fuel = initialFluel;
		updateFuelStatus();

	}

	@Override
	public String getTurnOnSound() {

		return "blowtorch-turned-on";
	}

	@Override
	public String getUseItemSound() {

		return "blowtorch-used";
	}

	public float getFuel() {
		return fuel;
	}

	private void updateFuelStatus() {

		if (fuel < 0.0f) {
			fuel = 0.0f;
		}

		weight = 20 + (fuel / 10);

		setDescription(String.format(originalDescription, ( ( int )fuel ) ));
		setIsDepleted(fuel <= 0.0f);
	}

	@Override
	public void wasUsedOn(Item item1) throws ItemActionNotSupportedException {
		if ( isActive() ) {
			
			super.wasUsedOn(item1);
			
			fuel -= 10.0f;
			
			updateFuelStatus();
		} else {
			throw new ItemActionNotSupportedException( "Inactive" );
		}
	}

	@Override
	public void update(long MS) {
		if (isActive()) {
			fuel -= 0.1f * (MS * Utils.SECOND_IN_MILISSECONDS);
		}
	}

	@Override
	public float getToxicity() {
		return isActive() ? 0.25f : 0.0f;
	}

	@Override
	public float getDestructivePower() {
		return 50;
	}
}
