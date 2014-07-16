package br.odb.derelict.engine.bzk3.android;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Toast;
import br.odb.derelict.engine.bzk3.android.geometry.GLESRenderer;
import br.odb.derelict.menus.PlayGameActivity;
import br.odb.derelict.menus.StoryTellingActivity;
import br.odb.gameapp.GameAssetManager;
import br.odb.gameapp.GameAudioManager;
import br.odb.gameapp.GameEngineController;
import br.odb.gameapp.GameResource;
import br.odb.gameworld.Actor;
import br.odb.libscene.Actor3D;
import br.odb.libscene.ActorConstants;
import br.odb.libscene.Sector;
import br.odb.littlehelper3d.GameActor;
import br.odb.littlehelper3d.GameDelegate;
import br.odb.littlehelper3d.GameEngine;
import br.odb.littlehelper3d.GameEngineListener;
import br.odb.littlehelper3d.GameEngineViewer;
import br.odb.littlehelper3d.GameSector;
import br.odb.littlehelper3d.GameWorld;
import br.odb.utils.FileServerDelegate;
import br.odb.utils.math.Vec3;

/**
 * @author Daniel "Monty" Monteiro
 * 
 * 
 * 
 *         Concerns: - Manages the render, so to allow it to only perform
 *         rendering efficiently - Takes user input - delegates gesture
 *         management to GestureManager
 * */
