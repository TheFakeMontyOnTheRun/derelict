package br.odb.derelict2d;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class ShowHowToPlayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_how_to_play);
		
//		Intent intent = new Intent( RecognizerIntent.ACTION_RECOGNIZE_SPEECH );
//		intent.putExtra( RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM );
//		intent.putExtra( RecognizerIntent.EXTRA_PROMPT, "Manja?" );
//		intent.putExtra( RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH );
//		startActivityForResult( intent, 1 );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_how_to_play, menu);
		return true;
	}

	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		
//		if ( resultCode == RESULT_OK && requestCode == 1 ) {
//			TextView result;
//			
//			result = (TextView) findViewById( R.id.result );
//			
//			String answers = "";
//			
//			for ( String candidate : data.getStringArrayListExtra( RecognizerIntent.EXTRA_RESULTS ) ) {
//				answers += candidate + "\n";
//			}
//			result.setText( answers );
//		}
//	}
}
