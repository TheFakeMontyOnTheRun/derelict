package br.odb.derelict2d;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;
import br.odb.gamelib.android.AndroidUtils;
import br.odb.gamelib.android.GameView;

public class ShowOutcomeActivity extends Activity {

	GameView gvOutcome;
	MediaPlayer theme = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_outcome);
		gvOutcome = (GameView) findViewById( R.id.gvOutcome );
		
		int ending = Integer.parseInt( getIntent().getExtras().getString( "ending" ) );
		
		AndroidUtils.initImage( gvOutcome, "ending" + ending, ((Derelict2DApplication) getApplication()).getAssetManager() );
		( ( TextView ) findViewById( R.id.txtOutcome ) ).setText( getIntent().getExtras().getString( "outcome" ) );
		
		if (theme == null
				&& ((Derelict2DApplication) getApplication()).mayEnableSound()) {
			theme = MediaPlayer.create(this, R.raw.derelicttheme);
			theme.setLooping(true);
			theme.start();
		}		
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
