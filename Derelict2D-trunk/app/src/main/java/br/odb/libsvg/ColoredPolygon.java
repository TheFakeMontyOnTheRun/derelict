package br.odb.libsvg;

import java.util.ArrayList;

import br.odb.gameutils.Color;
import br.odb.gameutils.math.Vec2;

/**
 * @author Daniel "Monty" Monteiro
 */
public class ColoredPolygon {

    final public ArrayList<Vec2> controlPoints = new ArrayList<>();
    final private ArrayList<Vec2> points = new ArrayList<>();
    public Color color = new Color();
    public String originalStyle;
    public String gradient;
    public float[] xpoints;
    public float[] ypoints;
    public int z = 0;
    public int npoints;
    public String id;
    public boolean visible = true;


    public void addPoint(float x, float y) {
        Vec2 p = new Vec2();
        p.x = x;
        p.y = y;
        points.add(p);
        p = new Vec2();
        controlPoints.add(p);
        p.invalidate();
        updateState();
    }

    private void updateState() {
        npoints = points.size();

        xpoints = new float[npoints];
        ypoints = new float[npoints];
        Vec2 v;

        for (int c = 0; c < npoints; ++c) {
            v = points.get(c);
            xpoints[c] = v.x;
            ypoints[c] = v.y;
        }
    }

    public Vec2 getCenter() {

        Vec2 v;
        Vec2 center = new Vec2();

        for (int c = 0; c < npoints; ++c) {
            v = points.get(c);
            center.x += v.x;
            center.y += v.y;
        }
        center.x /= npoints;
        center.y /= npoints;

        return center;
    }

    public ColoredPolygon scale(float width, float height) {

        ColoredPolygon toReturn = new ColoredPolygon();

        toReturn.color.set(color);
        toReturn.originalStyle = originalStyle;
        toReturn.z = z;
        toReturn.id = id;
        toReturn.visible = visible;

        for (Vec2 p : points) {
            toReturn.points.add(new Vec2(p).scaled(width, height));
        }

        for (Vec2 c : controlPoints) {
            toReturn.controlPoints.add(new Vec2(c).scaled(width, height));
        }

        toReturn.updateState();

        return toReturn;
    }
}
