package br.odb.derelict2d.game;

import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.gameworld.Place;

/**
 * @author monty
 *
 */
public class DerelictGameSession  {
	private int currentLevel;
	private int dificulty;
	
	private DerelictGameSession(DerelictGameConfiguration derelictGameConfiguration) {
		reset();
	}
	

	private void reset() {
		currentLevel = 0;
		dificulty = 0;		
	}


	public static DerelictGameSession startNewSession() {
		return new DerelictGameSession( new DerelictGameConfiguration() );
	}


	public Place getCurrentLevel() {

		return new TotautisSpaceStation();
	}

}
