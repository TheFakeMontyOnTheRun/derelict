package br.odb.derelict2d;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ShowCreditsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_credits);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_credits, menu);
		return true;
	}

}
