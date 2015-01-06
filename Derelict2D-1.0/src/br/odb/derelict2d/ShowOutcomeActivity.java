package br.odb.derelict2d;

import br.odb.gamelib.android.GameView;
import br.odb.gamerendering.rendering.AssetManager;
import br.odb.gamerendering.rendering.DisplayList;
import br.odb.gamerendering.rendering.RenderingNode;
import br.odb.gamerendering.rendering.SVGRenderingNode;
import br.odb.libsvg.SVGGraphic;
import br.odb.utils.Rect;
import br.odb.utils.math.Vec2;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

public class ShowOutcomeActivity extends Activity {

	MediaPlayer theme = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_outcome);
		
		( ( TextView ) findViewById( R.id.txtOutcome ) ).setText( getIntent().getExtras().getString( "outcome" ) );
		
		if (theme == null
				&& ((Derelict2DApplication) getApplication()).mayEnableSound()) {
			theme = MediaPlayer.create(this, R.raw.derelicttheme);
			theme.setLooping(true);
			theme.start();
		}		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		DisplayList dl = new DisplayList("dl");
		AssetManager resManager = ((Derelict2DApplication) getApplication())
				.getAssetManager();

		SVGGraphic graphic = resManager.getGraphics("logo");

		float scale = 1;
		Vec2 trans = new Vec2();

		GameView gvSplash = (GameView) findViewById( R.id.gvGameLogoOutcome );
		
		if (gvSplash.getWidth() > 0 && gvSplash.getHeight() > 0) {

			Rect bound = graphic.makeBounds();

			// não me interessa a parte acima da "página".
			float newWidth = bound.p1.x;
			float newHeight = bound.p1.y;

			if (newWidth > newHeight) {
				scale = gvSplash.getWidth() / newWidth;
				trans.y = (gvSplash.getHeight() - (bound.p1.y * scale)) / 2.0f;
			} else {
				scale = gvSplash.getHeight() / newHeight;
				trans.x = (gvSplash.getWidth() - (bound.p1.x * scale)) / 2.0f;
			}
		}

		graphic = graphic.scale(scale, scale);
		SVGRenderingNode node = new SVGRenderingNode(graphic, "title");
		node.translate.set(trans);

		
		dl .setItems(new RenderingNode[] { node });

		gvSplash.setRenderingContent(dl);
		gvSplash.postInvalidate();		
				
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
}
