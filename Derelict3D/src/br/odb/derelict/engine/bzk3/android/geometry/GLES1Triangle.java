package br.odb.derelict.engine.bzk3.android.geometry;
 
import static android.opengl.GLES10.glColorPointer;
import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES10.glVertexPointer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import br.odb.libstrip.AbstractTriangle;
import br.odb.libstrip.GeneralTriangle;
import br.odb.libstrip.IndexedSetFace;
import br.odb.libstrip.Material;
import br.odb.utils.Color;
import br.odb.utils.math.Vec3;
/**
 * 
 * @author monty
 *
 */
public class GLES1Triangle extends GeneralTriangle implements GLESIndexedSetFace, AbstractTriangle  {

	private FloatBuffer colorBuffer;
	private FloatBuffer vertexBuffer;
	private float[] vertices = new float[9];
	int[] verticesBits = new int[9];
	private float[] color = new float[12];
	int[] colorBits = new int[ 4 ];
	private boolean visible = true;

	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	@Override
	public void addIndex(int index) {
	}
	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	@Override
	public void addVertex(Vec3 v) {
		// TODO Auto-generated method stub

	}
	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	@Override
	public void draw(GL10 gl) {

		if ( visible ) {
			
			gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vertices.length / 3);
		}
	}
	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	@Override
	public void draw() {
		
		if ( visible ) {
			
			glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);
			glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			glDrawArrays(GL10.GL_TRIANGLES, 0, vertices.length / 3);
		}
	}

	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	public void flushToGLES() {
		
		vertices[0] = x0;
		vertices[1] = y0;
		vertices[2] = z0;
		
		vertices[3] = x1;
		vertices[4] = y1;
		vertices[5] = z1;
		
		vertices[6] = x2;
		vertices[7] = y2;
		vertices[8] = z2;
		
		color[0] = r / 255.0f;
		color[1] = g / 255.0f;
		color[2] = b / 255.0f;
		color[3] = a / 255.0f;

		color[4] = r / 255.0f;
		color[5] = g / 255.0f;
		color[6] = b / 255.0f;
		color[7] = a / 255.0f;

		color[8] = r / 255.0f;
		color[9] = g / 255.0f;
		color[10] = b / 255.0f;
		color[11] = a / 255.0f;

		for ( int c = 0; c < vertices.length; ++c ) {
			verticesBits[ c ] = Float.floatToRawIntBits( vertices[ c ] );
		}
		
		for ( int c = 0; c < colorBits.length; ++c ) {
			colorBits[ c ] = Float.floatToRawIntBits( color[ c ] );
		}
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(color.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		colorBuffer = byteBuf.asFloatBuffer();
		colorBuffer.put(color);
		colorBuffer.position(0);

	}
	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	@Override
	public Color getColor() {

		return new Color( color[ 0 ], color[ 1 ], color[ 2 ], color[ 3 ] );
	}
	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	@Override
	public float getColor(int i) {
		return color[i];
	}
	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	@Override
	public float[] getColorData() {
		return color;
	}
	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	@Override
	public int getIndex(int d) {

		return d;
	}
	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	@Override
	public int getTotalIndexes() {

		return 3;
	}
	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	@Override
	public Vec3 getVertex(int c) {
		// TODO Auto-generated method stub
		return null;
	}
	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	@Override
	public float[] getVertexData() {
		return vertices;
	}
	
	public float[] singleColorData() {
		
		float[] colorData = new float[ 4 ];
		colorData[ 0 ] = this.color[0];
		colorData[ 1 ] = this.color[1];
		colorData[ 2 ] = this.color[2];
		colorData[ 3 ] = this.color[3];
		return colorData;
	}
	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	@Override
	public boolean getVisibility() {

		return visible;
	}
	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	@Override
	public IndexedSetFace makeCopy() {
		GLES1Triangle t = new GLES1Triangle();
		t.a = a;
		t.b = b;
		t.g = g;
		t.r = r;

		t.visible = visible;
		t.x0 = x0;
		t.x1 = x1;
		t.x2 = x2;

		t.y0 = y0;
		t.y1 = y1;
		t.y2 = y2;

		t.z0 = z0;
		t.z1 = z1;
		t.z2 = z2;

		t.flushToGLES();

		return t;
	}
	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	@Override
	public void setColor( Color c) {
		this.color[0] = c.getA();
		this.color[1] = c.getR();
		this.color[2] = c.getG();
		this.color[3] = c.getB();

		
		for ( int d = 0; d < colorBits.length; ++d ) {
			colorBits[ d ] = Float.floatToRawIntBits( color[ d ] );
		}
	}
	// ------------------------------------------------------------------------------------------------------------
	 public void drawGLES2(int vertexHandle, int colorHandle ) {
	 GLES20.glVertexAttribPointer( vertexHandle, vertices.length / 3,
	 GLES20.GL_FLOAT, false, 0, vertexBuffer );
	 GLES20.glEnableVertexAttribArray( vertexHandle );
	
	 GLES20.glVertexAttribPointer( colorHandle, 4, GLES20.GL_FLOAT, false, 0,
	 colorBuffer );
	 GLES20.glEnableVertexAttribArray( colorHandle );
	
	 GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 3 );
	 }
	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	@Override
	public void setVisibility(boolean visibility) {

		visible = visibility;
	}
	@Override
	public Material getMaterial() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Vec3 makeNormal() {
		
		Vec3 v1;
		Vec3 v2;
		Vec3 vn;
		
		v1 = new Vec3( x1 - x0, y1 - y0, z1 - z0 );
		v2 = new Vec3( x2 - x0, y2 - y0, z2 - z0 );
		
		vn = v1.crossProduct( v2 );
		
		return vn;
	}
	// ------------------------------------------------------------------------------------------------------------
	/**
	 * 
	 */
	public void flatten( float z ) {
		z0 = z1 = z2 = z;
	}
	public void flatten() {
		
		flatten( -1.2f );
	}
	@Override
	public void destroy() {

		colorBuffer.clear();
		colorBuffer = null;
		vertexBuffer.clear();
		vertexBuffer = null;
		color = null;
		vertices = null;
		colorBits = null;
		verticesBits = null;
	}
	public void multiplyColor( float factor ) {
		
		for ( int c = 0; c < color.length; ++ c )
			color[ c ] *= factor;
		
		
		for ( int d = 0; d < colorBits.length; ++d ) {
			colorBits[ d ] = Float.floatToRawIntBits( color[ d ] );
		}
	}
}
