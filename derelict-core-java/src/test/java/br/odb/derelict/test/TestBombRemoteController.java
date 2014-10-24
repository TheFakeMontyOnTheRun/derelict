package br.odb.derelict.test;

import org.junit.Assert;
import org.junit.Test;

import br.odb.derelict.core.items.BombRemoteController;
import br.odb.derelict.core.items.Equipment;
import br.odb.derelict.core.items.TimeBomb;
import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.gameworld.exceptions.InvalidLocationException;
import br.odb.gameworld.exceptions.ItemActionNotSupportedException;
import br.odb.gameworld.exceptions.ItemNotFoundException;

public class TestBombRemoteController {

	@Test
	public void testElectromagneticInterference() throws ItemNotFoundException,
			InvalidLocationException {
		TotautisSpaceStation station0 = new TotautisSpaceStation();
		TimeBomb bomb0 = (TimeBomb) station0.getItem("time-bomb");
		Equipment le = (Equipment) station0
				.getItem("electric-experiment");
		BombRemoteController controller = new BombRemoteController(bomb0);

		le.setActive(true);
		Assert.assertFalse(station0.destroyed);
		le.location.addItem(bomb0);
		Assert.assertFalse(station0.destroyed);
		bomb0.toggle();
		controller.activate();
		Assert.assertFalse(station0.destroyed);

		le.setActive(false);
		controller.activate();
		Assert.assertTrue(station0.destroyed);

	}

	@Test
	public void testBombController() throws ItemNotFoundException,
			InvalidLocationException {
		TotautisSpaceStation station0 = new TotautisSpaceStation();
		TimeBomb bomb0 = (TimeBomb) station0.getItem("time-bomb");

		BombRemoteController controller = new BombRemoteController(bomb0);

		Assert.assertFalse(station0.destroyed);
		station0.getLocation("hangar").addItem(bomb0);
		Assert.assertFalse(station0.destroyed);
		bomb0.toggle();
		controller.activate();
		Assert.assertTrue(station0.destroyed);

		station0 = new TotautisSpaceStation();
		bomb0 = (TimeBomb) station0.getItem("time-bomb");
		controller = new BombRemoteController(bomb0);
		station0.getLocation("hangar").addItem(bomb0);
		bomb0.toggle();
		controller.activate();
		Assert.assertTrue(station0.destroyed);

		station0 = new TotautisSpaceStation();
		bomb0 = (TimeBomb) station0.getItem("time-bomb");
		controller = new BombRemoteController(bomb0);
		station0.getLocation("hangar").addItem(bomb0);
		bomb0.toggle();
		controller.toggle();
		Assert.assertTrue(station0.destroyed);

		station0 = new TotautisSpaceStation();
		bomb0 = (TimeBomb) station0.getItem("time-bomb");
		controller = new BombRemoteController(bomb0);
		station0.getLocation("hangar").addItem(bomb0);
		bomb0.toggle();
		try {
			controller.use(null);
		} catch (ItemActionNotSupportedException e) {
			Assert.fail();
		}
		Assert.assertTrue(station0.destroyed);
	}
}
