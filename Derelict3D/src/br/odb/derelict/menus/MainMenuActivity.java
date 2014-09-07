package br.odb.derelict.menus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import br.odb.derelict.R;

public class MainMenuActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition( R.anim.dissolve_from, R.anim.dissolve_into );
		setContentView(R.layout.activity_main_menu);
		
		findViewById( R.id.btnQuit ).setOnClickListener( this );
		findViewById( R.id.btnSettings ).setOnClickListener( this );
		findViewById( R.id.btnPlayGame ).setOnClickListener( this );
		findViewById( R.id.btnPrologue ).setOnClickListener( this );
		
		MediaPlayer music = MediaPlayer.create( this, R.raw.derelicttheme );
		music.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main_menu, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		
		switch ( v.getId() ) {
		
			case R.id.btnPlayGame:
				onClickPlayGame();
				break;
				
			case R.id.btnQuit:
				onClickQuit();
				break;
			
			case R.id.btnPrologue:
				onPrologueClicked();
				break;
				
			case R.id.btnSettings:
				onClickSettings();
				break;
		}
	}
	
	private void onClickPlayGame() {

		Intent intent;
		intent = new Intent( this, PlayGameActivity.class );
		this.startActivity( intent );
	}

	public void onClickSettings() {

//		Intent intent;
//		intent = new Intent( this, SettingsActivity.class );
//		this.startActivity( intent );
	}

	public void onPrologueClicked() {

//		Intent intent;
//		intent = new Intent( this, StoryTellingActivity.class );
//		this.startActivity( intent );
	}
	
	public void onClickQuit() {
		
		finish();
	}
}
