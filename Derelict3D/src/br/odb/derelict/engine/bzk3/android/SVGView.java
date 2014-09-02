package br.odb.derelict.engine.bzk3.android;

import java.io.IOException;
import java.util.HashMap;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import br.odb.derelict.R;
import br.odb.libsvg.ColoredPolygon;
import br.odb.libsvg.SVGGraphic;
import br.odb.libsvg.SVGParsingUtils;
import br.odb.libsvg.SVGParsingUtils.Gradient;
import br.odb.libsvg.SVGUtils;
import br.odb.utils.Color;
import br.odb.utils.math.Vec2;

public class SVGView extends View {

//	public class Gradient {
//		public String id;
//		public String link;
//		GradientStop[] stops;
//		public float x1;
//		public float x2;
//		public float y1;
//		public float y2;
//
//	}
//
//	private class GradientStop {
//		public int index;
//		public String style;
//	}
	
	
	private Vec2 origin = new Vec2();

	public void setOrigin(Vec2 origin) {
		this.origin.set( origin );		
	}

	public Vec2 getOrigin() {
		
		return origin;
	}
	
	org.w3c.dom.Element root;
	XmlResourceParser xml;
	private Paint paint;
	
	SVGGraphic graphic;
	
	public SVGView(Context context, AttributeSet attributeSet)
	{
	    super(context, attributeSet);

	    init( "derelicttitleflat.svg" );
	}


	public SVGView(Context context) {
		super(context);
		
		init( "derelicttitleflat.svg" );
	}
	
