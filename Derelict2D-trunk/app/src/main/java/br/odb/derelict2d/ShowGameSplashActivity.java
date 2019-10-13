package br.odb.derelict2d;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

import br.odb.gamelib.android.AndroidUtils;
import br.odb.gamelib.android.GameView;

public class ShowGameSplashActivity extends Activity implements OnClickListener {

	private MediaPlayer theme;
	private GameView gvSplash;
	private GameView gvLogo;
	private CheckBox chkSound;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_show_game_splash);

		chkSound = findViewById(R.id.chkSound);

		chkSound.setChecked(((Derelict2DApplication) getApplication())
				.mayEnableSound());


		gvSplash = findViewById(R.id.gvSplash);
		gvLogo = findViewById(R.id.gvLogo);

		gvSplash.setVisibility(View.INVISIBLE);
		gvLogo.setVisibility(View.INVISIBLE);

		findViewById(R.id.btnExploreStation).setOnClickListener(this);
		findViewById(R.id.btnAbout).setOnClickListener(this);
		findViewById(R.id.btnHowToPlay).setOnClickListener(this);

		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	}

	private void startLoading() {

		new Thread(new Runnable() {
			@Override
			public void run() {

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						initImage();
						gvSplash.postInvalidate();
						gvLogo.postInvalidate();

						gvSplash.setVisibility(View.VISIBLE);
						gvLogo.setVisibility(View.VISIBLE);
					}
				});
			}
		}).start();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		startLoading();

		if (theme == null
				&& ((Derelict2DApplication) getApplication()).mayEnableSound()) {
			theme = MediaPlayer.create(this, R.raw.derelicttheme);
			theme.setLooping(true);
			theme.start();
		}
	}

	private void initImage() {

		Derelict2DApplication app = ((Derelict2DApplication) getApplication());

		AndroidUtils.initImage(gvLogo, "logo", app.getAssetManager());
		AndroidUtils.initImage(gvSplash, "title", app.getAssetManager());
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		finish();
	}

	@Override
	protected void onPause() {
		if (theme != null) {
			theme.stop();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (theme != null) {
			theme.stop();
		}
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {

		if (theme != null) {
			theme.stop();
		}

		Intent intent;
		Bundle bundle = new Bundle();
		bundle.putString("hasSound", chkSound.isChecked() ? "y" : "n");

		switch (v.getId()) {

			case R.id.btnExploreStation:
				((Derelict2DApplication) this.getApplication()).startNewGame();
				intent = new Intent(this, ExploreStationActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				break;

			case R.id.btnHowToPlay:
				intent = new Intent(this, ShowHowToPlayActivity.class);
				startActivity(intent);
				break;
			case R.id.btnAbout:
				intent = new Intent(this, ShowCreditsActivity.class);
				startActivity(intent);
				break;
		}
	}
}
