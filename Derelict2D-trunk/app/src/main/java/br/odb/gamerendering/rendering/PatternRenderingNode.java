package br.odb.gamerendering.rendering;

import br.odb.gameutils.Rect;

public class PatternRenderingNode extends RenderingNode {

	private final RenderingNode repeated;

	public PatternRenderingNode(RenderingNode node, Rect rect) {
		super( "PatternRenderingNode_" + node.getId() );

		this.repeated = node;
		this.bounds.set( rect );
	}

	@Override
	public void render(RenderingContext rc) {

		Rect dst = new Rect();
		int width = (int) repeated.bounds.getDX();
		int height = (int) repeated.bounds.getDY();

		rc.saveClipRect();
		rc.setClipRect(bounds);

		for (int x = (int) bounds.p0.x; x <= bounds.p1.x; x += width) {
			for (int y = (int) bounds.p0.y; y <= bounds.p1.y; y += height) {

				dst.set(x, y, width, height);
				repeated.bounds.set(dst);
				repeated.render(rc);
			}
		}

		rc.restoreClipRect();
	}
}
