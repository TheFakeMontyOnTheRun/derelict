package br.odb.derelict2d.game;

import br.odb.utils.FileServerDelegate;

public interface GameLevelParser {
	GameLevel getLevel( FileServerDelegate delegate);
}
