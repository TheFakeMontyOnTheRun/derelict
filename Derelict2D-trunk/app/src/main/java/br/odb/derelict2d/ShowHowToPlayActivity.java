package br.odb.derelict2d;

import android.app.Activity;
import android.os.Bundle;

import br.odb.gamelib.android.AndroidUtils;
import br.odb.gamelib.android.GameView;

public class ShowHowToPlayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_how_to_play);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		AndroidUtils.initImage((GameView) findViewById(R.id.gvHowToPlayHero), "hero-centered", ((Derelict2DApplication) getApplication()).getAssetManager(), 200, 200, "");
		AndroidUtils.initImage((GameView) findViewById(R.id.gvHowToPlayItem), "plasma-gun", ((Derelict2DApplication) getApplication()).getAssetManager());
		AndroidUtils.initImage((GameView) findViewById(R.id.gvHowToPlayPick), "icon-pick", ((Derelict2DApplication) getApplication()).getAssetManager());
		AndroidUtils.initImage((GameView) findViewById(R.id.gvHowToPlayToggle), "icon-toggle", ((Derelict2DApplication) getApplication()).getAssetManager());
		AndroidUtils.initImage((GameView) findViewById(R.id.gvHowToPlayUseWith), "icon-use-with", ((Derelict2DApplication) getApplication()).getAssetManager());
		AndroidUtils.initImage((GameView) findViewById(R.id.gvHowToPlayUse), "icon-use", ((Derelict2DApplication) getApplication()).getAssetManager());
	}
}
