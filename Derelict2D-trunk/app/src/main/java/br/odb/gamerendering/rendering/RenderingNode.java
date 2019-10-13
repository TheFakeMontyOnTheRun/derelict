package br.odb.gamerendering.rendering;

import br.odb.gameutils.Rect;
import br.odb.gameutils.Updatable;
import br.odb.gameutils.math.Vec2;

public abstract class RenderingNode implements Updatable {

	final public Vec2 translate = new Vec2(0.0f, 0.0f);
	public final float alpha = 1.0f;
	final Rect bounds = new Rect();
	private final String id;

	RenderingNode(String id) {
		this.id = id;
	}

	float getWidth() {
		return bounds.getDX();
	}

	float getHeight() {
		return bounds.getDY();
	}

	public boolean isVisible() {
		return true;
	}

	@Override
	public void update(long ms) {
	}

	public abstract void render(RenderingContext rc);

	String getId() {
		return id;
	}
}
