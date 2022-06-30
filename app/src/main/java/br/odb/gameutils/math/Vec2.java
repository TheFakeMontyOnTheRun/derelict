package br.odb.gameutils.math;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Vec2 implements Serializable {

	public float x;
	public float y;

	public Vec2() {
		x = 0;
		y = 0;
	}

	public Vec2(float d, float d0) {
		x = d;
		y = d0;
	}

	public Vec2(Vec2 other) {
		x = other.x;
		y = other.y;
	}

	public Vec2 add(Vec2 v) {

		return new Vec2(x + v.x, y + v.y);
	}

	@NotNull
	@Override
	public String toString() {

		return "Vec2( " + x + ", " + y + " )";
	}

	public Vec2 sub(Vec2 initialPosition) {
		return new Vec2(x - initialPosition.x, y - initialPosition.y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vec2 other = (Vec2) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		return Float.floatToIntBits(y) == Float.floatToIntBits(other.y);
	}

	public void invalidate() {
		x = Float.NaN;
		y = Float.NaN;
	}

	public boolean isValid() {
		return !Float.isNaN(x) && !Float.isNaN(y) && !Float.isInfinite(x)
				&& !Float.isInfinite(y);
	}

	public void set(Vec2 vec) {

		if (vec == null || !vec.isValid()) {
			return;
		}

		this.x = vec.x;
		this.y = vec.y;
	}

	public void set(float x, float y) {

		this.x = x;
		this.y = y;
	}

	private void scale(float d) {

		x = x * d;
		y = y * d;
	}

	private Vec2 scaled(float f) {
		Vec2 toReturn = new Vec2(x, y);
		toReturn.scale(f);
		return toReturn;
	}

	public void addTo(Vec2 v) {

		if (v == null || !v.isValid()) {
			return;
		}

		this.x += v.x;
		this.y += v.y;
	}

	public Vec2 negated() {

		return scaled(-1.0f);
	}

	public Vec2 scaled(float sx, float sy) {
		Vec2 toReturn = new Vec2(this);
		toReturn.x *= sx;
		toReturn.y *= sy;
		return toReturn;
	}
}