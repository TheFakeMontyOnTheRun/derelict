package br.odb.derelict2d;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import br.odb.derelict2d.game.DerelictGameSession;
import br.odb.gamelib.android.GameView;
import br.odb.gameutils.math.Vec2;

/**
 * @author monty
 *
 */
public class DerelictGameView extends GameView {

	Vec2 camera;


	/**
	 * @param context
	 */
	public DerelictGameView(Context context) {
		super(context);
		
		init( context );
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public DerelictGameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		init( context );
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public DerelictGameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init( context );
	}


    @Override
	protected void onDraw(Canvas canvas) {	
		super.onDraw(canvas);	
	}
}
