/**
 * 
 */
package br.odb.littlehelper3d;

import br.odb.derelict.engine.bzk3.android.AndroidGameActor;


/**
 * @author monty
 *
 */
public interface GameDelegate {
	public void setGameEngine( GameEngine engine );
	void onMapChange( String oldMapName, String newMapName, GameWorld world );
	void update( GameWorld world );
	void onStart( GameWorld world );
	void onSectorEntered( GameWorld world, AndroidGameActor actor, GameSector sector );
	boolean onActionAboutToBePerformed( GameActor actor,  int action );
	public boolean shouldOpenDoor( GameDoor gameDoor);
	public void refreshView();
}
