package br.odb.derelict2d.game;


import br.odb.gameapp.FileServerDelegate;

/**
 * @author monty
 *
 */
class DerelictLevelLoader extends GameLevelLoader {
	public static GameLevel loadLevel(int currentLevel, FileServerDelegate delegate ) {
		
		return GameLevelLoader.loadLevel( currentLevel, delegate, new DerelictLevelParser() );
	}
}
