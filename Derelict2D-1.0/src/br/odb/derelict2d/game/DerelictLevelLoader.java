/**
 * 
 */
package br.odb.derelict2d.game;

import br.odb.utils.FileServerDelegate;

/**
 * @author monty
 *
 */
public class DerelictLevelLoader extends GameLevelLoader {
	public static GameLevel loadLevel(int currentLevel, FileServerDelegate delegate ) {
		
		return GameLevelLoader.loadLevel( currentLevel, delegate, new DerelictLevelParser() );
	}
}
