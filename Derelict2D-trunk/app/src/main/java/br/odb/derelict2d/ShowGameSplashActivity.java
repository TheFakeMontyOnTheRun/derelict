package br.odb.derelict2d;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import br.odb.gamelib.android.AndroidUtils;
import br.odb.gamelib.android.GameView;

public class ShowGameSplashActivity extends Activity implements OnClickListener {

	private MediaPlayer theme;
	private GameView gvSplash;
	private GameView gvLogo;
	private Button playBtn;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_game_splash);

		playBtn = findViewById(R.id.btnPlay);

		gvSplash = findViewById(R.id.gvSplash);
		gvLogo = findViewById(R.id.gvLogo);

		gvSplash.setVisibility(View.INVISIBLE);
		gvLogo.setVisibility(View.INVISIBLE);

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

						playBtn.setText("Play");
						playBtn.setOnClickListener(ShowGameSplashActivity.this);
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

		Intent intent = new Intent(this, RootGameMenuActivity.class);
		this.startActivityForResult(intent, 1);
	}
}
