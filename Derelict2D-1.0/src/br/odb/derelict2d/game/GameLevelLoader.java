package br.odb.derelict2d.game;

import br.odb.utils.FileServerDelegate;

public class GameLevelLoader {

	public static GameLevel loadLevel(int currentLevel, FileServerDelegate delegate, GameLevelParser parser ) {

		GameLevel toReturn = parser.getLevel( delegate );

		return toReturn;
	}
}
