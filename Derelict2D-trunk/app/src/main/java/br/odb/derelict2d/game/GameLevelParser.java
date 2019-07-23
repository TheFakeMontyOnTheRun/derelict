package br.odb.derelict2d.game;


import br.odb.gameapp.FileServerDelegate;

interface GameLevelParser {
	GameLevel getLevel( FileServerDelegate delegate);
}
