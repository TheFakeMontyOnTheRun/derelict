package br.odb.gamerendering.rendering;

import br.odb.gameutils.Rect;

/**
 * @author monty
 *
 */
public class BorderedSquareRenderingNode extends RenderingNode {

    private final RenderingNode[] border = new RenderingNode[4];
    private final RenderingNode[] joints = new RenderingNode[4];
    private final RenderingNode content;
    private final PatternRenderingNode background;

    public BorderedSquareRenderingNode(RenderingNode[] borders,
                                       RenderingNode[] joints, RenderingNode background,
                                       RenderingNode content, Rect rect, String id) {

        super("BorderedSquareRenderingNode_" + id);

        this.bounds.set( rect );

        Rect[] borderRects = new Rect[4];

        // cima
        borderRects[0] = new Rect();
        borderRects[0].p0.x = joints[0].getWidth();
        borderRects[0].p0.y = 0;
        borderRects[0].p1.x = bounds.p1.x - joints[1].getWidth();
        borderRects[0].p1.y = borders[0].getHeight();

        // direita
        borderRects[1] = new Rect();
        borderRects[1].p0.x = bounds.p1.x - borders[1].getWidth();
        borderRects[1].p0.y = joints[1].getHeight();
        borderRects[1].p1.x = bounds.p1.x;
        borderRects[1].p1.y = bounds.p1.y - joints[2].getHeight();

        // baixo
        borderRects[2] = new Rect();
        borderRects[2].p0.x = joints[3].getWidth();
        borderRects[2].p0.y = bounds.p1.y - borders[2].getHeight();
        borderRects[2].p0.x = bounds.p1.x - joints[2].getWidth();
        borderRects[2].p0.y = bounds.p1.y;

        // esquerda
        borderRects[3] = new Rect();
        borderRects[3].p0.x = 0;
        borderRects[3].p0.y = joints[0].getHeight();
        borderRects[3].p1.x = borders[3].getWidth();
        borderRects[3].p1.y = bounds.p1.y - joints[3].getHeight();

        for (int c = 0; c < 4; ++c) {

            this.border[c] = new PatternRenderingNode(borders[c],
                    borderRects[c]);
            this.joints[c] = joints[c];
        }
        this.background = new PatternRenderingNode(background, new Rect(
                joints[0].getWidth(), joints[0].getHeight(), bounds.p1.x
                - joints[2].getWidth() - joints[0].getWidth(),
                bounds.p1.y - joints[2].getHeight() - joints[0].getHeight()));
        this.content = new PatternRenderingNode(content, new Rect(
                borders[3].getWidth(), borders[0].getHeight(), bounds.p1.x
                - borders[1].getWidth() - borders[3].getWidth(),
                bounds.p1.y - borders[2].getHeight() - borders[0].getHeight()));

        float dx;
        float dy;

        dx = joints[1].bounds.getDX();
        dy = joints[1].bounds.getDY();
        joints[1].bounds.p0.x = bounds.p1.x - joints[1].getWidth();
        joints[1].bounds.setD(dx, dy);

        dx = joints[1].bounds.getDX();
        dy = joints[1].bounds.getDY();
        joints[2].bounds.p0.x = bounds.p1.x - joints[2].getWidth();
        joints[2].bounds.p0.y = bounds.p1.y - joints[2].getHeight();
        joints[2].bounds.setD(dx, dy);

        dx = joints[1].bounds.getDX();
        dy = joints[1].bounds.getDY();
        joints[3].bounds.p0.y = bounds.p1.y - joints[3].getHeight();
        joints[3].bounds.setD(dx, dy);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * br.odb.gamelib.rendering.RenderingNode#render(br.odb.gamelib.rendering
     * .RenderingContext)
     */
    @Override
    public void render(RenderingContext rc) {

        for (int c = 0; c < 4; ++c) {
            joints[c].render(rc);
        }

        background.render(rc);
        content.render(rc);

        for (int c = 0; c < 4; ++c) {
            border[c].render(rc);
        }
    }
}
