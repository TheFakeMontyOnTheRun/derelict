package br.odb.gamerendering.rendering;

import java.util.HashMap;

import br.odb.gameutils.Color;
import br.odb.gameutils.Rect;
import br.odb.gameutils.math.Vec2;
import br.odb.libsvg.ColoredPolygon;
import br.odb.libsvg.SVGParsingUtils.Gradient;

public abstract class RenderingContext {

    final public Vec2 currentOrigin = new Vec2(0.0f, 0.0f);
    protected float currentAlpha = 1.0f;

    public abstract void fillRect(Color color, Rect rect);

    public abstract void drawColoredPolygon(ColoredPolygon c, Rect bounds,
                                            String style, HashMap<String, Gradient> gradients);

    public float getCurrentAlpha() {

        return currentAlpha;
    }

    public void setCurrentAlpha(float f) {
        currentAlpha = f;
    }

    public abstract void drawText(Vec2 p0, String content, Color color, int fontSize);

}
