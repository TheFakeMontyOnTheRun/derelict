/**
 * 
 */
package br.odb.derelict.engine.bzk3.android;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import br.odb.derelict.menus.PlayGameActivity;
import br.odb.gameapp.GameAssetManager;
import br.odb.gameapp.MediaPlayerWrapper;
import br.odb.libscene.ObjMesh;
import br.odb.utils.FileServerDelegate;

/**
 * @author monty
 *
 */
public class AndroidGameAssetManager extends GameAssetManager {

	private Context context;
	private AndroidMeshFactory factory;
	
	
	public AndroidGameAssetManager( Context context ) {
		this.context = context;
		factory = new AndroidMeshFactory();
	}
	
	
	/* (non-Javadoc)
	 * @see br.odb.derelict.engine.bzk3.android.GameAssetManager#createMediaPlayer(android.content.Context, int)
	 */
	@Override
	public MediaPlayerWrapper createMediaPlayer( int resId) {
		return new MediaPlayerAndroidWrapper( MediaPlayer.create(context, resId) );
	}

	@Override
	public boolean isSilentModeEnabled() {
		android.media.AudioManager am = (android.media.AudioManager) PlayGameActivity.getInstance().getSystemService(Context.AUDIO_SERVICE);

		switch (am.getRingerMode()) {
		    case android.media.AudioManager.RINGER_MODE_SILENT:
		    case android.media.AudioManager.RINGER_MODE_VIBRATE:
		    	return true;
		    case android.media.AudioManager.RINGER_MODE_NORMAL:
		        return false;
		}
		
		return false;
	}


	public ObjMesh meshForName(String string) {

		ObjMesh obj = new ObjMesh();
		try {
			obj.internalize( PlayGameActivity.getInstance().getAssets().open( "torpedo.obj" ), (FileServerDelegate) PlayGameActivity.getInstance(), factory );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return obj;
	}
}
