package br.odb.derelict2d;

import android.app.Application;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

import br.odb.derelict.core.DerelictGame;
import br.odb.gameapp.FileServerDelegate;
import br.odb.gamerendering.rendering.AssetManager;
import br.odb.libsvg.SVGParsingUtils;

public class Derelict2DApplication extends Application implements
		FileServerDelegate, TextToSpeech.OnInitListener {

	volatile public DerelictGame game;
	final private AssetManager resManager = new AssetManager();
	final ArrayList< String > notes = new ArrayList<>();
    public TextToSpeech tts;

    @Override
	public void onCreate() {
		super.onCreate();
        loadAssets();
	}
	
	private void loadAssets() {


		// StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        //         .detectAll().penaltyLog().penaltyDeath().build());
		// StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
        //         .penaltyLog().penaltyDeath().build());
		try {

			resManager.putGraphic("floor1", SVGParsingUtils
					.readSVG(openAsset("overview-map/floor1.svg")));
			resManager.putGraphic("floor2", SVGParsingUtils
					.readSVG(openAsset("overview-map/floor2.svg")));
			resManager.putGraphic("floor3", SVGParsingUtils
					.readSVG(openAsset("overview-map/floor3.svg")));
			resManager.putGraphic("heroGraphic", SVGParsingUtils
					.readSVG(openAsset("overview-map/astronaut-icon.svg")));
			resManager.putGraphic("hero-centered", SVGParsingUtils
					.readSVG(openAsset("overview-map/centered-astronaut.svg")));
			resManager.putGraphic("blowtorch",
					SVGParsingUtils.readSVG(openAsset("items/blowtorch.svg")));

			resManager.putGraphic("bomb-remote-controller", SVGParsingUtils
					.readSVG(openAsset("items/bomb-remote-controller.svg")));
			resManager.putGraphic("book",
					SVGParsingUtils.readSVG(openAsset("items/book.svg")));
			resManager
					.putGraphic("comm-system", SVGParsingUtils
							.readSVG(openAsset("items/comm-system.svg")));
			resManager.putGraphic("atmosphere-purifier", SVGParsingUtils
					.readSVG(openAsset("items/atmosphere-purifier.svg")));
			resManager.putGraphic("keycard-for-high-rank", SVGParsingUtils
					.readSVG(openAsset("items/keycard-for-high-rank.svg")));
			resManager.putGraphic("keycard-for-root-access", SVGParsingUtils
					.readSVG(openAsset("items/keycard-for-lab-access.svg")));
			resManager.putGraphic("keycard-for-low-rank", SVGParsingUtils
					.readSVG(openAsset("items/keycard-for-low-rank.svg")));
			resManager.putGraphic("lab-equipment", SVGParsingUtils
					.readSVG(openAsset("items/lab-equipment.svg")));
			resManager.putGraphic("electric-experiment", SVGParsingUtils
					.readSVG(openAsset("items/lab-equipment.svg")));
			resManager.putGraphic("backdrop", SVGParsingUtils
					.readSVG(openAsset("overview-map/backdrop.svg")));
			resManager.putGraphic("title",
					SVGParsingUtils.readSVG(openAsset("title.svg")));
			resManager.putGraphic("logo",
					SVGParsingUtils.readSVG(openAsset("logo.svg")));
			resManager.putGraphic("pattern",
					SVGParsingUtils.readSVG(openAsset("pattern.svg")));

			resManager.putGraphic("magboots",
					SVGParsingUtils.readSVG(openAsset("items/magboots.svg")));
			resManager.putGraphic("selection",
					SVGParsingUtils.readSVG(openAsset("items/selection.svg")));
			
			resManager
					.putGraphic("metal-plate", SVGParsingUtils
							.readSVG(openAsset("items/metal-plate.svg")));
			resManager
			.putGraphic("metal-scrap", SVGParsingUtils
					.readSVG(openAsset("items/metal-plate.svg")));
			resManager
			.putGraphic("metal-sheet", SVGParsingUtils
					.readSVG(openAsset("items/metal-plate.svg")));
			
			resManager.putGraphic("plasma-gun",
					SVGParsingUtils.readSVG(openAsset("items/plasma-gun.svg")));
			resManager.putGraphic("plasma-pellet", SVGParsingUtils
					.readSVG(openAsset("items/plasma-pellet.svg")));
			resManager.putGraphic("plastic-pipes", SVGParsingUtils
					.readSVG(openAsset("items/plastic-pipes.svg")));
			resManager.putGraphic("time-bomb",
					SVGParsingUtils.readSVG(openAsset("items/time-bomb.svg")));
			resManager.putGraphic("computer-stand", SVGParsingUtils
					.readSVG(openAsset("items/computer-stand.svg")));

			resManager.putGraphic("ship-ignition-key", SVGParsingUtils
					.readSVG(openAsset("items/ship-ignition-key.svg")));

			resManager
					.putGraphic("icon-move", SVGParsingUtils
							.readSVG(openAsset("action-icons/move.svg")));
			resManager
					.putGraphic("icon-turn", SVGParsingUtils
							.readSVG(openAsset("action-icons/turn.svg")));
			resManager
					.putGraphic("icon-pick", SVGParsingUtils
							.readSVG(openAsset("action-icons/pick.svg")));
			resManager.putGraphic("icon-use-with", SVGParsingUtils
					.readSVG(openAsset("action-icons/useWith.svg")));
			resManager.putGraphic("icon-use",
					SVGParsingUtils.readSVG(openAsset("action-icons/use.svg")));
			resManager
					.putGraphic("icon-drop", SVGParsingUtils
							.readSVG(openAsset("action-icons/drop.svg")));

			resManager.putGraphic("icon-toggle", SVGParsingUtils
					.readSVG(openAsset("action-icons/toggle.svg")));

			resManager.putGraphic("intro-comics2",
					SVGParsingUtils.readSVG(openAsset("chapters/intro3.svg")));

			resManager.putGraphic("logo_github",
					SVGParsingUtils.readSVG(openAsset("logo_github.svg")));

			resManager.putGraphic("logo_inkscape",
					SVGParsingUtils.readSVG(openAsset("logo_inkscape.svg")));
			
			resManager.putGraphic("beer",
					SVGParsingUtils.readSVG(openAsset("beer.svg")));


		} catch (IOException ex) {
			ex.printStackTrace();
		}

		resManager.addResId("atmosphereon", R.raw.atmosphereon);
		resManager.addResId("atmosphereoff", R.raw.atmosphereoff);
		resManager.addResId("magbootsuse", R.raw.magbootsused);
		resManager.addResId("magbootsoff", R.raw.magbootsoff);
		resManager.addResId("magbootson", R.raw.magbootson);
		resManager.addResId("bonk", R.raw.bonk);
		resManager.addResId("spooky1", R.raw.spooky1);
		resManager.addResId("spooky2", R.raw.spooky2);
		resManager.addResId("spooky3", R.raw.spooky3);
		resManager.addResId("click", R.raw.click);
		resManager.addResId("drop", R.raw.drop);
		resManager.addResId("pick", R.raw.pick);
		resManager.addResId("blowtorch-turned-on", R.raw.blowtorchon);
		resManager.addResId("blowtorch-used", R.raw.blowtorchuse);
		resManager.addResId("shot", R.raw.shot);

		resManager.addResId("coughm", R.raw.coughm);
		resManager.addResId("coughdeathm", R.raw.coughdeathm);

		startNewGame();
	}

	public void startNewGame() {
		notes.clear();
		notes.add( "New note" );
		notes.add( DerelictGame.GAME_STORY1 + DerelictGame.GAME_STORY2 );

		
		game = new DerelictGame();
		game.setAppName("DERELICT - THE GRAPHICAL ADVENTURE")
				.setAuthorName("Daniel Monteiro")
				.setLicenseName("3-Clause BSD").setReleaseYear(2014).init();
	}

	public AssetManager getAssetManager() {
		return resManager;
	}

	@Override
	public InputStream openAsInputStream(String filename) throws IOException {
		return getAssets().open(filename);
	}

	@Override
	public InputStream openAsset(int resId) {
		return getResources().openRawResource(resId);
	}

	@Override
	public InputStream openAsset(String filename) throws IOException {
		return getAssets().open(filename);
	}

	@Override
	public OutputStream openAsOutputStream(String filename) {
		return null;
	}

	@Override
	public void log(String tag, String string) {
		Log.d(tag, string);

	}

	public boolean mayEnableSound() {
		android.media.AudioManager am = (android.media.AudioManager) getSystemService(Context.AUDIO_SERVICE);

		switch (am.getRingerMode()) {
		case android.media.AudioManager.RINGER_MODE_SILENT:
		case android.media.AudioManager.RINGER_MODE_VIBRATE:
			return false;
		case android.media.AudioManager.RINGER_MODE_NORMAL:
			return true;
		default:
			return false;
		}
	}

    public void toggleSpeech() {

        if ( tts == null ) {
            tts = new TextToSpeech(this, this);
            Toast.makeText(this, "Text-To-Speech enabled. Please wait for spoken confirmation to enter game.",
                    Toast.LENGTH_SHORT).show();

        } else {
            tts = null;
            Toast.makeText(this, "Text-To-Speech disabled.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onInit(int code ) {
        if (code == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.getDefault());
            Toast.makeText(this, "Text-To-Speech working!.",
                    Toast.LENGTH_SHORT).show();
            tts.speak( "Text-To-Speech working!", TextToSpeech.QUEUE_FLUSH, null );
        } else {
            tts = null;
            Toast.makeText(this, "Failed to initialize TTS engine.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
