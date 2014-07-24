/**
 * 
 */
package br.odb.derelict.test;

import org.junit.Assert;
import org.junit.Test;

import br.odb.derelict.core.Astronaut;
import br.odb.derelict.core.items.BlowTorch;
import br.odb.derelict.core.items.MagneticBoots;
import br.odb.derelict.core.items.MetalPlate;
import br.odb.derelict.core.locations.TotautisSpaceStation;

/**
 * @author monty
 * 
 */
public class MetalPlateTest {

	@Test
	public void test() {

		MetalPlate metalPlate = (MetalPlate) new MetalPlate()
				.setPickable(false);
		
		Assert.assertNotNull( metalPlate.getUseItemSound() );
		
		BlowTorch blowTorch = (BlowTorch) new BlowTorch(100.0f).toggle();
		MagneticBoots magboots = new MagneticBoots();
		TotautisSpaceStation station = new TotautisSpaceStation();
		Astronaut hero = (Astronaut) station.getCharacter("hero");
		hero.getLocation().addItem(metalPlate);

		try {
			metalPlate.useWith( blowTorch );
			hero.getLocation().giveItemTo("metal-plate", hero);
		} catch (Exception e) {
			Assert.fail();
		}

		try {
			metalPlate.useWith( magboots );
			Assert.fail();
		} catch (Exception e) {
		}

		blowTorch = new BlowTorch(0);
		metalPlate = (MetalPlate) new MetalPlate().setPickable(false);
		try {
			blowTorch.useWith(metalPlate);
			hero.getLocation().giveItemTo("metal-plate", hero);
			Assert.fail();
		} catch (Exception e) {
		}
	}
}
