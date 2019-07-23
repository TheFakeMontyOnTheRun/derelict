package br.odb.libsvg;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import br.odb.gameutils.Color;
import br.odb.gameutils.math.Vec2;

/**
 * 
 * @author Daniel "Monty" Monteiro
 */
public class ColoredPolygon {
	
	public Color color = new Color();
	final private ArrayList<Vec2> points = new ArrayList<>();
	final public ArrayList<Vec2> controlPoints = new ArrayList<>();
	public String originalStyle;
	public String gradient;
	public float[] xpoints;
	public float[] ypoints;
	public int z = 0;
	public int npoints;
	public String id;
	public boolean visible = true;


	public String getSVGString() {
		int n;
		StringBuilder toReturn = new StringBuilder();

		if (this.npoints <= 0)
			return toReturn.toString();

		toReturn = new StringBuilder("<path ");
		toReturn.append("z = '").append(z).append("' ");
		toReturn.append(" style='");
		toReturn.append(originalStyle);

		if (id != null) {

			toReturn.append("' id='");
			toReturn.append(id);
		}

		toReturn.append("' d='");
		toReturn.append(" M ");
		toReturn.append(xpoints[0]);
		toReturn.append(",");
		toReturn.append(ypoints[0]);

		for (int c = 0; c < npoints; ++c) {

			n = c % (this.npoints);
			toReturn.append(" L ");
			toReturn.append(this.xpoints[n]);
			toReturn.append(",");
			toReturn.append(this.ypoints[n]);
		}

		toReturn.append(" z' />\n");
		return toReturn.toString();
	}

	public void writePath(OutputStream os) {

		byte[] bytes = new byte[11];
		bytes[0] = encodeUnsingedValueIntoByte(color.a);
		bytes[1] = encodeUnsingedValueIntoByte(color.r);
		bytes[2] = encodeUnsingedValueIntoByte(color.g);
		bytes[3] = encodeUnsingedValueIntoByte(color.b);

		bytes[4] = normalize(xpoints[0], 800);
		bytes[5] = normalize(ypoints[0], 480);

		bytes[6] = normalize(xpoints[1], 800);
		bytes[7] = normalize(ypoints[1], 480);

		bytes[8] = normalize(xpoints[2], 800);
		bytes[9] = normalize(ypoints[2], 480);

		bytes[10] = encodeUnsingedValueIntoByte(z);

		try {
			os.write(bytes);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

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

	public void buildStyleProperty() {

		if (color == null)
			return;

		originalStyle = "fill:" + color.getHTMLColor() + ";";

		if (color.a != 255) {
			originalStyle += "opacity: " + (color.a / 255.0f) + ";";
		}
	}

	public void readEdges( FileInputStream is ) {
		DataInputStream dis = new DataInputStream( is );
		byte[] bytes = new byte[9]; 

		try {

			npoints = dis.readInt();

                        xpoints = new float[ npoints ];
                        ypoints = new float[ npoints ];
                        
			for (int c = 0; c < npoints; ++c) {
				
				dis.read(bytes);

				color.a = decodeUnsingedValueIntoByte( bytes[ 0 ] );
				color.r = decodeUnsingedValueIntoByte( bytes[ 1 ] );
				color.g = decodeUnsingedValueIntoByte( bytes[ 2 ] );
				color.b = decodeUnsingedValueIntoByte( bytes[ 3 ] );
				
                                
                                
                                xpoints[(c ) % npoints] = normalize( bytes[4], 800);
				ypoints[(c ) % npoints] = normalize( bytes[5], 800);
                                
                                controlPoints.add( new Vec2( xpoints[(c ) % npoints], ypoints[(c ) % npoints] ) );

                                xpoints[(c + 1) % npoints] = normalize( bytes[6], 800);
				ypoints[(c + 1) % npoints] = normalize( bytes[7], 800);
				z = decodeUnsingedValueIntoByte( bytes[8] );
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void writeEdges(FileOutputStream os) {

		byte[] bytes = new byte[9];
		DataOutputStream dos = new DataOutputStream(os);

		try {

			dos.writeInt(npoints);

			for (int c = 0; c < npoints; ++c) {

				bytes[0] = encodeUnsingedValueIntoByte(color.a );
				bytes[1] = encodeUnsingedValueIntoByte(color.r);
				bytes[2] = encodeUnsingedValueIntoByte(color.g);
				bytes[3] = encodeUnsingedValueIntoByte(color.b);

				bytes[4] = normalize(xpoints[c % npoints], 800);
				bytes[5] = normalize(ypoints[c % npoints], 480);

				bytes[6] = normalize(xpoints[(c + 1) % npoints], 800);
				bytes[7] = normalize(ypoints[(c + 1) % npoints], 480);

				bytes[8] = encodeUnsingedValueIntoByte(z);

				os.write(bytes);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private byte decodeUnsingedValueIntoByte(int uv) {

		return (byte) (uv + Byte.MIN_VALUE);
	}
	
	
	private byte encodeUnsingedValueIntoByte(int uv) {

		return (byte) (uv - Byte.MIN_VALUE);
	}

	// O ideal seria pegar o maior e o menor dos valores e usar como minimum e
	// maximum
	private byte normalize(float f, float maximum) {

		int range = Byte.MAX_VALUE - Byte.MIN_VALUE;

		// f é primeiro normalizado, para depois ser
		// transformado para uma
		// nova base de espaço vetorial
		return encodeUnsingedValueIntoByte((int) ((f * range) / maximum));
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
		
		toReturn.color.set( color );
		toReturn.originalStyle = originalStyle;
		toReturn.z = z;
		toReturn.id = id;
		toReturn.visible = visible;

		for ( Vec2 p : points ) {
			toReturn.points.add( new Vec2( p ).scaled(  width, height) );
		}
		
		for ( Vec2 c : controlPoints ) {
			toReturn.controlPoints.add( new Vec2( c ).scaled( width, height ) );
		}
		
		toReturn.updateState();
		
		return toReturn;
	}
}
