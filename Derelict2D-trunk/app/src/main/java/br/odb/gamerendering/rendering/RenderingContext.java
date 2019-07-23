package br.odb.gamerendering.rendering;

import java.util.HashMap;

import br.odb.libsvg.ColoredPolygon;
import br.odb.libsvg.SVGParsingUtils.Gradient;
import br.odb.gameutils.Color;
import br.odb.gameutils.Rect;
import br.odb.gameutils.math.Vec2;

/**
 * @author monty
 * 
 */
public abstract class RenderingContext {

	private boolean readyForRendering;
	final public Vec2 currentOrigin = new Vec2( 0.0f, 0.0f );
	protected float currentAlpha = 1.0f;

	public abstract void fillRect(Color color, Rect rect);

	public abstract void endRendering();

	public abstract void drawColoredPolygon(ColoredPolygon c, Rect bounds,
			String style, HashMap<String, Gradient> gradients);

	protected void setReadyForRendering(boolean status) {
		this.readyForRendering = status;
	}

	public abstract void saveClipRect();

	public abstract void restoreClipRect();

	public abstract void drawBitmap(RasterImage tileImage, Vec2 p0);

	public abstract void drawBitmap(RasterImage tileImage, Rect bounds);

	public abstract void drawLine(Vec2 p0, Vec2 p1);
	
	public abstract void drawBitmap(RasterImage currentImage, Vec2 translate, Vec2 scale, int angleRotation );
	
	public abstract void setClipRect(Rect bounds);

	public float getCurrentAlpha() {

		return currentAlpha;
	}

	public void setCurrentAlpha(float f) {
		currentAlpha = f;
	}

	public abstract void drawText(Vec2 p0, String content, Color color, int fontSize );

}
