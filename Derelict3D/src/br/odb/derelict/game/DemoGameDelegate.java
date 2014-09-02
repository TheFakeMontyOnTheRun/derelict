package br.odb.derelict.game;

import java.util.ArrayList;

import android.util.Log;
import br.odb.derelict.engine.bzk3.android.AndroidGameActor;
import br.odb.littlehelper3d.GameActor;
import br.odb.littlehelper3d.GameDelegate;
import br.odb.littlehelper3d.GameDoor;
import br.odb.littlehelper3d.GameEngine;
import br.odb.littlehelper3d.GameSector;
import br.odb.littlehelper3d.GameWorld;

public class DemoGameDelegate implements GameDelegate {
	
	
	private GameEngine engine;
//	static boolean showStory;
//	ArrayList< GameSector > visitedParents = new ArrayList< GameSector>();
//	ArrayList< String > collectedItems = new ArrayList< String>();
	
	
	public void setGameEngine( GameEngine engine ) {
		
		this.engine = engine;
	}

	@Override
	public void onMapChange(String oldMapName, String newMapName,
			GameWorld world) {


	}

	@Override
	public void update(GameWorld world) {

		
	}

	@Override
	public void onStart(GameWorld world) {
		
	
//		int count = world.getTotalActors();
//		AndroidGameActor actor;
//		
//		for (int c = 0; c < count; ++c) {
//			
//			actor = (AndroidGameActor) world.getActor( c );
//		
//			switch ( actor.getActorClass() ) {
//				case 0:
//					actor.speed = 3.0f;
//					break;
//				default:
//					actor.speed = 5.0f;
//					break;
//			}
//		}

	}

	@Override
	public void onSectorEntered(GameWorld world, AndroidGameActor actor, GameSector sector ) {

//		Log.d( "derelict", "sector:" +  sector.getParent() );		
//		
//		if ( !visitedParents.contains( sector.cachedParent ) ) {
//			visitedParents.add( sector.cachedParent );
//			if ( sector.cachedParent.getExtraInformation() != null ) {
//				Log.d( "Derelict3D", "derelict location:" + sector.cachedParent.getExtraInformation() );
//			}
//		}
//		
//		
//		if ( sector.cachedParent.getExtraInformation() != null && !collectedItems.contains( sector.cachedParent.getExtraInformation() ) ) {
//			collectedItems.add( sector.cachedParent.getExtraInformation() );
//			
//			if ( sector.cachedParent.getExtraInformation().contains( "showPersonalLogTerminal()") ) {
//				
//				String className = br.odb.derelict.menus.TerminalAccessActivity.class.toString();
//				className = className.substring( className.indexOf( ' ' ) ).trim();
//				engine.showScreen( className );
//			}
//		}
		
//		if  ( sector.getParent() == 17 ) {
//			
//			engine.requestMapChange( "portas.opt" );
//		}
	}

	@Override
	public boolean onActionAboutToBePerformed(GameActor actor, int action ) {
		
//		if ( action == ActorConstants.ACTION1.ordinal() ) {
//
//			GameSector current;
//			GameWorld world;	
//			int sector;
//			
//			world = engine.getWorld();
//			sector = actor.getCurrentSector();
//			current = world.getSector( sector );
//			current.openAllDoors();
//			refreshView();
//		}
		
		return true;
	}

	@Override
	public boolean shouldOpenDoor( GameDoor gameDoor) {
		
		boolean result;
		
//		if ( gameDoor.getSector() == 25 ) {
//			result = collectedItems.contains( "blue key" ); 
//		} else		
		result = true;	
//		
		return result;
	}

	@Override
	public void refreshView() {
		engine.getListener().needsToRefreshWindow( false );		
	}
}
