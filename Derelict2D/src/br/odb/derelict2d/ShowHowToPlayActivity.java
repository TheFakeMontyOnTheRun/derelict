package br.odb.derelict2d;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import br.odb.gamelib.android.GameView;
import br.odb.gamerendering.rendering.AssetManager;
import br.odb.gamerendering.rendering.DisplayList;
import br.odb.gamerendering.rendering.RenderingNode;
import br.odb.gamerendering.rendering.SVGRenderingNode;
import br.odb.libsvg.SVGGraphic;
import br.odb.utils.Rect;
import br.odb.utils.math.Vec2;

public class ShowHowToPlayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_how_to_play);
		
		
	}
	
	

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		initImage((GameView) findViewById(R.id.gvHowToPlayHero), "heroGraphic");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_how_to_play, menu);
		return true;
	}

	void initImage(GameView gv, String name) {

		DisplayList dl = new DisplayList("dl");
		AssetManager resManager = ((Derelict2DApplication) getApplication())
				.getAssetManager();

		SVGGraphic graphic = resManager.getGraphics(name);

		float scale = 1;
		Vec2 trans = new Vec2();

		Rect bound = graphic.makeBounds();

		if (gv.getWidth() > 0 && gv.getHeight() > 0) {

			// não me interessa a parte acima da "página".
			float newWidth = bound.p1.x;
			float newHeight = bound.p1.y;

			if (newWidth > newHeight) {
				scale = gv.getWidth() / newWidth;
			} else {
				scale = gv.getHeight() / newHeight;
			}
		}

		trans.y = (gv.getHeight() - (bound.p1.y)) / 2.0f;
		trans.x = (gv.getWidth() - (bound.p1.x)) / 2.0f;

		
		graphic = graphic.scale(scale, scale);
		
		SVGRenderingNode node = new SVGRenderingNode(graphic, "title");
		
		node.translate.set(trans);

		
		dl.setItems(new RenderingNode[] { node });
		
		gv.setRenderingContent(dl);
		gv.postInvalidate();
	}


}
