package br.odb.derelict.menus;

import br.odb.derelict.R;
import br.odb.derelict.R.layout;
import br.odb.derelict.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TerminalAccessActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terminal_access);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.terminal_access, menu);
		return true;
	}

}
