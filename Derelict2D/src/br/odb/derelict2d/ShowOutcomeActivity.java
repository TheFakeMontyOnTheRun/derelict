package br.odb.derelict2d;

import br.odb.gamelib.android.AndroidUtils;
import br.odb.gamelib.android.GameView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class ShowOutcomeActivity extends Activity {

	GameView gvOutcome;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_outcome);
		gvOutcome = (GameView) findViewById( R.id.gvOutcome );
		
		int ending = Integer.parseInt( getIntent().getExtras().getString( "ending" ) );
		
		AndroidUtils.initImage( gvOutcome, "ending" + ending, ((Derelict2DApplication) getApplication()).getAssetManager() );
		( ( TextView ) findViewById( R.id.txtOutcome ) ).setText( getIntent().getExtras().getString( "outcome" ) );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_outcome, menu);
		return true;
	}

}
