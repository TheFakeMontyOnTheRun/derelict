package br.odb.gameutils;

import br.odb.gameutils.math.Vec2;

/**
 * @author monty
 * 
 */
public class Rect {

	public final Vec2 p0 = new Vec2();
	public final Vec2 p1 = new Vec2();

	public Rect() {
	}

	public Rect(float x0, float y0, float dx, float dy) {
		set( x0, y0, dx, dy );
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (p0.hashCode());
		result = prime * result + (p1.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

	public Rect(Rect rect) {
		this.set(rect);
	}

	public float getDX() {
		return p1.x - p0.x;
	}

	public float getDY() {
		return p1.y - p0.y;
	}

	public void set(float x0, float y0, float dx, float dy) {
		p0.set( x0, y0 );
		setD( dx, dy );
	}
	
	public boolean isInside( Vec2 point ) {
		return isInside( point.x, point.y );
	}
	
	public  boolean intersect( Rect another ) {
		
		if ( another == null ) {
			return false;
		}
		
		if ( this == another || this.equals( another ) ) {
			return true;
		}
		
		boolean b;
		
		b = another.isInside( p0.x, p1.y ); 
		
		if ( b ) {
			return true;
		}
		b = another.isInside( p1.x, p0.y ); 
		
		if ( b ) {
			return true;
		}
		b = another.isInside( p1.x, p1.y ); 
		
		if ( b ) {
			return true;
		}
		b = another.isInside( p0.x, p0.y ); 
		
		if ( b ) {
			return true;
		}
		
		b = ( p0.x > another.p0.x && p1.x < another.p1.x && p0.y < another.p0.y && p1.y > another.p1.y );
		
		if ( b ) {
			return true;
		}
		
		b = isInside( another.p0.x, another.p1.y ); 
		
		if ( b ) {
			return true;
		}
		b = isInside( another.p1.x, another.p0.y ); 
		
		if ( b ) {
			return true;
		}
		b = isInside( another.p1.x, another.p1.y ); 
		
		if ( b ) {
			return true;
		}
		b = isInside( another.p0.x, another.p0.y ); 
		
		if ( b ) {
			return true;
		}
		
		b = ( another.p0.x > p0.x && another.p1.x < p1.x && another.p0.y < p0.y && another.p1.y > p1.y );

		return b;

	}

	private boolean isInside(float x, float y) {
		return ( p0.x <= x  ) && ( p0.y <= y ) && ( x <= p1.x ) && ( y <= p1.y );
	}

	public void set(Rect rect) {
		p0.set( rect.p0 );
		p1.set( rect.p1 );
	}

	public void setD(float dx, float dy) {
		p1.set( p0.x + dx, p0.y + dy );
	}
}
