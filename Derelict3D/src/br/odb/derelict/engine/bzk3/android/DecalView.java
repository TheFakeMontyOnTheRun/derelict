/**
 * 
 */
package br.odb.derelict.engine.bzk3.android;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import br.odb.derelict.engine.bzk3.android.geometry.GLES1Triangle;
import br.odb.littlehelper3d.Decal;

/**
 * @author monty
 * 
 */
public class DecalView extends View {

	private GLES1Triangle[] shapes;

	public DecalView(Context context) {
		super(context);
		init();
	}

	public DecalView(Context context, AttributeSet attributeSet)
	{
	    super(context, attributeSet);

	    init();
	}
	
	public void init() {		

		try {
			InputStream is = getContext().getAssets().open( "astrohud_still.bin" );
			shapes = Decal.loadGraphic(is, getWidth(), getHeight() );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if ( shapes == null ) 
			return;
		
		setBackgroundColor(Color.WHITE);
		
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(2);
		Path path = new Path();
		

		for ( GLES1Triangle t : shapes ) {
		

			path = new Path();
			path.moveTo(t.x0, t.y0);
			path.lineTo(t.x1, t.y1);
			path.lineTo(t.x2, t.y2);
			path.close();

			paint.setColor(Color.rgb((int) t.r, (int) t.g, (int) t.b));
			paint.setAlpha((int) t.a);

			canvas.drawPath(path, paint);
		}
	}

}
