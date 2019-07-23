package br.odb.gamerendering.rendering.animation;

import br.odb.gamerendering.rendering.RenderingNode;
import br.odb.gameutils.Updatable;

public abstract class Animation implements Updatable {

	final RenderingNode subject;
	private long remainingTime;
	
	Animation(RenderingNode subject, long remainingTime) {
		this.subject = subject;
		this.remainingTime = remainingTime;
	}
	
	public abstract void kill();
	
	public boolean isActive() {
		return remainingTime > 0;
	}
	
	@Override
	public void update(long ms) {
		remainingTime -= ms;
		if ( remainingTime <= 0 ) {
			kill();
		}
	}
}
