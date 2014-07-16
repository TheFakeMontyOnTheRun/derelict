package br.odb.derelict2d.game;

import br.odb.utils.FileServerDelegate;

public class DerelictLevelParser implements GameLevelParser {

	@Override
	public GameLevel getLevel( FileServerDelegate delegate) {
		return new Derelict2DTotautisSpaceStation( delegate );
	}
}