@SuppressLint("NewApi")
public class EngineView extends GLSurfaceView implements GameEngineViewer,
		GameEngineController, GameEngineListener, OnTouchListener,
		SensorEventListener, OnGestureListener {
	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	private GameAudioManager audioManager;
	/**
	 * 
	 */
	private GLESRenderer renderer;
	/***
	 * 
	 */
	GameEngine gameEngine;
	/**
	 * 
	 */
	private GameWorld world;
	/**
	 * 
	 */
	private boolean[] keyMap;
	/**
	 * 
	 */
	public Vec3 accel;
	/**
	 * 
	 */
	private GestureManager gestureManager;
	private FileServerDelegate fileServer;
	GestureDetector gestureDetector;
	GameDelegate delegate;
	GameAssetManager gam;

	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 * @param context
	 */
	public EngineView(Context context, FileServerDelegate server) {
		super(context);

	
		setEGLContextClientVersion(2);
		keyMap = new boolean[9];
		
		audioManager = GameAudioManager.getInstance();
		gam = ( (PlayGameActivity) PlayGameActivity.getInstance() ).gameAssetManager;
		audioManager.active = !gam.isSilentModeEnabled();

		renderer = new GLESRenderer(2);
		accel = renderer.accel;
		fileServer = server;

		gestureDetector = new GestureDetector(getContext(), this);
		gestureManager = new GestureManager(this);
		setFocusable(true);
		setClickable(true);
		setLongClickable(true);
		setFocusableInTouchMode(true);
		requestFocus();
		setRenderer(renderer);
		setOnTouchListener(this);
	}

	// ------------------------------------------------------------------------------------------------------------

	public void setGameDelegate(GameDelegate delegate) {

		this.delegate = delegate;
		gameEngine.setDelegate(delegate);
		delegate.setGameEngine(gameEngine);
	}

	/**
	 * 
	 * @return
	 */
	public GameWorld getWorld() {
		return world;
	}

	// ------------------------------------------------------------------------------------------------------------

	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	public void handleKeys(boolean[] keymap) {

		if (keymap[VirtualPad.KB_UP]) {

			renderer.getCameraActor().consume(ActorConstants.MOVE_N);
		}

		if (keymap[VirtualPad.KB_FIRE4]) {

			renderer.getCameraActor().consume(ActorConstants.ACTION1);
		}

		if (keymap[VirtualPad.KB_DOWN]) {

			renderer.getCameraActor().consume(ActorConstants.MOVE_S);
		}

		if (keymap[VirtualPad.KB_LEFT]) {

			renderer.getCameraActor().consume(ActorConstants.TURN_L);
			renderer.setAngle(renderer.getCameraActor().getAngleXZ());
			keymap[VirtualPad.KB_LEFT] = false;
		}

		if (keymap[VirtualPad.KB_RIGHT]) {

			renderer.getCameraActor().consume(ActorConstants.TURN_R);
			renderer.setAngle(renderer.getCameraActor().getAngleXZ());
			keymap[VirtualPad.KB_RIGHT] = false;
		}

		if (keymap[VirtualPad.KB_FIRE2]) {
			renderer.getCameraActor().consume(ActorConstants.MOVE_UP);

		}

		if (keymap[VirtualPad.KB_FIRE3]) {
			renderer.getCameraActor().consume(ActorConstants.MOVE_DOWN);
		}

		if (keymap[VirtualPad.KB_FIRE]) {
			renderer.getCameraActor().consume(ActorConstants.FIRE);
		}
	}

	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 * @param path
	 * @param detailFile
	 * @param server
	 */
	@SuppressWarnings("deprecation")
	public void load(String path, String detailFile, FileServerDelegate server) {
		System.out.println("<load>");

		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		float width;
		float height;

		if (android.os.Build.VERSION.SDK_INT < 13) {

			width = display.getWidth();
			height = display.getHeight();
		} else {
			Point size = new Point();
			display.getSize(size);
			width = size.x;
			height = size.y;
		}
		//
		// try {
		// renderer.clearScreenGeometry();
		//
		// // renderer.addToMovingGeometryToScreen( Decal.loadGraphic(
		// getResources().getAssets().open( "astrohud.bin" ), width, height ) );
		//
		// renderer.addToFixedGeometryToScreen( Decal.loadGraphic(
		// getResources().getAssets().open( "ref.bin" ), width, height ) );
		// // renderer.addToMovingGeometryToScreen( Decal.loadGraphic(
		// getResources().getAssets().open( "ref.bin" ), width, height ) );
		//
		// // renderer.addToFixedGeometryToScreen( Decal.loadGraphic(
		// getResources().getAssets().open( "astrohud_still.bin" ), width,
		// height ) );
		// // renderer.addToMovingGeometryToScreen( Decal.loadGraphic(
		// getResources().getAssets().open( "hand.bin" ), width, height ) );
		// } catch (IOException e) {
		//
		// e.printStackTrace();
		// }
		System.out.println("new world");
		world = new GameWorld();
		world.internalize(path, detailFile, server, gam );

		String previousMap = null;

		if (delegate != null)
			delegate.onMapChange(previousMap, path, world);

		System.out.println("new engine");
		gameEngine = new GameEngine(world, this);
		gameEngine.setTimeStep(50);
		renderer.setWorld(world);
		System.out.println("</load>");
	}

	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 * @param actor
	 */
	public void loadMeshForActor( GameActor actor, String meshName,
			FileServerDelegate fileServer) {

//		actor.setMesh( gam.meshForName( meshName ) );
	}

	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	public void loadComplete() {
		System.out.println("<loadComplete>");
		world.consolidate(delegate);
		AndroidGameActor actor;
		renderer.setWorld(world);
		int visibleSectors = 0;

		renderer.clearActors();

		for (Actor3D baseActor : world.getActorList()) {

			actor = (AndroidGameActor) baseActor;
			gameEngine.placeActor(actor, actor.currentSector);

			if (actor.actorClass == 1) {

				loadMeshForActor(actor, "ghost.obj", fileServer);
			}

			renderer.addToScene(actor);
		}

		for (Sector s : world) {

			if (s != null && !s.isMaster()) {

				visibleSectors++;
				renderer.addToScene((GameSector) s);
			}
		}

		renderer.setCurrentCamera((AndroidGameActor) world.getActor(world
				.getPlayerActorIndex()));
		audioManager.setListener((AndroidGameActor) renderer.getCameraActor());
		((AndroidGameActor) renderer.getCameraActor()).setPlayable(true);

		renderer.initBuffers(visibleSectors);

		Log.d("bzk3", "load complete complete with " + world.getTotalSectors()
				+ " sectors ");
		System.gc();

		if (delegate != null)
			delegate.onStart(world);

		gameEngine.start();
		gameEngine.loaded = true;
		Thread monitorThread = new Thread(gameEngine);
		monitorThread.setPriority(Thread.MAX_PRIORITY);
		monitorThread.start();
		renderer.attach();
		System.out.println("</loadComplete>");
	}

	// ------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_SPACE) {
			keyMap[VirtualPad.KB_FIRE4] = true;
		}

		if (keyCode == KeyEvent.KEYCODE_W
				|| keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			keyMap[VirtualPad.KB_UP] = true;
		}
		if (keyCode == KeyEvent.KEYCODE_S
				|| keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			keyMap[VirtualPad.KB_DOWN] = true;
		}
		if (keyCode == KeyEvent.KEYCODE_Q
				|| keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			keyMap[VirtualPad.KB_LEFT] = true;
		}
		if (keyCode == KeyEvent.KEYCODE_E
				|| keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			keyMap[VirtualPad.KB_RIGHT] = true;
		}
		if (keyCode == KeyEvent.KEYCODE_O) {
			keyMap[VirtualPad.KB_FIRE2] = true;
		}
		if (keyCode == KeyEvent.KEYCODE_L) {
			keyMap[VirtualPad.KB_FIRE3] = true;
		}
		if (keyCode == KeyEvent.KEYCODE_ENTER
				|| keyCode == KeyEvent.KEYCODE_MENU) {
			keyMap[VirtualPad.KB_FIRE] = true;
		}

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			gameEngine.stop();
			renderer.detach();
			gameEngine.destroy();
			deleteSavedState();
			((Activity) getContext()).finish();
		}

		// handleKeys(keyMap);

		return true;
	}

	public void saveState() {
		try {
			world.saveSnapshotAt(PlayGameActivity.getInstance().openFileOutput(
					"state", Context.MODE_PRIVATE));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void restoreState() {

		try {
			// gameEngine.pause();
			world.loadSnapshotAt(PlayGameActivity.getInstance().openFileInput(
					"state"));
			// renderer.setCurrentCamera( (GameActor) world.getActor(
			// world.getPlayerActorIndex() ) );
			renderer.setAngle(((AndroidGameActor) world.getActor(world
					.getPlayerActorIndex())).getAngleXZ());
			// gameEngine.resume();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_W
				|| keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			keyMap[VirtualPad.KB_UP] = false;
		}
		if (keyCode == KeyEvent.KEYCODE_S
				|| keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			keyMap[VirtualPad.KB_DOWN] = false;
		}
		if (keyCode == KeyEvent.KEYCODE_Q
				|| keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			keyMap[VirtualPad.KB_LEFT] = false;
		}
		if (keyCode == KeyEvent.KEYCODE_E
				|| keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			keyMap[VirtualPad.KB_RIGHT] = false;
		}
		if (keyCode == KeyEvent.KEYCODE_O) {
			keyMap[VirtualPad.KB_FIRE2] = false;
		}
		if (keyCode == KeyEvent.KEYCODE_L) {
			keyMap[VirtualPad.KB_FIRE3] = false;
		}
		if (keyCode == KeyEvent.KEYCODE_ENTER
				|| keyCode == KeyEvent.KEYCODE_MENU) {
			keyMap[VirtualPad.KB_FIRE] = false;
		}

		if (keyCode == KeyEvent.KEYCODE_SPACE) {
			keyMap[VirtualPad.KB_FIRE4] = false;
		}

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			gameEngine.stop();
			renderer.detach();
			gameEngine.destroy();
			deleteSavedState();
			((Activity) getContext()).finish();
		}

		handleKeys(keyMap);
		return true;
	}

	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {

		gestureDetector.onTouchEvent(event);
		gestureManager.onTouch(v, event);
		//
		// switch (event.getAction()) {
		//
		// case MotionEvent.ACTION_UP: {
		//
		// if ( gestureManager.getTimeSinceLastTouch() < 2000) {
		//
		// renderer.getCameraActor().consume(ActorConstants.FIRE);
		// }
		// }
		// break;
		// }
		return true;
	}

	/**
	 * 
	 */
	private void updateMovement() {

		handleKeys(keyMap);

		if (gestureManager.notReady())
			return;

		if (gestureManager.isTurningLeft()) {

			renderer.getCameraActor().consume(ActorConstants.TURN_L);
			renderer.setAngle(renderer.getCameraActor().getAngleXZ());
			gestureManager.resetMovement();
		}

		if (gestureManager.isTurningRight()) {

			renderer.getCameraActor().consume(ActorConstants.TURN_R);
			renderer.setAngle(renderer.getCameraActor().getAngleXZ());
			gestureManager.resetMovement();
		}

		if (gestureManager.isMovingUp()) {

			renderer.getCameraActor().consume(ActorConstants.MOVE_N);
		}

		if (gestureManager.isMovingDown()) {

			renderer.getCameraActor().consume(ActorConstants.MOVE_S);
		}
	}

	// ------------------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------------------

	public void stop() {

		gameEngine.stop();
	}

	@Override
	public void onActorAdded( GameActor actor) {

		renderer.addToScene(actor);
	}

	@Override
	public void needsToRefreshWindow(boolean fastRefresh) {

		renderer.needsToResetView(fastRefresh);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		accel.x = (event.values[0]);
		accel.y = (event.values[1]);
		accel.z = (event.values[2]);
	}

	@Override
	public void beforeTick() {

		updateMovement();
	}

	@Override
	public void requestMapChange(String mapName) {

		if (gameEngine != null) {

			deleteSavedState();
			renderer.detach();
			gameEngine.stop();
		}

		load(mapName, null, (FileServerDelegate) PlayGameActivity.getInstance());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gameEngine.start();
		loadComplete();
		renderer.attach();

	}

	public void deleteSavedState() {
		try {
			OutputStream state = PlayGameActivity.getInstance().openFileOutput(
					"state", Context.MODE_PRIVATE);
			state.write(0);
			state.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void showHistory(int index) {

		saveState();

		renderer.detach();
		gameEngine.stop();
		// gameEngine.destroy();
		Intent intent = new Intent(this.getContext(),
				StoryTellingActivity.class);
		getContext().startActivity(intent);
	}

	@Override
	public boolean[] updateControllerState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void refreshView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void fadeIn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void fadeOut() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setOverlay(GameResource res) {
		// TODO Auto-generated method stub

	}

	@Override
	public GameEngine getGameEngine() {

		return gameEngine;
	}

	@Override
	public void setAngle(float angleXZ) {

		renderer.setAngle(angleXZ);
	}

	@Override
	public void needsToRefreshLightning() {

		renderer.calculateLightning();
	}

	// --GestureManagerListener--
	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float velocityX,
			float velocityY) {

		// if ( velocityX < getWidth() / 4 )
		// renderer.getCameraActor().consume(ActorConstants.MOVE_E );
		//
		// if ( velocityX < -getWidth() / 4 )
		// renderer.getCameraActor().consume(ActorConstants.MOVE_W );
		//
		// if ( velocityY < getHeight() / 4 )
		// renderer.getCameraActor().consume(ActorConstants.MOVE_UP );
		//
		// if ( velocityY < -getHeight() / 4 )
		// renderer.getCameraActor().consume(ActorConstants.MOVE_DOWN );

		return true;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		renderer.getCameraActor().consume(ActorConstants.ACTION1);
	}

	@Override
	public boolean onScroll(MotionEvent me0, MotionEvent me1, float distanceX,
			float distanceY) {

		float width = getWidth();
		float height = getHeight();

		if (-distanceX > width / 4) {

			renderer.getCameraActor().consume(ActorConstants.TURN_R);
			renderer.setAngle(renderer.getCameraActor().getAngleXZ());

		} else if (-distanceX < -width / 4) {
			renderer.getCameraActor().consume(ActorConstants.TURN_L);
			renderer.setAngle(renderer.getCameraActor().getAngleXZ());
		} else if (distanceY > height / 2)
			renderer.getCameraActor().consume(ActorConstants.MOVE_N);
		else if (distanceY < -height / 2)
			renderer.getCameraActor().consume(ActorConstants.MOVE_S);

		return true;
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		renderer.getCameraActor().consume(ActorConstants.FIRE);
		return false;
	}

	@Override
	public void needsToDisplayMessage(String information) {

		Toast.makeText(getContext(), information, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void showScreen(String screenClass) {
		saveState();

		try {
			renderer.detach();
			gameEngine.stop();
			Intent intent;
			intent = new Intent(this.getContext(), Class.forName(screenClass));
			getContext().startActivity(intent);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void destroy() {
		gameEngine.stop();
		gameEngine.destroy();
	}
}
