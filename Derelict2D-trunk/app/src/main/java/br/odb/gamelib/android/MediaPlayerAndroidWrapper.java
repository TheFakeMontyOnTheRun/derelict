package br.odb.gamelib.android;

import android.media.MediaPlayer;

/**
 * @author monty
 *
 */
class MediaPlayerAndroidWrapper {

	private final MediaPlayer mp;
	
	
	public MediaPlayerAndroidWrapper( MediaPlayer mp ) {
		this.mp = mp;
	}
	
	
	public void setLooping(boolean b) {
		mp.setLooping( b );
	}


	
	public boolean isPlaying() {
		return mp.isPlaying();
	}

	
	public void stop() {
		mp.stop();
	}

	
	public void start(float l, float r) {
		start();
		setVolume( l, r );		
	}

	
	private void start() {

		mp.start();
	}

	
	private void setVolume(float l, float r) {
		mp.start();
		mp.setVolume( l, r );
	}
}
