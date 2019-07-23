package br.odb.gameutils.math;

import br.odb.gameutils.Utils;

/**
 * @author monty
 * 
 */
public class Matrix {
	
	private float[] values;
	private final int sizeX;
	private final int sizeY;

	private Matrix(int dx, int dy, float[] rawData) {
		sizeX = dx;
		sizeY = dy;
		initWithRawData(rawData);
	}

	public Matrix(Matrix matrix) {
		sizeX = matrix.sizeX;
		sizeY = matrix.sizeY;
		init(matrix);
	}

	public Matrix(int dx, int dy) {
		sizeX = dx;
		sizeY = dy;
		initWithRawData(null);
	}

	private void initWithRawData( float[] rawData) {

		int head = 0;
		values = new float[ sizeX * sizeY ];

		for (int c = 0; c <  sizeY; ++c) {

			for (int d = 0; d < sizeX; ++d) {

				if (rawData != null && head < rawData.length) {
					values[ ( c * sizeX ) + d ] = rawData[head];
					++head;
				} else {
					values[ ( c * sizeX ) + d ] = 0.0f;
				}
			}
		}
	}

	private float[] getRawDataCopy() {
		float[] toReturn = new float[ sizeX * sizeY ];

		int used = 0;

		for (int c = 0; c < sizeY; ++c) {
			for (int d = 0; d < sizeX; ++d) {
				toReturn[used] = values[ ( c * sizeX ) + d ];
				++used;
			}
		}

		return toReturn;
	}

	private void init(Matrix m) {
		initWithRawData( m.getRawDataCopy());
	}
	
	
	public void setAsIdentity() {
		for (int x = 0; x < sizeX; ++x) {
			for (int y = 0; y < sizeY; ++y) {
				this.values[(sizeX * y) + x] = (x == y) ? 1.0f : 0.0f;
			}
		}
	}

	public static Matrix makeIdentity(int size) {

		float[] rawData = new float[size * size];

		for (int x = 0; x < size; ++x) {
			for (int y = 0; y < size; ++y) {
				rawData[(size * y) + x] = (x == y) ? 1.0f : 0.0f;
			}
		}

		return new Matrix(size, size, rawData);
	}

	public static Matrix makeRotationAlongZ(double rads) {

		float sin = (float) Math.sin(Math.PI / 2.0f);
		float cos = (float) Math.cos(Math.PI / 2.0f);
		float[] rawData = new float[9];
		rawData[0] = cos;
		rawData[1] = -sin;
		rawData[2] = 0.0f;
		rawData[3] = sin;
		rawData[4] = cos;
		rawData[5] = 0.0f;
		rawData[6] = 0.0f;
		rawData[7] = 0.0f;
		rawData[8] = 1.0f;

		return new Matrix(3, 3, rawData);
	}

	public boolean isIdentity() {

		for (int x = 0; x < sizeX; ++x) {
			for (int y = 0; y < sizeY; ++y) {

				if (x == y) {
					if ( Math.abs( values[ ( y * sizeX ) + x  ] - 1.0f ) > 0.1f ) {						
						return false;
					}
				} else {
					if (!Utils.eqFloat(0.0f, values[ ( y * sizeX ) + x ]))
						return false;
				}
			}
		}

		return true;
	}

	public boolean isNullMatrix() {

		for (int x = 0; x < sizeX; ++x) {

			for (int y = 0; y < sizeY; ++y) {

				if (!Utils.eqFloat(0.0f, values[ ( y * sizeX ) + x ]))
					return false;
			}
		}

		return true;
	}

	public float get( int x, int y ) {
		return values[ ( y * sizeX ) + x ];
	}
	
	public void set(int x, int y, float f) {
		values[ ( y * sizeX ) + x ] = f;		
	}
}
