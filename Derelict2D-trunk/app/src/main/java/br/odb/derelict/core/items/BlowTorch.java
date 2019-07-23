package br.odb.derelict.core.items;

import br.odb.gameworld.ActiveItem;
import br.odb.gameworld.CharacterActor;
import br.odb.gameworld.Item;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;

public class BlowTorch extends ActiveItem implements Toxic, Destructive {

	private static final String NAME = "blowtorch";
	private static final float DEFAULT_FUEL_USAGE = 10.0f;
	private static final float DEFAULT_IDLE_FUEL_USAGE = 0.0001f;
	private static final float DEFAULT_TOXICITY = 0.25f;
	private static final int DEFAULT_DESTRUCTIVE_POWER = 50;
	private static final String ORIGINAL_DESCRIPTION = "precision vintage-but-rather-well-kept metal cutter (fuel: %.2f).";

	private float fuel;


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
	public void use(CharacterActor user) throws ItemActionNotSupportedException {
		throw new ItemActionNotSupportedException( "This can't be used on it's on." + ( isActive() ? " Go look for something to cutout with this." : " Activate it and use it on other stuff." ) );
	}
	
	@Override
	public String getUseItemSound() {

		return isActive() ? "blowtorch-used" : "";
	}

	public float getFuel() {
		return fuel;
	}
	
	@Override
	public ActiveItem toggle() {
		
		super.toggle();
		
		updateFuelStatus();
		
		return this;
	}
	
	private void updateFuelStatus() {

		if (fuel < 0.0f) {
			fuel = 0.0f;
		}

		weight = 20 + (fuel / 10);

		setDescription(String.format(ORIGINAL_DESCRIPTION,  fuel ) );
		setIsDepleted(fuel <= 0.0f);
		setActive( isActive() && ( !isDepleted() ) );
	}

	@Override
	public void wasUsedOn(Item item1) throws ItemActionNotSupportedException {
		if ( isActive() ) {
			
			super.wasUsedOn(item1);
			
			fuel -= DEFAULT_FUEL_USAGE;
			
			updateFuelStatus();
		} else {
			throw new ItemActionNotSupportedException( "Inactive" );
		}
	}

	@Override
	public void update(long MS) {
		if (isActive()) {
			fuel -= DEFAULT_IDLE_FUEL_USAGE * MS;
			updateFuelStatus();
		}
	}

	@Override
	public float getToxicity() {
		return isActive() ? DEFAULT_TOXICITY : 0.0f;
	}

	@Override
	public float getDestructivePower() {
		return DEFAULT_DESTRUCTIVE_POWER;
	}
}
