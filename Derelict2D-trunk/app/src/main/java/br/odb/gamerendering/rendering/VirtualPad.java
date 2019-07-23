package br.odb.gamerendering.rendering;

import br.odb.gameutils.Rect;
import br.odb.gameutils.math.Vec2;

/**
 * @author monty
 * 
 */
class VirtualPad {

	public static final int KB_UP = 0;
	public static final int KB_RIGHT = 1;
	public static final int KB_DOWN = 2;
	public static final int KB_LEFT = 3;
	public static final int KB_FIRE = 4;

	private final boolean[] keyMap;

	private final Rect[] vKeys;
	private final Rect lastTouch1;

	public VirtualPad() {
		vKeys = new Rect[5];
		vKeys[0] = new Rect();
		vKeys[1] = new Rect();
		vKeys[2] = new Rect();
		vKeys[3] = new Rect();
		vKeys[4] = new Rect();
		lastTouch1 = new Rect();
		keyMap = new boolean[5];
	}

	public void setBounds(Rect bounds) {
		setBounds(bounds.p0.x, bounds.p0.y, bounds.p1.x, bounds.p1.y);
	}

	private void setBounds(float x0, float y0, float x1, float y1) {
		// super.setBounds(left, top, right, bottom);
		int width = (int) (x1 - x0);
		int height = (int) (y1 - x0);
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

	// @Override
	// public void draw(Canvas canvas) {
	//
	// for (int c = 0; c < vKeys.length; c++) {
	//
	// if (keyMap[c])
	// paint.setARGB(128, 0, 0, 255);
	// else
	// paint.setARGB(128, 255, 0, 0);
	//
	// canvas.drawRect(vKeys[c], paint);
	// }
	//
	// paint.setARGB(64, 0, 255, 0);
	//
	// canvas.drawRect(lastTouch1, paint);
	//
	// paint.setARGB(255, 0, 0, 0);
	// }

	public boolean onReleaseAt(Vec2 pointer) {
		for (int c = 0; c < vKeys.length; c++) {
			keyMap[c] = false;
		}

		return true;
	}

	public boolean onTouchAt(Vec2 pointer) {

		lastTouch1.set(pointer.x - 25, pointer.y - 25, (int) (pointer.x + 25),
				(int) (pointer.y + 25));

		//
		//
		// for (int c = 0; c < vKeys.length; c++) {
		// if (Rect.intersects(vKeys[c], lastTouch1)){
		// keyMap[c] = true;//(event.getAction() != MotionEvent.ACTION_UP) &&
		// (event.getAction() != MotionEvent.ACTION_OUTSIDE);
		// returnValue = true;
		// } else
		// keyMap[c] = false;
		// }
		//
		// if ( listener != null && returnValue )
		// listener.handleKeys( keyMap );
		//
		return false;
	}

	public boolean[] getKeyMap() {

		return keyMap;
	}

}
