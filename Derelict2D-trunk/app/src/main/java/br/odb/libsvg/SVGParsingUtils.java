package br.odb.libsvg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import br.odb.gameutils.Color;

/**
 * @author monty
 */
public final class SVGParsingUtils {

    /**
     *
     */
    public static SVGGraphic readSVG(InputStream is) {

        float x = 0;
        float y = 0;
        float width = 0;
        float height = 0;
        ArrayList<ColoredPolygon> instance = new ArrayList<>();
        SVGGraphic toReturn = new SVGGraphic();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);
            doc.getDocumentElement().normalize();
            Color color;
            ColoredPolygon p = null;
            NodeList nodeLst;
            String style;
            nodeLst = doc.getElementsByTagName("*");

            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node fstNode = nodeLst.item(s);

                color = null;

                if (fstNode != null) {

                    if (fstNode.getNodeType() == Node.ELEMENT_NODE
                            && (fstNode.getNodeName().equalsIgnoreCase("path")
                            || fstNode.getNodeName().equalsIgnoreCase(
                            "rect") || fstNode.getNodeName()
                            .equalsIgnoreCase("linearGradient")

                    )) {

                        Element fstElmnt = (Element) fstNode;
                        style = fstElmnt.getAttribute("style");

                        if (style != null && style.length() > 0)
                            color = SVGUtils.parseColorFromStyle(fstElmnt
                                    .getAttribute("style"));

                        if (color == null)
                            color = new Color(0xFF000000);

                        if (fstNode.getNodeName().equalsIgnoreCase("path")) {

                            String d = null;

                            NamedNodeMap properties = fstElmnt.getAttributes();

                            for (int j = 0; j < properties.getLength(); j++) {
                                Node property = properties.item(j);
                                String name = property.getNodeName();

                                if (name.equalsIgnoreCase("d")) {
                                    d = property.getNodeValue();
                                }

                                if (name.equalsIgnoreCase("style")) {
                                    style = property.getNodeValue();
                                }
                            }

                            p = parsePath(d, style);

                        } else if (fstNode.getNodeName().equalsIgnoreCase(
                                "rect")) {
                            p = new ColoredPolygon();

                            if (fstElmnt.getAttribute("x").length() > 0)
                                x = Float
                                        .parseFloat(fstElmnt.getAttribute("x"));

                            if (fstElmnt.getAttribute("y").length() > 0)
                                y = Float
                                        .parseFloat(fstElmnt.getAttribute("y"));

                            if (fstElmnt.getAttribute("width").length() > 0)
                                width = Float.parseFloat(fstElmnt
                                        .getAttribute("width"));

                            if (fstElmnt.getAttribute("height").length() > 0)
                                height = Float.parseFloat(fstElmnt
                                        .getAttribute("height"));

                            p.addPoint(x, y);
                            p.addPoint(x + width, y);
                            p.addPoint(x + width, y + height);
                            p.addPoint(x, y + height);
                        } else if (fstNode.getNodeName().equalsIgnoreCase(
                                "linearGradient")) {

                            NodeList nl = fstNode.getChildNodes();
                            Node stop;
                            HashMap<Integer, GradientStop> stops = new HashMap<>();
                            NamedNodeMap data = fstNode.getAttributes();

                            Gradient g = new Gradient();
                            g.id = data.getNamedItem("id").getNodeValue();

                            if (data.getNamedItem("xlink:href") != null) {

                                // 0:#
                                g.link = data.getNamedItem("xlink:href")
                                        .getNodeValue().substring(1);
                            }

                            if (data.getNamedItem("x1") != null) {

                                g.x1 = Float.parseFloat(data.getNamedItem("x1")
                                        .getNodeValue());
                            }

                            if (data.getNamedItem("y1") != null) {

                                g.y1 = Float.parseFloat(data.getNamedItem("y1")
                                        .getNodeValue());
                            }

                            if (data.getNamedItem("x2") != null) {

                                g.x2 = Float.parseFloat(data.getNamedItem("x2")
                                        .getNodeValue());
                            }

                            if (data.getNamedItem("y2") != null) {

                                g.y2 = Float.parseFloat(data.getNamedItem("y2")
                                        .getNodeValue());
                            }

                            for (int c = 0; c < nl.getLength(); ++c) {

                                stop = nl.item(c);
                                if (stop.getNodeName().equalsIgnoreCase("stop")) {

                                    NamedNodeMap attr = stop.getAttributes();

                                    GradientStop gs = new GradientStop();
                                    attr.getNamedItem("id").getNodeValue();
                                    gs.index = Integer.parseInt(attr
                                            .getNamedItem("offset")
                                            .getNodeValue());
                                    gs.style = attr.getNamedItem("style")
                                            .getNodeValue();
                                    stops.put(gs.index, gs);
                                }
                            }

                            if (stops.size() > 0) {

                                g.stops = new GradientStop[stops.size()];

                                for (int c = 0; c < stops.size(); ++c) {

                                    g.stops[c] = stops.get(c);
                                }

                                stops.clear();
                            }

                            toReturn.gradients.put(g.id, g);
                        }

                        if (p != null) {

                            if (fstElmnt != null) {

                                if (fstElmnt.hasAttribute("z"))
                                    p.z = (int) Float.parseFloat(fstElmnt
                                            .getAttribute("z"));

                                if (fstElmnt.hasAttribute("id"))
                                    p.id = fstElmnt.getAttribute("id");
                            }

                            p.color = color;
                            p.originalStyle = fstElmnt.getAttribute("style");
                            instance.add(p);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        toReturn.shapes = new ColoredPolygon[instance.size()];
        instance.toArray(toReturn.shapes);

        consolidateGradients(toReturn);

        return toReturn;
    }

    private static void consolidateGradients(SVGGraphic graphic) {
        for (Gradient g : graphic.gradients.values()) {
            if (g.link != null) {
                g.copy(graphic.gradients);
            }
        }
    }

    private static ColoredPolygon parsePath(String nodeValue, String style) {

        ColoredPolygon pol = SVGUtils.parseD(nodeValue, 800, 480);
        pol.color = SVGUtils.parseColorFromStyle(style);
        pol.originalStyle = style;

        return pol;
    }

    public static final class Gradient {

        public GradientStop[] stops;
        public float x1;
        public float x2;
        public float y1;
        public float y2;
        String id;
        String link;
        Gradient() {
        }

        void copy(HashMap<String, Gradient> gradients) {

            if (gradients.get(link).link != null) {
                gradients.get(link).copy(gradients);
            }

            stops = gradients.get(link).stops;
        }

    }

    public static final class GradientStop {

        public String style;
        public Color color;
        int index;
        GradientStop() {
        }
        GradientStop(GradientStop gradientStop) {
            this.index = gradientStop.index;
            this.style = gradientStop.style;
        }
    }
}
