package br.odb.derelict2d;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.io.ObjectInputStream;
import java.util.List;

import br.odb.derelict.core.DerelictGame;
import br.odb.gamelib.android.AndroidUtils;
import br.odb.gamelib.android.GameView;
import br.odb.gamelib.android.geometry.GLES1TriangleFactory;
import br.odb.gamerendering.rendering.AssetManager;
import br.odb.gamerendering.rendering.DisplayList;
import br.odb.gamerendering.rendering.RenderingNode;
import br.odb.gamerendering.rendering.SVGRenderingNode;
import br.odb.libsvg.SVGGraphic;
import br.odb.utils.Rect;
import br.odb.utils.math.Vec2;
import br.odb.utils.math.Vec3;

public class ShowGameSplashActivity extends Activity implements OnClickListener {

	private MediaPlayer theme;
	GameView gvSplash;
	GameView gvLogo;
	private Button playBtn;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_game_splash);

		playBtn = (Button) findViewById(R.id.btnPlay);
		
		gvSplash = (GameView) findViewById(R.id.gvSplash);
		gvLogo = (GameView) findViewById(R.id.gvLogo);

		gvSplash.setVisibility( View.INVISIBLE );
		gvLogo.setVisibility( View.INVISIBLE );

		
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LOW_PROFILE);
		}
	}

    void startLoading() {

        new Thread(  new Runnable() {
            @Override
            public void run() {

              //  ((Derelict2DApplication) getApplication()).loadAssets();

                runOnUiThread( new Runnable() {

                    @Override
                    public void run() {
                        initImage();
                        gvSplash.postInvalidate();
                        gvLogo.postInvalidate();

                        gvSplash.setVisibility( View.VISIBLE );
                        gvLogo.setVisibility( View.VISIBLE );

                        playBtn.setText( "Play" );
                        playBtn.setOnClickListener( ShowGameSplashActivity.this);
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

	void initImage() {

        Derelict2DApplication app = ((Derelict2DApplication) getApplication());

        AndroidUtils.initImageScaled(gvLogo, "logo", app.getAssetManager(), 0.5f, 0.5f);
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
