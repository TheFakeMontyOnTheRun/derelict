package br.odb.derelict2d;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import br.odb.gamelib.android.GameView;
import br.odb.gamerendering.rendering.AssetManager;
import br.odb.gamerendering.rendering.DisplayList;
import br.odb.gamerendering.rendering.RenderingNode;
import br.odb.gamerendering.rendering.SVGRenderingNode;
import br.odb.libsvg.SVGGraphic;
import br.odb.utils.Rect;
import br.odb.utils.math.Vec2;

public class ShowGameSplashActivity extends Activity implements OnClickListener {

	private MediaPlayer theme;
	GameView gvSplash;
	GameView gvLogo;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_game_splash);

		findViewById(R.id.btnPlay).setOnClickListener(this);

		gvSplash = (GameView) findViewById(R.id.gvSplash);
		gvLogo = (GameView) findViewById(R.id.gvLogo);

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LOW_PROFILE);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		initImage();
		gvSplash.postInvalidate();
		gvLogo.postInvalidate();
	}

	void initImage() {

		DisplayList dl = new DisplayList("dl");
		AssetManager resManager = ((Derelict2DApplication) getApplication())
				.getAssetManager();

		SVGGraphic graphic = resManager.getGraphics("title");
		SVGGraphic logo = resManager.getGraphics("logo");
		Rect rect;
		float scale = 1;
		Vec2 trans = new Vec2();

		if (gvSplash.getWidth() > 0 && gvSplash.getHeight() > 0) {

			Rect bound = graphic.makeBounds();

			// não me interessa a parte acima da "página".
			float newWidth = bound.p1.x;
			float newHeight = bound.p1.y;

			if (newWidth > newHeight) {
				scale = gvSplash.getWidth() / newWidth;
				trans.y = (gvLogo.getHeight() - (bound.p1.y * scale)) / 2.0f;
			} else {
				scale = gvSplash.getHeight() / newHeight;
				trans.x = (gvLogo.getWidth() - (bound.p1.x * scale)) / 2.0f;
			}
		}

		graphic = graphic.scale(scale, scale);
		logo = logo.scale(2.0f * scale, 2.0f * scale);
		SVGRenderingNode node = new SVGRenderingNode(graphic, "title");
		SVGRenderingNode node2 = new SVGRenderingNode(logo, "logo");
		node.translate.set(trans);

		rect = logo.makeBounds();
		node2.translate.set((gvLogo.getWidth() - rect.getDX()) / 2.0f,
				(gvLogo.getHeight() - rect.getDY()) / 2.0f);

		dl.setItems(new RenderingNode[] { node });

		gvSplash.setRenderingContent(dl);

		if (theme == null
				&& ((Derelict2DApplication) getApplication()).mayEnableSound()) {
			theme = MediaPlayer.create(this, R.raw.derelicttheme);
			theme.setLooping(true);
			theme.start();
		}

		dl = new DisplayList("dl");
		dl.setItems(new RenderingNode[] { node2 });
		gvLogo.setRenderingContent(dl);

		gvSplash.postInvalidate();
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
		// gvSplash.invalidate();
		if (theme != null) {

			theme.stop();
		}

		Intent intent = new Intent(this, RootGameMenuActivity.class);
		this.startActivityForResult(intent, 1);
	}
}
