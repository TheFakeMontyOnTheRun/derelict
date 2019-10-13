package br.odb.derelict2d;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import br.odb.derelict.core.DerelictGame;
import br.odb.gameapp.ApplicationClient;
import br.odb.gamerendering.rendering.AssetManager;

public class ExploreStationActivity extends Activity implements
		ApplicationClient, GameUpdateDelegate,
		DerelictGame.EndGameListener,
		OnClickListener {

	private final ArrayList<GameUpdateDelegate> updateDelegates = new ArrayList<>();
	private final HashMap<String, MediaPlayer> mediaPlayers = new HashMap<>();
	AssetManager resManager;
	private boolean shouldPlaySound;
	private DerelictGame game;
	private MediaPlayer playerSound;
	private long lastTimeCough = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_explore_station);

		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LOW_PROFILE);

		Intent intent = getIntent();
		String hasSound = intent.getExtras().getString("hasSound");

		resManager = ((Derelict2DApplication) getApplication())
				.getAssetManager();

		game = ((Derelict2DApplication) getApplication()).game;
		game.endGameListener = this;
		game.hero.setGender("m");

		shouldPlaySound = (hasSound != null && hasSound.equals("y"));

		if (shouldPlaySound) {
			playerSound = MediaPlayer.create(this, R.raw.playersounds);
		}

		game.setApplicationClient(this);

		showInfoDialog();
		update();
	}

	@Override
	protected void onPause() {
		if (playerSound != null) {

			playerSound.pause();
		}

		super.onPause();
	}

	@Override
	protected void onResume() {

		if (shouldPlaySound) {
			playerSound.start();
			playerSound.setLooping(true);
		}

		update();

		super.onResume();
	}

	@Override
	protected void onDestroy() {

		if (playerSound != null) {
			playerSound.stop();
		}

		super.onDestroy();
	}

	@Override
	public void alert(String string) {

		string = string.substring(0, 1).toUpperCase() + string.substring(1);

		Toast.makeText(this, string.replace('-', ' '), Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		finish();
	}

	@Override
	public void update() {

		if (game.hero.toxicity > 99.9f) {
			playMedia("coughdeathm", "*cough*");
		} else if (game.getTextOutput().contains("*cough*")
				&& lastTimeCough < game.station.elapsedTime) {
			playMedia("cough" + game.hero.getGender(), "*cough*");
			lastTimeCough = game.station.elapsedTime;
		}

		game.station.update(1250);

		for (GameUpdateDelegate gup : updateDelegates) {
			gup.update();
		}
	}

	@Override
	public void playMedia(String uri, String alt) {

		if (!shouldPlaySound) {
			return;
		}

		MediaPlayer mp;

		if (this.mediaPlayers.containsKey(uri)) {
			mp = mediaPlayers.get(uri);
		} else {
			mp = MediaPlayer.create(this, resManager.getResIdForUri(uri));
			mediaPlayers.put(uri, mp);
		}

		mp.start();
	}

	@Override
	public void sendQuit() {
		finish();
	}

	public void addUpdateListener(GameUpdateDelegate delegate) {
		updateDelegates.add(delegate);
	}

	@Override
	public void onGameEnd(int ending) {
		Intent intent = new Intent(this, ShowOutcomeActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("outcome", DerelictGame.finalMessage[ending]);
		bundle.putString("ending", Integer.toString(ending));
		intent.putExtras(bundle);
		this.startActivityForResult(intent, 1);
	}

	@Override
	public void onClick(View v) {
		FragmentManager fm = getFragmentManager();
		InfoDialog dialog = new InfoDialog();
		Bundle args = new Bundle();
		dialog.setArguments(args);
		dialog.show(fm, "fragment_info_dialog");
	}

	private void showInfoDialog() {

		final FragmentManager fm = getFragmentManager();
		final ShowGameIntroDialogFragment gameIntro = new ShowGameIntroDialogFragment();
		Bundle args = new Bundle();
		args.putString("desc", DerelictGame.GAME_STORY1 + "\n\n\n"
				+ DerelictGame.GAME_STORY2 + "\n\n\n" + DerelictGame.GAME_RULES);
		gameIntro.setArguments(args);

		gameIntro.show(fm, "show_game_intro");
	}

}
