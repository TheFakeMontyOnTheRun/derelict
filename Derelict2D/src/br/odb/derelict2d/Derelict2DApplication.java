package br.odb.derelict2d;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import br.odb.derelict.core.DerelictGame;
import br.odb.gamerendering.rendering.AssetManager;
import br.odb.libsvg.SVGParsingUtils;
import br.odb.utils.FileServerDelegate;

public class Derelict2DApplication extends Application implements
		FileServerDelegate {

	public DerelictGame game;
	private AssetManager resManager;

	@Override
	public void onCreate() {
		super.onCreate();

		// StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		// .detectAll().penaltyLog().penaltyDeath().build());
		// StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
		// .penaltyLog().penaltyDeath().build());

		resManager = new AssetManager();
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
			resManager
					.putGraphic("metal-plate", SVGParsingUtils
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

			resManager.putGraphic("intro-comics0",
					SVGParsingUtils.readSVG(openAsset("chapters/intro1.svg")));
			resManager.putGraphic("intro-comics1",
					SVGParsingUtils.readSVG(openAsset("chapters/intro2.svg")));
			resManager.putGraphic("intro-comics2",
					SVGParsingUtils.readSVG(openAsset("chapters/intro3.svg")));

			for (int c = 0; c < 32; ++c) {

				resManager.putGraphic(
						"ending" + c,
						SVGParsingUtils.readSVG(openAsset("chapters/ending" + c
								+ ".svg")));
			}

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

		resManager.addResId("coughf", R.raw.coughf);
		resManager.addResId("coughm", R.raw.coughm);
		resManager.addResId("coughdeathf", R.raw.coughdeathf);
		resManager.addResId("coughdeathm", R.raw.coughdeathm);

		startNewGame();
	}

	public void startNewGame() {
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
	public InputStream openAsset(int resId) throws IOException {
		return getResources().openRawResource(resId);
	}

	@Override
	public InputStream openAsset(String filename) throws IOException {
		return getAssets().open(filename);
	}

	@Override
	public OutputStream openAsOutputStream(String filename) throws IOException {
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
}
