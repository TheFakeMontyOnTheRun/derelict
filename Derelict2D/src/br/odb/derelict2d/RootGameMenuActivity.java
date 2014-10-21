package br.odb.derelict2d;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import br.odb.gamelib.android.GameView;
import br.odb.gamerendering.rendering.AssetManager;
import br.odb.gamerendering.rendering.DisplayList;
import br.odb.gamerendering.rendering.RenderingNode;
import br.odb.gamerendering.rendering.SVGRenderingNode;
import br.odb.libsvg.SVGGraphic;
import br.odb.utils.Rect;
import br.odb.utils.math.Vec2;

public class RootGameMenuActivity extends Activity implements OnClickListener {

	enum DificultyLevel {
		EASY( "Easy: still figuring out what this is all about", "You will have SOME help", new String[] {
				"pick magboots",
				"toggle magboots",
				"pick plasma-gun"
		} ),
		NORMAL( "Normal: players that read", "Are you ready, pal!?", new String[] {
				"pick magboots"
		}),
		HARD( "Hard: the real deal", "GET LAMP", new String[]{} );
		
		public final String name;
		public final String description;
		public final String[] aid;
		
		DificultyLevel( String name, String description, String[] aid ) {
			this.name = name;
			this.description = description;
			this.aid = aid;
		}
		
		@Override
		public String toString() {
			return name + " (" + description + ")";
		}
	}
	
	private Bundle bundle;
	private android.widget.CheckBox chkSound;
	private android.widget.CheckBox chkSpeech;
	Spinner spnLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_root_game_menu);

		findViewById(R.id.btnExploreStation).setOnClickListener(this);
//		findViewById(R.id.btnExploreStation3D).setOnClickListener(this);
		findViewById(R.id.btnAbout).setOnClickListener(this);
		findViewById(R.id.btnHowToPlay).setOnClickListener(this);
		
		
		spnLevel = (Spinner) findViewById( R.id.spnLevel );
		chkSound = (android.widget.CheckBox) findViewById(R.id.chkSound);
		chkSpeech = (android.widget.CheckBox) findViewById(R.id.chkSpeech);

		chkSound.setChecked(((Derelict2DApplication) getApplication())
				.mayEnableSound());
		gvSplash = (GameView) findViewById(R.id.gvbg);
		
		spnLevel.setAdapter( new ArrayAdapter<DificultyLevel>( this, android.R.layout.simple_spinner_item, DificultyLevel.values() ));
		spnLevel.setSelection( 1 );
	}

	GameView gvSplash;

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		initImage();
	}

	void initImage() {

		DisplayList dl = new DisplayList("dl");
		AssetManager resManager = ((Derelict2DApplication) getApplication())
				.getAssetManager();

		SVGGraphic graphic = resManager.getGraphics("logo");
		
		float scale = 1;
		Vec2 trans = new Vec2();

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

		dl.setItems(new RenderingNode[] { node });

		gvSplash.setRenderingContent(dl);
		gvSplash.postInvalidate();
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		bundle = new Bundle();
		bundle.putString("hasSound", chkSound.isChecked() ? "y" : "n");
		bundle.putString("speech", chkSpeech.isChecked() ? "y" : "n");
		bundle.putInt("aid", ( ( DificultyLevel )spnLevel.getSelectedItem() ).ordinal() );
		
		switch (v.getId()) {
		
//		case R.id.btnExploreStation3D:
//			((Derelict2DApplication) this.getApplication()).startNewGame();
//			intent = new Intent(getBaseContext(),
//					ExploreStationActivity.class);
//			intent.putExtras(bundle);
//			startActivityForResult(intent, 1);
//			
//			break;
		case R.id.btnExploreStation:
			((Derelict2DApplication) this.getApplication()).startNewGame();
			intent = new Intent(getBaseContext(),
					ExploreStationActivity.class);
			intent.putExtras(bundle);
			startActivityForResult(intent, 1);
			break;
			
		
			
		case R.id.btnHowToPlay:
			intent = new Intent( this, ShowHowToPlayActivity.class );
			startActivity( intent );
			break;
		case R.id.btnAbout:
			intent = new Intent( this, ShowCreditsActivity.class );
			startActivity( intent );
			break;			
		}		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_CANCELED) {
//			Toast.makeText(this, "Game Over!", Toast.LENGTH_SHORT).show();
		}
	}
}
