package br.odb.gamerendering.rendering;

import br.odb.libsvg.ColoredPolygon;
import br.odb.libsvg.SVGGraphic;

public class SVGRenderingNode extends RenderingNode {

	public final SVGGraphic graphic;

	public SVGRenderingNode(SVGGraphic graphic, String id) {
		super("SVGRenderingNode_" + id);
		this.graphic = graphic;
	}

	@Override
	public void render(RenderingContext renderingContext) {

		for (ColoredPolygon c : graphic.shapes) {

			if (c.visible) {
				renderingContext.drawColoredPolygon(c, bounds, null,
						graphic.gradients);
			}
		}
	}
}
