package br.odb.gamerendering.rendering;

import java.io.InputStream;

import br.odb.libsvg.ColoredPolygon;
import br.odb.libsvg.SVGGraphic;
import br.odb.libsvg.SVGParsingUtils;
import br.odb.gameutils.math.Vec2;

public class SVGRenderingNode extends RenderingNode {

    public SVGRenderingNode(SVGGraphic graphic, String id) {
        super("SVGRenderingNode_" + id);
        this.graphic = graphic;
    }

    public SVGRenderingNode(InputStream is, String id) {
        this(SVGParsingUtils.readSVG(is), id);
    }

    @Override
    public void render(RenderingContext renderingContext) {

        String style = null;

        for (ColoredPolygon c : graphic.shapes) {

            if (c.visible) {
                renderingContext.drawColoredPolygon(c, bounds, style,
                        graphic.gradients);
            }
        }
    }
    private final Vec2 origin = new Vec2();

    public void setOrigin(Vec2 origin) {
        this.origin.set(origin);
    }

    public Vec2 getOrigin() {

        return origin;
    }
    public final SVGGraphic graphic;
}
