package br.odb.derelict.menus;

import br.odb.derelict.R;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;

public class ShowODBSplashActivity extends Activity implements Runnable {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		overridePendingTransition( R.anim.dissolve_from, R.anim.dissolve_into );
		setContentView(R.layout.activity_show_odbsplash);
		
		Handler handler = new Handler();
		handler.postDelayed( this, 2000 );
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_show_odbsplash, menu);
		return true;
	}



	@Override
	public void run() {
		finish();
		
	}

}
