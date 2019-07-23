package br.odb.gameutils;

import br.odb.gameutils.math.Vec2;

/**
 * @author monty
 */
public class Rect {

    public final Vec2 p0 = new Vec2();
    public final Vec2 p1 = new Vec2();

    public Rect() {
    }

    public Rect(float x0, float y0, float dx, float dy) {
        set(x0, y0, dx, dy);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (p0.hashCode());
        result = prime * result + (p1.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Rect)) {
            return false;
        }
        Rect other = (Rect) obj;
        if (!p0.equals(other.p0)) {
            return false;
        }
        return p1.equals(other.p1);
    }

    public float getDX() {
        return p1.x - p0.x;
    }

    public float getDY() {
        return p1.y - p0.y;
    }

    private void set(float x0, float y0, float dx, float dy) {
        p0.set(x0, y0);
        setD(dx, dy);
    }

    public void set(Rect rect) {
        p0.set(rect.p0);
        p1.set(rect.p1);
    }

    private void setD(float dx, float dy) {
        p1.set(p0.x + dx, p0.y + dy);
    }
}
