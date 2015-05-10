package br.odb.derelict.game;

import br.odb.derelict.core.DerelictGame;

/**
 * 
 */

/**
 * @author monty
 * 
 */
public class Derelict1DTelnetApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new Thread( new DerelictGame().setApplicationClient(new TelnetClientServer())
				.setAppName("DERELICT1D").setAuthorName("Daniel Monteiro")
				.setLicenseName("3-Clause BSD").setReleaseYear(2014) ).start();
	}
}
