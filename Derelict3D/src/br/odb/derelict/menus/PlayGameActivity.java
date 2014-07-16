package br.odb.derelict.menus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import br.odb.derelict.R;
import br.odb.derelict.engine.bzk3.android.AndroidGameAssetManager;
import br.odb.derelict.engine.bzk3.android.EngineView;
import br.odb.derelict.engine.bzk3.android.SensorOrientedSVGViewer;
import br.odb.derelict.game.DemoGameDelegate;
import br.odb.gameapp.GameAssetManager;
import br.odb.littlehelper3d.GameSession;
import br.odb.utils.FileServerDelegate;

/*
 * 
 * What concerns here?
 * This must represent the activity of playing a game and nothing more. Thats in playing a game?
 * 1 - You respect the rules
 * 2 - You follow (and influence) its flow
 * 3 - this class has also to mediate the game and the underlying OS.
 * 
 * 1 and 2 are delegated to a game session. 3 is dealt here
 * */
public class PlayGameActivity extends Activity implements FileServerDelegate {

	private static PlayGameActivity instance;
	private SensorManager sensorManager;
	private EngineView engineView;	
	MediaPlayer musicPlayer;	
	private DemoGameDelegate delegate;
	private GameSession session;
	public AndroidGameAssetManager gameAssetManager;

	@Override
	protected void onStart() {

		super.onStart();
		
		if ( engineView != null && sensorManager != null )
			sensorManager.unregisterListener( engineView );		
		
		initWithData( "totautis_floor1.opt.level" );
//		initWithData( "testeless.level" );
		
		session.start();
		session.tryRestoreState();
		
//		musicPlayer = MediaPlayer.create( this, R.raw.bolero );
//		musicPlayer.start();		
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);		
		instance = this;
		overridePendingTransition( R.anim.dissolve_from, R.anim.dissolve_into );
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		gameAssetManager = new AndroidGameAssetManager( this );
		
		//this is the real game
		delegate = new DemoGameDelegate();	
		
		session = new GameSession( delegate );		
		session.clearSavedStates( this );
	}
	
	private void initWithData( String path ) {
		
		engineView = new EngineView( this, this );		
		setContentView( engineView );
		SensorOrientedSVGViewer hud;
		
		hud = new SensorOrientedSVGViewer( this, 20, 10 );		
		hud.init( "hand.svg" );
		addContentView( hud, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT ) );
		sensorManager.registerListener( hud, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),	SensorManager.SENSOR_DELAY_NORMAL);
		
		hud = new SensorOrientedSVGViewer( this, 4, 2 );
		hud.init( "astrohud.svg" );
		addContentView( hud, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT ) );
		sensorManager.registerListener( hud, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),	SensorManager.SENSOR_DELAY_NORMAL);
		
		session.setView( engineView );
		session.startNewLevel( path );	

		sensorManager.registerListener( engineView, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),	SensorManager.SENSOR_DELAY_NORMAL);
	}


	@Override
	protected void onDestroy() {
		
		stop();		
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		stop();
		super.onBackPressed();
	}
	
	@Override
	protected void onPause() {
		session.saveState();
		super.onPause();
	}

	private void stop() {
		engineView.stop();		
	}
	
	@Override
	protected void onStop() {
		engineView.destroy();
		super.onStop();
	}

	@Override
	public InputStream openAsInputStream(String filename) throws IOException {
			return getAssets().open( filename );
	}

	@Override
	public OutputStream openAsOutputStream(String filename) throws IOException {

		return null;
	}

	public static Context getInstance() {

		return instance;
	}


	@Override
	public InputStream openAsset(String filename) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public InputStream openAsset(int resId) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}


	public String readTextFully(String filename) {
		
		String buffer = "";
		StringBuilder builder = new StringBuilder();				
		
		BufferedReader br;
		
		try {
			br = new BufferedReader(new InputStreamReader( ( ( PlayGameActivity ) PlayGameActivity.getInstance() ).openAsInputStream( filename ) ) );
			while ( buffer != null ) {
				builder.append( buffer );
				builder.append( "\n" );
				buffer = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return builder.toString();
	}
	
	@Override
	public void log(String tag, String string) {
		Log.d( tag, string );		
	}

}
