package br.odb.core;

import org.junit.Assert;
import org.junit.Test;

import br.odb.derelict.core.Astronaut;
import br.odb.derelict.core.commands.TurnToDirectionCommand;
import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.gameapp.ApplicationClient;
import br.odb.gameutils.Direction;

public class TurnToDirectionCommandTest {

	@Test
	public void test() {
		TotautisSpaceStation station = new TotautisSpaceStation();
		Astronaut astro = station.getAstronaut();

		ApplicationClient client = new DummyApplicationClient();
		TurnToDirectionCommand cmd = new TurnToDirectionCommand();

		for (Direction d : Direction.values()) {

			try {
				cmd.run(station, astro, d.simpleName, client);
				Assert.assertEquals(d, astro.direction);
			} catch (Exception e) {
				Assert.fail();
			}
		}

		for (Direction d : Direction.values()) {

			try {
				cmd.run(station, astro, d.prettyName, client);
				Assert.assertEquals(d, astro.direction);
			} catch (Exception e) {
				Assert.fail();
			}
		}
	}
}
