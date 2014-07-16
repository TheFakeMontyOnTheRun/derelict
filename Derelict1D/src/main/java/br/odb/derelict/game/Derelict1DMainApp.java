package br.odb.derelict.game;

import br.odb.derelict.core.DerelictGame;

/**
 * 
 */

/**
 * @author monty
 * 
 */
public class Derelict1DMainApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new DerelictGame().createDefaultClient().setAppName("DERELICT1D")
				.setAuthorName("Daniel Monteiro")
				.setLicenseName("3 Clause BSD").setReleaseYear(2014).start();
	}
}
