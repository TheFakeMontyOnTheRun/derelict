package br.odb.gamelib.android;

import android.content.Context;
import android.media.MediaPlayer;
import br.odb.gameapp.AbstractMediaPlayer;

/**
 * @author monty
 *
 */
class AndroidMediaPlayer extends AbstractMediaPlayer {

	private final MediaPlayer mp;

	/**
	 * 
	 */
	public AndroidMediaPlayer( Context context, int resId ) {
		mp = MediaPlayer.create( context, resId);
	}

	/* (non-Javadoc)
	 * @see br.odb.gameapp.AbstractMediaPlayer#loop()
	 */
	
	public void loop() {
		mp.stop();
		mp.setLooping( true );
		mp.start();
	}

	/* (non-Javadoc)
	 * @see br.odb.gameapp.AbstractMediaPlayer#play()
	 */
	
	public void play() {
		mp.start();
	}

	/* (non-Javadoc)
	 * @see br.odb.gameapp.AbstractMediaPlayer#stop()
	 */
	
	public void stop() {
		mp.stop();
	}
}
