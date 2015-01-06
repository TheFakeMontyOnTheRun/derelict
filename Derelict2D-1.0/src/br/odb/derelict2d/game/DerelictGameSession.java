/**
 * 
 */
package br.odb.derelict2d.game;

import br.odb.derelict.core.locations.TotautisSpaceStation;
import br.odb.gameapp.GameSession;
import br.odb.gameapp.SaveStateNotFound;
import br.odb.gameworld.Place;

/**
 * @author monty
 *
 */
public class DerelictGameSession extends GameSession {
	int currentLevel;
	int dificulty;
	
	public DerelictGameSession() {
		reset();
	}
	
	public DerelictGameSession(
			DerelictGameConfiguration derelictGameConfiguration) {
		// TODO Auto-generated constructor stub
	}

	public void close() {
		
	}


	@Override
	public void reset() {
		currentLevel = 0;
		dificulty = 0;		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restore() throws SaveStateNotFound {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearSavedSession() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	public static DerelictGameSession startNewSession() {
		return new DerelictGameSession( new DerelictGameConfiguration() );
	}


	public Place getCurrentLevel() {

		return new TotautisSpaceStation();
	}

}
