package br.odb.derelict.menus;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import br.odb.derelict.R;
import br.odb.utils.FileServerDelegate;

public class LoadGameActivity extends Activity implements Runnable,
		FileServerDelegate {

	String toReturn = "";

	private class GameWorldLoadRunnable implements Runnable {

		@Override
		public void run() {

			InputStream fis = null;
			DataInputStream dis = null;
			String buffer;
			StringBuilder sb = new StringBuilder();

			try {
				fis = getAssets().open("portas.opt");
				dis = new DataInputStream(fis);

				buffer = dis.readUTF();

				while (buffer != null) {

					
					sb.append(buffer);
					sb.append( "\n" );
					buffer = dis.readUTF();
				}


				dis.close();
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
			}

			toReturn = sb.toString();
			
			
			try
            {
                  File gpxfile = new File( Environment.getExternalStorageDirectory(), "/response.txt");
                  FileWriter writer = new FileWriter(gpxfile);
                  writer.append( toReturn );
                  writer.flush();
                  writer.close();
              }
              catch(IOException eio)
              {
                         System.out.println("Error:::::::::::::"+ eio.getMessage());

              }			
			
			
			
			LoadGameActivity.this.runOnUiThread(LoadGameActivity.this);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_load_game);
		// Handler handler = new Handler();
		// handler.postDelayed( this, 2000 );
		Thread gameMapLoader = new Thread(new GameWorldLoadRunnable());
		gameMapLoader.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_load_game, menu);
		return true;
	}

	@Override
	public void run() {

		Intent intent = new Intent();
//		intent.putExtra(DerelictGameSession.MAP_DATA_KEY, toReturn);
		setResult(RESULT_OK, intent);
		finish();
	}



	@Override
	public InputStream openAsset(String filename) throws IOException {

		
			return getAssets().open(filename);
		
	}

	@Override
	public InputStream openAsset(int resId) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream openAsInputStream(String filename)
			throws IOException {
		

			return getAssets().open(filename);
	}

	@Override
	public OutputStream openAsOutputStream(String filename)
			throws IOException {
		return null;
	}

	@Override
	public void log(String tag, String string) {
		Log.d( tag, string );		
	}
}