	public void init( String pathToSVG ) {
		paint = new Paint();
		
		try {
			if ( !isInEditMode() )
				graphic = SVGParsingUtils.readSVG( getContext().getAssets().open( pathToSVG ) );
			else
				graphic = SVGParsingUtils.readSVG( getContext().getResources().openRawResource( R.raw.chapter4b ) );
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public void init( int resId ) {
//		Log.d("bzk3", "vou come��ar a ler agora");
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//
//		try {
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			Document dom = builder.parse( getContext().getAssets().open( pathToSVG ) );
//			root = dom.getDocumentElement();
//
//		} catch (Exception e) {
//			Log.d("bzk3", "deu merda mesmo");
//		}
//		
//		this.invalidate();
//	}
	
	private void drawColoredPolygon( Canvas canvas, Paint paint, ColoredPolygon pol, String style, HashMap<String, Gradient> gradients ) {
		
		float scale;
		float scaleX;
		float scaleY;
		float diffX;
		float diffY;
		
		String gradient = null;
		
//		if ( 800.0f < getWidth() )
			scaleX = getWidth() / 800.0f;
//		else
//			scaleX = 800.0f / getWidth();
		
//		if ( 480.0f < getHeight() )
			scaleY = getHeight() / 480.0f;
//		else
//			scaleY = 480.0f / getHeight();
		
		if ( scaleX > scaleY )
			scale = scaleX;
		else
			scale = scaleY;
		
//		if ( getHeight() < getWidth() )
//			scale = getHeight() / 480.0f;
//		else
//			scale = getWidth() / 800.0f;
		
		diffX = ( 800 * scale ) - getWidth();
		diffX /= 2.0f;
		
		diffY = ( 480 * scale ) - getHeight();
		diffY /= 2.0f;
		
		if ( pol.xpoints == null || pol.ypoints == null || pol.npoints <= 0 )
			return;
		
		Path path = new Path();
		path.offset( origin.x, origin.y );
		path.moveTo( ( origin.x + pol.xpoints[0] * scale ) - diffX, origin.y + pol.ypoints[0] * scale  - diffY );

		for (int c = 0; c < pol.npoints; ++c) {
			if (((Vec2) pol.controlPoints.get(c)).isValid()) {
				Vec2 control = (Vec2) pol.controlPoints.get(c);
				path.cubicTo( origin.x + ( pol.xpoints[c] * scale ) - diffX, origin.y + pol.ypoints[c] * scale  - diffY ,
						origin.x + control.x * scale, origin.y + control.y * scale  - diffY,
						origin.x + ( pol.xpoints[c + 1] * scale ) - diffX, origin.y + pol.ypoints[c + 1] * scale  - diffY );
				++c;
			} else
				path.lineTo( origin.x + ( pol.xpoints[c] * scale ) - diffX,origin.y + pol.ypoints[c] * scale  - diffY );
		}

		path.close();
		paint.setShader(null);
		paint.setAlpha( 255 );

		if (pol.originalStyle != null) {
			pol.color = SVGUtils.parseColorFromStyle( pol.originalStyle );
			gradient = SVGUtils.parseGradientFromStyle( pol.originalStyle );
		}

		if (gradient != null) {

			Gradient g0 = gradients.get(gradient);
			Gradient g1 = null;

			if (g0.stops == null && g0.link != null) {
				g1 = gradients.get(g0.link);
			} else {
				g1 = g0;
			}
			
			Color color1;
			Color color2;
			
			color1 = SVGUtils.parseColorFromStyle(g1.stops[0].style,
					"stop-color", "stop-opacity");
			color2 = SVGUtils.parseColorFromStyle(g1.stops[1].style,
					"stop-color", "stop-opacity");
			
			if ( pol.color != null ) {
//				paint.setAlpha( pol.color.getA() );
				color1.setA( (int) (( color1.getA() / 255.0f ) * ( pol.color.getA() / 255.0f ) * 255) );
				color2.setA( (int) (( color2.getA() / 255.0f ) * ( pol.color.getA() / 255.0f ) * 255) );
			}

			LinearGradient lg = new LinearGradient(g0.x1 * scale, g0.y1 * scale, g0.x2 * scale, g0.y2 * scale,
					(int) color1.getARGBColor(),
					(int) color2.getARGBColor(),
					Shader.TileMode.CLAMP);

			paint.setShader(lg);

		} else if (pol.color != null) {
			
			paint.setColor((int) pol.color.getARGBColor());
			paint.setAlpha( pol.color.getA() );
		} else
			paint.setColor(0xFF000000);

		canvas.drawPath(path, paint);
	}
	

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		
		
		paint.setColor(0xFF0000FF);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setAntiAlias(true);

		String style = null;
		
		for ( ColoredPolygon c : graphic.shapes ) {
			drawColoredPolygon( canvas, paint, c, style, graphic.gradients );
		}

//		
//
//		if (root != null) {
//
//			NodeList paths = root.getElementsByTagName("*");
//
//			for (int i = 0; i < paths.getLength(); i++) {
//
//				Node item = paths.item(i);
//
//				if (item.getNodeName().equalsIgnoreCase("linearGradient")) {
//
//					NodeList nl = item.getChildNodes();
//					Node stop;
//					HashMap<Integer, GradientStop> stops = new HashMap<Integer, SVGView.GradientStop>();
//					NamedNodeMap data = item.getAttributes();
//
//					Gradient g = new Gradient();
//					g.id = data.getNamedItem("id").getNodeValue();
//
//					if (data.getNamedItem("xlink:href") != null)
//						// 0:#
//						g.link = data.getNamedItem("xlink:href").getNodeValue()
//								.substring(1);
//
//					if (data.getNamedItem("x1") != null)
//						g.x1 = Float.parseFloat(data.getNamedItem("x1")
//								.getNodeValue());
//
//					if (data.getNamedItem("y1") != null)
//						g.y1 = Float.parseFloat(data.getNamedItem("y1")
//								.getNodeValue());
//
//					if (data.getNamedItem("x2") != null)
//						g.x2 = Float.parseFloat(data.getNamedItem("x2")
//								.getNodeValue());
//
//					if (data.getNamedItem("y2") != null)
//						g.y2 = Float.parseFloat(data.getNamedItem("y2")
//								.getNodeValue());
//
//					for (int c = 0; c < nl.getLength(); ++c) {
//
//						stop = nl.item(c);
//						if (stop.getNodeName().equalsIgnoreCase("stop")) {
//
//							NamedNodeMap attr = stop.getAttributes();
//
//							GradientStop gs = new GradientStop();
//							attr.getNamedItem("id").getNodeValue();
//							gs.index = Integer.parseInt(attr.getNamedItem(
//									"offset").getNodeValue());
//							gs.style = attr.getNamedItem("style")
//									.getNodeValue();
//							stops.put(Integer.valueOf(gs.index), gs);
//						}
//					}
//
//					if (stops.size() > 0) {
//
//						g.stops = new GradientStop[stops.size()];
//
//						for (int c = 0; c < stops.size(); ++c) {
//
//							g.stops[c] = stops.get(Integer.valueOf(c));
//						}
//
//						stops.clear();
//					}
//
//					gradients.put(g.id, g);
//				}
//
//				String d = null;
//				if (item.getNodeName().equalsIgnoreCase("rect")) {
//
//					float x = 0;
//					float y = 0;
//					float width = 0;
//					float height = 0;
//
//					if (item.getAttributes().getNamedItem("x") != null)
//						x = Float.parseFloat(item.getAttributes()
//								.getNamedItem("x").getNodeValue());
//
//					if (item.getAttributes().getNamedItem("y") != null)
//						y = Float.parseFloat(item.getAttributes()
//								.getNamedItem("y").getNodeValue());
//
//					if (item.getAttributes().getNamedItem("width") != null)
//						width = Float.parseFloat(item.getAttributes()
//								.getNamedItem("width").getNodeValue());
//
//					if (item.getAttributes().getNamedItem("height") != null)
//						height = Float.parseFloat(item.getAttributes()
//								.getNamedItem("height").getNodeValue());
//
//					Color color = null;
//					if (item.getAttributes().getNamedItem("style") != null)
//						color = SVGUtils.parseColorFromStyle(item
//								.getAttributes().getNamedItem("style")
//								.getNodeValue());
//
//					if (color != null)
//						paint.setColor((int) color.getARGBColor());
//					else
//						paint.setColor(0xFF000000);
//
//					Rect r = new Rect();
//					r.top = (int) y;
//					r.bottom = (int) (y + height);
//					r.right = (int) (x + width);
//					r.left = (int) x;
//
//					canvas.drawRect(r, paint);
//
//				} else if (item.getNodeName().equalsIgnoreCase("path")) {
//
//					NamedNodeMap properties = item.getAttributes();
//
//					for (int j = 0; j < properties.getLength(); j++) {
//						Node property = properties.item(j);
//						String name = property.getNodeName();
//						Log.d("bzk3", "::" + name);
//						if (name.equalsIgnoreCase("d")) {
//							d = property.getNodeValue();
//						}
//
//						if (name.equalsIgnoreCase("style")) {
//							style = property.getNodeValue();
//						}
//					}
//				}
//
//				if (d != null)
//					parsePath(canvas, paint, d, style);
//			}
//		}
	}
//
//	private void parsePath(Canvas canvas, Paint paint, String nodeValue,
//			String style) {
//		
//		float scale;
//		
//		if ( getHeight() < getWidth() )
//			scale = getHeight() / 480.0f;
//		else
//			scale = getWidth() / 800.0f;
//		
//
//		ColoredPolygon pol = SVGUtils
//				.parseD(nodeValue, getWidth(), getHeight());
//		
//		if ( pol.xpoints == null || pol.ypoints == null || pol.npoints <= 0 )
//			return;
//			
//		pol.color = SVGUtils.parseColorFromStyle(style);
//
//		String gradient = null;
//
//		if (pol.color == null)
//			gradient = SVGUtils.parseGradientFromStyle(style);
//
//		Path path = new Path();
//		path.moveTo( pol.xpoints[0] * scale, pol.ypoints[0] * scale );
//
//		for (int c = 0; c < pol.npoints; ++c) {
//			if (((Vec2) pol.controlPoints.get(c)).isValid()) {
//				Vec2 control = (Vec2) pol.controlPoints.get(c);
//				path.cubicTo(pol.xpoints[c] * scale, pol.ypoints[c] * scale, control.x * scale,
//						control.y * scale, pol.xpoints[c + 1] * scale, pol.ypoints[c + 1] * scale );
//				++c;
//			} else
//				path.lineTo(pol.xpoints[c] * scale, pol.ypoints[c] * scale );
//		}
//
//		path.close();
//		paint.setShader(null);
//		paint.setAlpha(255);
//
//		if (gradient != null) {
//
//			Gradient g0 = gradients.get(gradient);
//			Gradient g1 = null;
//
//			if (g0.stops == null && g0.link != null) {
//				g1 = gradients.get(g0.link);
//			} else {
//				g1 = g0;
//			}
//
//			LinearGradient lg = new LinearGradient(g0.x1, g0.y1, g0.x2, g0.y2,
//					(int) SVGUtils.parseColorFromStyle(g1.stops[0].style,
//							"stop-color", "stop-opacity").getARGBColor(),
//					(int) SVGUtils.parseColorFromStyle(g1.stops[1].style,
//							"stop-color", "stop-opacity").getARGBColor(),
//					Shader.TileMode.MIRROR);
//
//			paint.setShader(lg);
//
//		} else if (pol.color != null)
//			paint.setColor((int) pol.color.getARGBColor());
//		else
//			paint.setColor(0xFF000000);
//
//		canvas.drawPath(path, paint);
//	}
}
