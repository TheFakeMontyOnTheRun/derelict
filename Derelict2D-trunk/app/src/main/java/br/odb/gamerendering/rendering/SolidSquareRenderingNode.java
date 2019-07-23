package br.odb.gamerendering.rendering;

import br.odb.gameutils.Color;
import br.odb.gameutils.Rect;

/**
 * @author monty
 *
 */
public class SolidSquareRenderingNode extends RenderingNode {

    private final Color color;

    /**
     * @param rect
     *
     */
    public SolidSquareRenderingNode( Rect rect,
            Color color) {

        super("square_" + rect.p0.x + "_" + rect.p0.y + "_" + rect.p1.x + "_" + rect.p1.y + "_" + color.getHTMLColor());
        this.bounds.set( rect );
        this.color = color;
    }

    @Override
    public void render(RenderingContext context) {
        context.fillRect(color, bounds);
    }
}
