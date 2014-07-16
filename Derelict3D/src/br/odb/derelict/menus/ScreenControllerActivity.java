package br.odb.derelict.menus;

import br.odb.derelict.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class ScreenControllerActivity extends Activity {
	Intent intent;
	static boolean splashShown = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		if ( !splashShown ){
			
			setContentView(R.layout.activity_screen_controller);
			intent = new Intent( this, ShowODBSplashActivity.class );
			this.startActivityForResult( intent, 1 );
			splashShown = true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_screen_controller, menu);
		return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);

		switch ( requestCode ) {
			case 2:
				finish();
				break;
			case 1:
				intent = new Intent( this, MainMenuActivity.class );
				this.startActivityForResult( intent, 2 );
				break;
		}
	}
}
