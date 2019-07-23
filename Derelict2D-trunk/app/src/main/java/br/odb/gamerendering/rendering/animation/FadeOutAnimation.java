package br.odb.gamerendering.rendering.animation;

import br.odb.gamerendering.rendering.RenderingNode;

public class FadeOutAnimation extends Animation {

	private final RenderingNode subject;
	private float speed;
	long showTime;

	public FadeOutAnimation(RenderingNode subject, long displayTime,
			int remainingTime) {
		super(subject, remainingTime);

		this.subject = subject;
		this.speed = 1.0f / (remainingTime / 100.0f);

		subject.alpha = 1.0f;

	}

	@Override
	public void kill() {
		speed = 0;
		subject.setVisible(false);
	}

	@Override
	public void update(long ms) {
		super.update(ms);
		subject.alpha -= (speed);
	}

}
