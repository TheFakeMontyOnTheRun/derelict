/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.odb.gamelib.swing;

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
        graphics.setColor(new java.awt.Color(color.r, color.g, color.b));
        graphics.fillRect((int) rect.p0.x, (int) rect.p0.y, (int) rect.getDX(), (int) rect.getDY());
    }

    @Override
    public void prepareForRendering() {
    }

    @Override
    public void endRendering() {
    }

    @Override
    public void drawColoredPolygon(ColoredPolygon c, Rect bounds, String style, HashMap<String, SVGParsingUtils.Gradient> gradients) {

        int[] xpoints = null;
        int[] ypoints = null;
        GeneralPath path = new GeneralPath();

        Vec2 point = c.controlPoints.get(0);
        Vec2 origin = bounds.p0.add(this.currentOrigin);
        float diffX = 0;
        float diffY = 0;

        graphics.setColor(SwingUtils.getSwingColor(c.color));

        path.moveTo(point.x + currentOrigin.x, point.y + currentOrigin.y);

        if (style != null) {
            xpoints = new int[c.npoints];
            ypoints = new int[c.npoints];
        }

        for (int i = 0; i < c.npoints; ++i) {

            if (c.controlPoints.get(i).isValid()) {

                Vec2 control = c.controlPoints.get(i);
                path.curveTo( origin.x + (c.xpoints[ i]) - diffX, 
                            origin.y + c.ypoints[ i] - diffY, 
                            origin.x + control.x,
                        origin.y + control.y - diffY, 
                        origin.x + (c.xpoints[ i + 1]) - diffX, 
                        origin.y + c.ypoints[ i + 1] - diffY);
                ++i;

            } else {
                path.lineTo(c.xpoints[ i] + currentOrigin.x, c.ypoints[ i] + currentOrigin.y);

                if (style != null) {
                    xpoints[ i] = (int) c.xpoints[ i];
                    ypoints[ i] = (int) c.ypoints[ i];
                }
            }
        }

        path.closePath();

        ((Graphics2D) graphics).fill(path);

        graphics.setColor(SwingUtils.getSwingColor(Color.getColorFromHTMLColor("#000000")));

        if (style != null) {
            ((Graphics2D) graphics).drawPolyline(xpoints, ypoints, c.npoints);
        }

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

    @Override
    public void drawBitmap(RasterImage ri, Vec2 vec2, Vec2 vec21, int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void drawLine(Vec2 p0, Vec2 p1) {
        graphics.drawLine((int) p0.x, (int) p0.y, (int) p1.x, (int) p1.y);
    }

    @Override
    public void drawText(Vec2 p0, String content, Color color, int fontSize) {
        graphics.drawString(content, (int) p0.x, (int) p0.y);

    }
}
