package br.odb.gamerendering.rendering.animation;

import br.odb.gamerendering.rendering.RenderingNode;
import br.odb.gameutils.math.Vec2;



/**
 * @author monty
 *
 */
public class TranslateAnimation extends Animation {
	
	private final Vec2 start = new Vec2();
	private final Vec2 target = new Vec2();
	private final Vec2 speed = new Vec2();
	
	public TranslateAnimation(RenderingNode subject, Vec2 start, Vec2 target, long remainingTime, long tickTime ) {
		super(subject, remainingTime );
		
		this.start.set( start );
		this.target.set( target );
		this.speed.set( target.sub( start ).scaled( ( ( float ) tickTime ) / remainingTime ) );
	}
	
	@Override
	public void update(long ms ) {
		super.update( ms );
		subject.translate.addTo( speed );		
	}

	@Override
	public void kill() {
		speed.set( 0.0f, 0.0f );
		
	}
}
