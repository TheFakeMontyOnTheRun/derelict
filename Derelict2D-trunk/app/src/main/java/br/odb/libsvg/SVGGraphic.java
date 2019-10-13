package br.odb.libsvg;

import java.util.ArrayList;
import java.util.HashMap;

import br.odb.gameutils.Rect;
import br.odb.gameutils.math.Vec2;
import br.odb.libsvg.SVGParsingUtils.Gradient;

public class SVGGraphic {

    public final HashMap<String, Gradient> gradients = new HashMap<>();
    public ColoredPolygon[] shapes;

    public SVGGraphic(ColoredPolygon[] shapes) {
        this.shapes = shapes;
    }

    SVGGraphic() {
    }

    public ColoredPolygon[] getShapesStartingWith(String prefix) {
        ArrayList<ColoredPolygon> tmp = new ArrayList<>();

        for (ColoredPolygon cp : shapes) {
            if (cp.id.startsWith(prefix)) {
                tmp.add(cp);
            }
        }
        return tmp.toArray(new ColoredPolygon[1]);
    }

    public ColoredPolygon getShapeById(String name) {

        for (ColoredPolygon s : shapes) {
            if (s.id.equals(name)) {
                return s;
            }
        }

        return null;
    }

    public SVGGraphic scaleTo(float width, float height) {

        Rect bound = makeBounds();

        float newWidth = bound.p1.x;
        float newHeight = bound.p1.y;

        float scaleX = width / newWidth;
        float scaleY = height / newHeight;

        return scale(scaleX, scaleY);
    }

    public SVGGraphic scale(float width, float height) {

        SVGGraphic toReturn = new SVGGraphic();
        ArrayList<ColoredPolygon> cps = new ArrayList<>();

        for (ColoredPolygon cp : this.shapes) {

            cps.add(cp.scale(width, height));
        }

        toReturn.shapes = cps.toArray(new ColoredPolygon[1]);

        for (String key : gradients.keySet()) {
            toReturn.gradients.put(key, gradients.get(key));
        }

        return toReturn;
    }

    public Rect makeBounds() {
        return makeBounds(new Vec2(0, 0));
    }

    private Rect makeBounds(Vec2 translate) {
        float x, y;
        Rect rect = new Rect();

        rect.p0.x = Integer.MAX_VALUE;
        rect.p0.y = Integer.MAX_VALUE;

        for (ColoredPolygon cp : shapes) {
            if (cp.xpoints != null && cp.ypoints != null) {

                for (float aX : cp.xpoints) {

                    x = aX + translate.x;

                    if (x < rect.p0.x) {
                        rect.p0.x = x;
                    }

                    if (x > rect.p1.x) {
                        rect.p1.x = x;
                    }
                }

                for (float aY : cp.ypoints) {

                    y = aY + translate.y;

                    if (y < rect.p0.y) {
                        rect.p0.y = y;
                    }

                    if (y > rect.p1.y) {
                        rect.p1.y = y;
                    }
                }
            } else {
                System.err.println("Path has no elements: " + cp.id);
            }
        }
        return rect;
    }
}
