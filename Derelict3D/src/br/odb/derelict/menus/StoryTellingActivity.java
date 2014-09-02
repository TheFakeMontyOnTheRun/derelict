package br.odb.derelict.menus;


import br.odb.derelict.R;
import br.odb.derelict.engine.bzk3.android.SVGView;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class StoryTellingActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition( R.anim.dissolve_from, R.anim.dissolve_into );
		setContentView(R.layout.activity_story_telling);
		
		findViewById( R.id.btnNext ).setOnClickListener( this );
		findViewById( R.id.btnPrevious ).setOnClickListener( this );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_story_telling, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		
		switch ( v.getId() ){
			case R.id.btnPrevious:
				break;
				
			case R.id.btnNext:
				break;
		}
		
		SVGView stvStory = (SVGView) findViewById( R.id.stvStory );
		stvStory.init( "intro.svg" );
	}

}
