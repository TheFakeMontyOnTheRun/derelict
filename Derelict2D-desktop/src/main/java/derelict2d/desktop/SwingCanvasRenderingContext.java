/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package derelict2d.desktop;


import br.odb.gamerendering.rendering.RasterImage;
import br.odb.gamerendering.rendering.RenderingContext;
import br.odb.libsvg.ColoredPolygon;
import br.odb.libsvg.SVGParsingUtils;
import br.odb.utils.Color;
import br.odb.utils.Rect;
import br.odb.utils.math.Vec2;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.util.HashMap;

/**
 *
 * @author monty
 */
public class SwingCanvasRenderingContext extends RenderingContext {

    private Graphics graphics;

    public void prepareWithCanvas(Graphics g) {
        this.graphics = g;

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    @Override
    public void fillRect(Color color, Rect rect) {
        graphics.setColor(new java.awt.Color(color.getR(), color.getG(), color.getB()));
        graphics.fillRect((int) rect.x0, (int) rect.y0, (int) rect.getDX(), (int) rect.getDY());
    }

    @Override
    public void prepareForRendering() {
    }

    @Override
    public void endRendering() {
    }

    @Override
    public void drawColoredPolygon(ColoredPolygon c, Rect bounds, String style, HashMap<String, SVGParsingUtils.Gradient> gradients) {

        GeneralPath path = new GeneralPath();

        Vec2 point = c.controlPoints.get(0);

        graphics.setColor(SwingUtils.getSwingColor(c.color));

        path.moveTo(point.x + currentOrigin.x, point.y + currentOrigin.y);


        for (int i = 0; i < c.npoints; ++i) {

            path.lineTo(c.xpoints[ i] + currentOrigin.x, c.ypoints[ i] + currentOrigin.y);
        }

        path.closePath();

        ((Graphics2D) graphics).fill(path);
    }

    @Override
    public void saveClipRect() {
    }

    @Override
    public void restoreClipRect() {
    }

    @Override
    public void drawBitmap(RasterImage tileImage, Vec2 p0) {
    }

    @Override
    public void drawBitmap(RasterImage tileImage, Rect bounds) {
    }

    @Override
    public void setClipRect(Rect bounds) {
    }
}
