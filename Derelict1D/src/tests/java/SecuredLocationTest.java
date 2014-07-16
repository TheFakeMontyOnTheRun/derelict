/**
 * 
 */
package br.odb.derelict.test;

import org.junit.Assert;
import org.junit.Test;

import br.odb.derelict.core.Clearance;
import br.odb.derelict.core.SecuredDoor;
import br.odb.derelict.core.SecuredLocation;
import br.odb.gameworld.Direction;
import br.odb.gameworld.Location;

/**
 * @author monty
 * 
 */
public class SecuredLocationTest {

	@Test
	public final void test() {
		SecuredLocation l1 = new SecuredLocation("place1");
		Location l2 = new Location("place2");
		SecuredLocation l3 = new SecuredLocation("place3");

		l1.setConnected(Direction.N, l2, Clearance.HIGH_RANK);
		l2.setConnected(Direction.N, l3);
		l2.setConnected(Direction.S, l1);
		l3.setConnected(Direction.S, l2);

		Assert.assertEquals(Clearance.HIGH_RANK,
				((SecuredDoor) l1.getDoor(Direction.N)).clearance);
		Assert.assertEquals(l1, l2.getConnections()[Direction.S.ordinal()]);
		Assert.assertEquals(l3, l2.getConnections()[Direction.N.ordinal()]);
		Assert.assertEquals(Clearance.DEFAULT_CLEARANCE,
				((SecuredDoor) l3.getDoor(Direction.S)).clearance);

		Assert.assertEquals(Clearance.DEFAULT_CLEARANCE, l1.rank);
		l1.setRank(Clearance.HIGH_RANK);
		Assert.assertEquals(Clearance.HIGH_RANK, l1.rank);
	}

}
