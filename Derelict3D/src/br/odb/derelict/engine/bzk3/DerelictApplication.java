/**
 * 
 */
package br.odb.derelict.engine.bzk3;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Application;
import android.util.Log;
import br.odb.utils.FileServerDelegate;

/**
 * @author monty
 *
 */
public class DerelictApplication extends Application implements
		FileServerDelegate {
	
	
	

	@Override
	public InputStream openAsInputStream(String filename)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream openAsset(String filename) throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream openAsset(int resId) throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputStream openAsOutputStream(String filename)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void log(String tag, String string) {
		Log.d( tag, string );		
	}
}
