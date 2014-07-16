/**
 * 
 */
package br.odb.derelict.engine.bzk3.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
 
/**
 * @author monty
 * 
 */
public class VirtualPad extends View {
	public static final int KB_DOWN = 2;
	public static final int KB_FIRE = 4;
	public static final int KB_FIRE2 = 5;
	public static final int KB_FIRE3 = 6;
	public static final int KB_FIRE4 = 7;
	public static final int KB_LEFT = 3;
	public static final int KB_RIGHT = 1;
	public static final int KB_UP = 0;

	private boolean[] keyMap;

	private Rect lastTouch1;
	private VirtualPadClient listener;
	private Paint paint;
	private Rect[] vKeys;

	public VirtualPad(Context context, AttributeSet attr) {
		super(context, attr);
		init();
	}

	@Override
	public void draw(Canvas canvas) {
		Log.d(VIEW_LOG_TAG, "------------DRAW!");

		paint.setARGB(255, 255, 255, 255);

		canvas.drawRect(new RectF(0, 0, getWidth(), getHeight()), paint);

		for (int c = 0; c < vKeys.length; c++) {

			if (keyMap[c])
				paint.setARGB(128, 0, 0, 255);
			else
				paint.setARGB(128, 255, 0, 0);

			canvas.drawRect(vKeys[c], paint);
		}

		paint.setARGB(64, 0, 255, 0);

		canvas.drawRect(lastTouch1, paint);

		paint.setARGB(255, 0, 0, 0);
	}

	public void init() {
		paint = new Paint();
		vKeys = new Rect[5];
		vKeys[0] = new Rect();
		vKeys[1] = new Rect();
		vKeys[2] = new Rect();
		vKeys[3] = new Rect();
		vKeys[4] = new Rect();
		lastTouch1 = new Rect();
		keyMap = new boolean[9];
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		this.postInvalidate();

		if (event.getAction() == MotionEvent.ACTION_UP) {

			for (int c = 0; c < vKeys.length; c++) {
				keyMap[c] = false;
			}

			return true;
		}

		lastTouch1.set((int) event.getX() - 25, (int) event.getY() - 25,
				(int) (event.getX() + 25), (int) (event.getY() + 25));


		boolean returnValue = false;

		for (int c = 0; c < vKeys.length; c++) {
			if (Rect.intersects(vKeys[c], lastTouch1)) {
				keyMap[c] = (event.getAction() != MotionEvent.ACTION_UP)
						&& (event.getAction() != MotionEvent.ACTION_OUTSIDE);
				returnValue = true;
			} else
				keyMap[c] = false;
		}

		if (listener != null)
			listener.handleKeys(keyMap);

		return returnValue;
	}

	public void setBounds(int left, int top, int right, int bottom) {

		int width = right - left;
		int height = bottom - left;
		vKeys[0].set((int) ((width * 15L) / 100L),
				(int) ((height * 30L) / 100L), (int) ((width * 20L) / 100L),
				(int) ((height * 40L) / 100L));
		vKeys[1].set((int) ((width * 25L) / 100L),
				(int) ((height * 40L) / 100L), (int) ((width * 30L) / 100L),
				(int) ((height * 50L) / 100L));
		vKeys[2].set((int) ((width * 15L) / 100L),
				(int) ((height * 50L) / 100L), (int) ((width * 20L) / 100L),
				(int) ((height * 60L) / 100L));
		vKeys[3].set((int) ((width * 5L) / 100L),
				(int) ((height * 40L) / 100L), (int) ((width * 10L) / 100L),
				(int) ((height * 50L) / 100L));
		vKeys[4].set((int) ((width * 80L) / 100L),
				(int) ((height * 30L) / 100L), (int) ((width * 90) / 100L),
				(int) ((height * 45L) / 100L));
	}

	public void setBounds(Rect bounds) {
		setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom);
	}

	public void setListener(VirtualPadClient listener) {
		this.listener = listener;
	}

}
