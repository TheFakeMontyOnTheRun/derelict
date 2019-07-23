package br.odb.derelict2d.game;

import br.odb.gameworld.Location;
import br.odb.gameutils.Rect;


/**
 * @author monty
 *
 */
class Location2D extends Location {
	
	public Location2D(String name, Rect space ) {
		super(name);
		
		space = new Rect( space );	
	}

	public Rect space;
}
