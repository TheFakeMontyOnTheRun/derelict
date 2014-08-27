package br.odb.derelict2d;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import br.odb.gamelib.android.AndroidUtils;
import br.odb.gamelib.android.GameView;
import br.odb.gamerendering.rendering.AssetManager;
import br.odb.gamerendering.rendering.DisplayList;
import br.odb.gamerendering.rendering.RenderingNode;
import br.odb.gamerendering.rendering.SVGRenderingNode;
import br.odb.libsvg.ColoredPolygon;
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
		
		AndroidUtils.initImage((GameView) findViewById(R.id.gvHowToPlayHero), "hero-centered", ((Derelict2DApplication) getApplication()).getAssetManager(), 200, 200, "");
		AndroidUtils.initImage((GameView) findViewById(R.id.gvHowToPlayItem), "plasma-gun", ((Derelict2DApplication) getApplication()).getAssetManager());
		
		
		AndroidUtils.initImage((GameView) findViewById(R.id.gvHowToPlayPick), "icon-pick", ((Derelict2DApplication) getApplication()).getAssetManager());
		AndroidUtils.initImage((GameView) findViewById(R.id.gvHowToPlayToggle), "icon-toggle", ((Derelict2DApplication) getApplication()).getAssetManager());
		AndroidUtils.initImage((GameView) findViewById(R.id.gvHowToPlayUseWith), "icon-use-with", ((Derelict2DApplication) getApplication()).getAssetManager());
		AndroidUtils.initImage((GameView) findViewById(R.id.gvHowToPlayUse), "icon-use", ((Derelict2DApplication) getApplication()).getAssetManager());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_how_to_play, menu);
		return true;
	}
}
