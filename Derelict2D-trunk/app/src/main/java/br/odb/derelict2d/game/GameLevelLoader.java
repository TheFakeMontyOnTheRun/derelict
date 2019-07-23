package br.odb.derelict2d.game;


import br.odb.gameapp.FileServerDelegate;

class GameLevelLoader {

	static GameLevel loadLevel(int currentLevel, FileServerDelegate delegate, GameLevelParser parser) {

		return parser.getLevel( delegate );
	}
}
