/**
 * 
 */
package br.odb.derelict.engine.bzk3.android;

import br.odb.utils.Utils;
import br.odb.utils.math.Vec2;
import br.odb.utils.math.Vec3;
import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @author monty
 *
 */
public class SensorOrientedSVGViewer extends SVGView implements
		SensorEventListener {

	
	Vec3 accel = new Vec3();
	private int scaleX;
	private int scaleY;
	
	/**
	 * @param context
	 * @param attributeSet
	 */
	public SensorOrientedSVGViewer(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param j 
	 * @param  
	 */
	public SensorOrientedSVGViewer(Context context, int scaleX, int scaleY ) {
		super(context);
		
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	/* (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onAccuracyChanged(android.hardware.Sensor, int)
	 */
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void draw(Canvas canvas) {
	
		Vec2 origin = getOrigin();
		origin.x = ( - scaleX / 2 ) + Utils.clamp( accel.x * scaleX, -scaleX / 2, scaleX / 2 );
		origin.y = ( - scaleY / 2 ) + Utils.clamp( accel.y * scaleY, -scaleY / 2, scaleY / 2 );
		setOrigin( origin );
		super.draw(canvas);
//		Log.d( "derelict", "sensor: " + origin.x + ", " + origin.y );
	}



	/* (non-Javadoc)
	 * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		accel.x = ( event.values[ 0 ] );
		accel.y = ( event.values[ 1 ] );
		accel.z = ( event.values[ 2 ] );
		this.postInvalidate();
	}
}
