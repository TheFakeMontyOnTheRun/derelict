package br.odb.derelict2d;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import br.odb.derelict.core.DerelictGame;
import br.odb.derelict2d.RootGameMenuActivity.DificultyLevel;
import br.odb.derelict2d.game.Derelict2DTotautisSpaceStation;
import br.odb.derelict2d.game.GameLevel;
import br.odb.gameapp.ApplicationClient;
import br.odb.gameapp.ConsoleApplication;
import br.odb.gameapp.GameUpdateDelegate;
import br.odb.gamerendering.rendering.AssetManager;
import br.odb.utils.FileServerDelegate;

//Here's the model - just a small view glue allowed
public class ExploreStationActivity extends Activity implements
		FileServerDelegate, ApplicationClient, GameUpdateDelegate,
		DerelictGame.EndGameListener,
		OnClickListener {

	public TextToSpeech tts;
	boolean shouldPlaySound;

	ArrayList<GameUpdateDelegate> updateDelegates = new ArrayList<GameUpdateDelegate>();
	HashMap<String, MediaPlayer> mediaPlayers = new HashMap<String, MediaPlayer>();

	GameLevel currentLevel;
	DerelictGame game;
	AssetManager resManager;
	MediaPlayer playerSound;
	private long lastTimeCough = -1;
	DificultyLevel level;
	
	// MediaPlayer music;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent;
		intent = getIntent();

		String hasSpeech = intent.getExtras().getString("speech");
		String hasSound = intent.getExtras().getString("hasSound");
		
		if (hasSpeech != null && hasSpeech.equals("y")) {
			tts = ((Derelict2DApplication) getApplication()).tts;
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_explore_station);

		// findViewById(R.id.btnInfo).setOnClickListener(this);

		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LOW_PROFILE);

		resManager = ((Derelict2DApplication) getApplication())
				.getAssetManager();

		game = ((Derelict2DApplication) getApplication()).game;
		game.endGameListener = this;
		game.createDefaultClient();
		game.setGameUpdateDelegate(this);

		game.hero.setGender( "m" );

		currentLevel = new Derelict2DTotautisSpaceStation(game.station, this);

		level = DificultyLevel.values()[intent.getExtras().getInt("aid")];

		for (String cmd : level.aid) {
			game.sendData(cmd);
		}

		shouldPlaySound = (hasSound != null && hasSound.equals("y"));



		if (shouldPlaySound) {
			playerSound = MediaPlayer.create(this, R.raw.playersounds);
			// music = MediaPlayer.create(this, R.raw.ravelbolero);
		}

		game.setApplicationClient(this);

		showInfoDialog();
		update();
	}

	@Override
	public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {

		event.getText().add(game.getTextOutput());

		return true;
	}

	@Override
	protected void onPause() {

		// if (music != null) {
		//
		// music.pause();
		// }

		if (playerSound != null) {

			playerSound.pause();
		}

		super.onPause();
	}

	@Override
	protected void onResume() {

		if (shouldPlaySound) {

			// music.start();
			playerSound.start();
			playerSound.setLooping(true);
		}

		update();

		super.onResume();
	}

	@Override
	protected void onDestroy() {

		// if (music != null) {
		//
		// music.stop();
		// }

		if (playerSound != null) {

			playerSound.stop();
		}

		if (tts != null) {
			tts.stop();
		}

		super.onDestroy();
	}

	@Override
	public InputStream openAsInputStream(String filename) throws IOException {

		return getAssets().open(filename);
	}

	@Override
	public InputStream openAsset(int resId) throws IOException {
		return getResources().openRawResource(resId);
	}

	@Override
	public void printWarning(String msg) {

		msg = msg.substring(0, 1).toUpperCase() + msg.substring(1);

		Toast.makeText(this, msg.replace('-', ' '), Toast.LENGTH_SHORT).show();
        say( msg );
	}

	@Override
	public String getInput(String msg) {
		final StringBuilder toReturn = new StringBuilder();

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(msg);

		final EditText input = new EditText(this);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				toReturn.append(input.getText().toString());
			}
		});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		builder.show();

		return toReturn.toString();
	}

	@Override
	public void alert(String string) {

		string = string.substring(0, 1).toUpperCase() + string.substring(1);

		Toast.makeText(this, string.replace('-', ' '), Toast.LENGTH_SHORT).show();
		say( string );
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		finish();
	}

	@Override
	public InputStream openAsset(String filename) throws IOException {

		return getAssets().open(filename);
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
		
		game.station.update( level.defaultPenaultyTime );

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
	public void log(String tag, String string) {
		Log.d(tag, string);

	}

	@Override
	public void sendQuit() {
		finish();
	}

	@Override
	public boolean isConnected() {
		return this.isTaskRoot();
	}

	public void addUpdateListener(GameUpdateDelegate delegate) {
		updateDelegates.add(delegate);
	}

	@Override
	public String openHTTP(String url) {
		return ConsoleApplication.defaultJavaHTTPGet(url, this);
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
	public int chooseOption(String arg0, String[] arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public FileServerDelegate getFileServer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printError(String s) {
        say( s );

	}

	@Override
	public void printVerbose(String s) {
        say( s );

	}

	@Override
	public String requestFilenameForOpen() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String requestFilenameForSave() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setClientId(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void shortPause() {
		// TODO Auto-generated method stub

	}

	@Override
	public OutputStream openAsOutputStream(String arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		FragmentManager fm = getFragmentManager();
		InfoDialog dialog = new InfoDialog();
		Bundle args = new Bundle();
		// args.putString("name", item.getName());
		// args.putString("desc", item.getDescription());
		dialog.setArguments(args);
		dialog.show(fm, "fragment_info_dialog");
	}
	
	public void say( String text ) {
		if (tts != null) {
			tts.speak( text, TextToSpeech.QUEUE_FLUSH, null);
		}
	}

	private void showInfoDialog() {

		final FragmentManager fm = getFragmentManager();
		final ShowGameIntroDialogFragment gameIntro = new ShowGameIntroDialogFragment();
		Bundle args = new Bundle();
		args.putString("desc", DerelictGame.GAME_STORY1 + "\n\n\n"
				+ DerelictGame.GAME_STORY2 + "\n\n\n" + DerelictGame.GAME_RULES);
		gameIntro.setArguments(args);

        if (tts != null) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    gameIntro.show(fm, "show_game_intro");
                }
            }, 100 );
        } else {
            gameIntro.show(fm, "show_game_intro");
        }
	}

	@Override
	public void printNormal(String s) {
	}

	public void stopTalking() {
		if (tts != null) {
			tts.stop();
		}		
	}
}
