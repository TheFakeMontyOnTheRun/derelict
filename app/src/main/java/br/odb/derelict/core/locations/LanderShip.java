package br.odb.derelict.core.locations;

public class LanderShip extends Spaceship {

	public static final String DEFAULT_NAME = "RLS BOHR 2";

	public LanderShip() {
		super(DEFAULT_NAME);

		setDescription("A rescue lander ship. Only for emergencies. Named after some Niels Bohr scientist guy or whatever. Some drops on the carpet and I don't even want know what it is, but I guess I already know. Ick.");
	}
}
