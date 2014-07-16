package br.odb.derelict.engine.bzk3.android;


import br.odb.derelict.R;
import br.odb.derelict.menus.PlayGameActivity;
import br.odb.gameapp.PositionalMediaPlayer;
import br.odb.littlehelper3d.GameActor;
import br.odb.utils.math.Vec3;

/**
 * 
 * @author daniel
 */
public class AndroidGameActor extends GameActor {
	
	public void setPlayable(boolean b) {
		
		super.setPlayable( b );
		
		if ( b ) {
			
			walkSound = PositionalMediaPlayer.getFor( new Vec3(), R.raw.step, ( (PlayGameActivity) PlayGameActivity.getInstance() ).gameAssetManager );			
			shotSound = PositionalMediaPlayer.getFor( new Vec3(), R.raw.shot, ( (PlayGameActivity) PlayGameActivity.getInstance() ).gameAssetManager );
			
//			breatheSound = PositionalMediaPlayer.getFor( new Vec3(), R.raw.breathe );			
//			beatSound = PositionalMediaPlayer.getFor( new Vec3(), R.raw.heartbeat );			
//			beatSound.bindPosition( getPosition() );
//			beatSound.playContinuously();
//			breatheSound.bindPosition( getPosition() );
//			breatheSound.playContinuously();
			
			
			playerSound = PositionalMediaPlayer.getFor( new Vec3(), R.raw.playersounds, ( (PlayGameActivity) PlayGameActivity.getInstance() ).gameAssetManager );			
			playerSound.bindPosition( getPosition() );
			playerSound.playContinuously();
			
			shotSound.bindPosition( getPosition() );
			walkSound.bindPosition( getPosition() );
		}
	}	
}
