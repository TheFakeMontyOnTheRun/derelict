/**

 * @author Daniel Monteiro
 * */

package br.odb.derelict.engine.bzk3.android.geometry;

import static android.opengl.GLES10.glClear;
import static android.opengl.GLES10.glClearColor;
import static android.opengl.GLES10.glClearDepthf;
import static android.opengl.GLES10.glDepthFunc;
import static android.opengl.GLES10.glDisableClientState;
import static android.opengl.GLES10.glEnable;
import static android.opengl.GLES10.glEnableClientState;
import static android.opengl.GLES10.glFrontFace;
import static android.opengl.GLES10.glHint;
import static android.opengl.GLES10.glLoadIdentity;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;
import static android.opengl.GLES10.glRotatef;
import static android.opengl.GLES10.glShadeModel;
import static android.opengl.GLES10.glTranslatef;
import static android.opengl.GLES10.glViewport;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;
import br.odb.derelict.engine.bzk3.android.AndroidGameActor;
import br.odb.derelict.menus.PlayGameActivity;
import br.odb.gamerendering.rendering.Constants;
import br.odb.gameworld.Actor;
import br.odb.gameworld.exceptions.InvalidSlotException;
import br.odb.libscene.Actor3D;
import br.odb.libscene.ObjMesh;
import br.odb.libscene.Sector;
import br.odb.libstrip.IndexedSetFace;
import br.odb.libstrip.Mesh;
import br.odb.littlehelper3d.Decal;
import br.odb.littlehelper3d.GameActor;
import br.odb.littlehelper3d.GameSector;
import br.odb.littlehelper3d.GameWorld;
import br.odb.utils.Utils;
import br.odb.utils.math.Vec3;

/*
 * Concerns:
 * - show the world from the chosen actor's view.
 * - do it in the most efficient way
 * - so, work to keep showing stuff efficiently
 * */

public class GLESRenderer implements GLSurfaceView.Renderer {
	// ////GLES2 stuff/////

	private int mProgram;
	private int maPositionHandle;
	private int colorHandle;
	private int muMVPMatrixHandle;
	private float[] mMVPMatrix = new float[16];
	private float[] mMMatrix = new float[16];
	private float[] mVMatrix = new float[16];
	private float[] mProjMatrix = new float[16];

	private int loadShader(int type, String shaderCode) {

		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);

		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}

	// ///////////////////
	int GLESVersion;
	final private ArrayList<GameActor> actors;
	private float angle;
	int[] lights = new int[96];
	private int back;
	private GLESVertexArrayManager fixedGeometryManager;
	public AndroidGameActor cameraActor;
	private int direction;
	float parcel;
	private String displayList;
	private long frame;
	final GLESVertexArrayManager manager = new GLESVertexArrayManager();
	final private ArrayList<GLESIndexedSetFace> sceneGeometryToRender;
	final public ArrayList<GLESIndexedSetFace> fixedScreenShapesToRender;
	final public ArrayList<GLESIndexedSetFace> screenShapesToRender;
	private int top;
	int visiblePolys;
	private GameSector[] toVisit;
	// private int[] incidingVisit;
	public static final Vec3[] relativePositions = {
			new Vec3(0.0f, 0.0f, -1.0f), new Vec3(1.0f, 0.0f, 0.0f),
			new Vec3(0.0f, 0.0f, 1.0f), new Vec3(-1.0f, 0.0f, 0.0f),
			new Vec3(0.0f, -1.0f, 0.0f), new Vec3(0.0f, 1.0f, 0.0f) };

	private GameWorld world;
	final private ArrayList<AndroidGameActor> lightEmissors;
	private boolean shouldResetView = true;
	private ArrayList<Mesh> meshes = new ArrayList<Mesh>();
	public Vec3 accel;
	boolean attached;
	private boolean shouldCheckForBailingOut;

	// ------------------------------------------------------------------------------------------------------------
	public GLESRenderer(int GLESVersion) {
		super();
		this.GLESVersion = GLESVersion;
		lightEmissors = new ArrayList<AndroidGameActor>();
		sceneGeometryToRender = new ArrayList<GLESIndexedSetFace>();
		actors = new ArrayList<GameActor>();
		screenShapesToRender = new ArrayList<GLESIndexedSetFace>();
		fixedScreenShapesToRender = new ArrayList<GLESIndexedSetFace>();
		accel = new Vec3();
	}

	// ------------------------------------------------------------------------------------------------------------

	public void addGeometryToScene(GLESIndexedSetFace isf) {
		sceneGeometryToRender.add(isf);
	}

	public void addGeometryToScreen(GLESIndexedSetFace s) {
		screenShapesToRender.add(s);

	}

	public void addToScene(GameActor actor) {

		actors.add(actor);
	}

	public void addToScene(GameSector sector) {
		GLES1Square square;

		for (int d = 0; d < 6; d++)

			if ((sector.cachedNeighBours[d] == null || sector.getDoor(d) != null)
					&& sector.meshWalls[d] == null) {

				square = null;

				switch (d) {
				case 0:
					square = GLES1SquareFactory.MakeXY(sector.getX0(),
							sector.getY0(), sector.getX1(), sector.getY1(),
							sector.getZ0(), 0, false);
					break;
				case 2:
					square = GLES1SquareFactory.MakeXY(sector.getX0(),
							sector.getY0(), sector.getX1(), sector.getY1(),
							sector.getZ1(), 0, false);
					break;
				case 1:
					square = GLES1SquareFactory.MakeYZ(sector.getY0(),
							sector.getZ0(), sector.getY1(), sector.getZ1(),
							sector.getX1(), 0, false);
					break;
				case 3:
					square = GLES1SquareFactory.MakeYZ(sector.getY0(),
							sector.getZ0(), sector.getY1(), sector.getZ1(),
							sector.getX0(), 0, false);
					break;
				case 4:
					square = GLES1SquareFactory.MakeXZ(sector.getX0(),
							sector.getZ0(), sector.getX1(), sector.getZ1(),
							sector.getY0(), 0, false);
					break;
				case 5:
					square = GLES1SquareFactory.MakeXZ(sector.getX0(),
							sector.getZ0(), sector.getX1(), sector.getZ1(),
							sector.getY1(), 0, false);
					break;
				}

				if (square == null)
					continue;

				square.setColor(sector.getColor(d).getR() / 255.0f, sector
						.getColor(d).getG() / 255.0f,
						sector.getColor(d).getB() / 255.0f, 1.0f);
				visiblePolys++;
				sector.face[d] = square;
				square.setVisibility(false);

				if (sector.getDoor(d) != null
						&& sector.getDoor(d).getMesh() == null) {

					Mesh mesh = new Mesh();
					mesh.addFace((br.odb.libstrip.IndexedSetFace) square);
					sector.getDoor(d).setMesh(mesh);
					sector.getDoor(d).close();
				}
			}
	}

	// ------------------------------------------------------------------------------------------------------------

	public void calculateLightning() {

		for (Sector s : world) {
			if (s != null && !s.isMaster())
				((GameSector) s).setEmissiveLightningIntensity(0);
		}

		for (AndroidGameActor lightSource : lightEmissors) {
			if (lightSource.candelas > 0)
				lit(lightSource.getCurrentSector(), 5);
		}

	}

	// -----------

	/**
	 * @return the world
	 */
	public GameWorld getWorld() {
		return world;
	}

	/**
	 * 
	 * @param numSectors
	 */
	public void initBuffers(int numSectors) {
		toVisit = new GameSector[numSectors];
		manager.init(visiblePolys);
		manager.flush();
		GameSector sector;

		GLES1Triangle[] returned = null;

		for (Sector baseSector : world) {

			if (baseSector == null)
				continue;

			sector = (GameSector) baseSector;

			if (!sector.isMaster())
				continue;

			// for (int d = 0; d < 6; ++d) {
			//
			// if (sector.decals[d] != null) {
			//
			// returned = sector.decals[d];
			//
			// for (int e = 0; e < sector.decals[d].length; ++e) {
			// sceneGeometryToRender.add(sector.applyToFace(
			// Constants.FACE_FLOOR, returned[e], 1.0f ) );
			// }
			// }
			// }
		}

		ObjMesh detailMesh = world.getDetailMesh();

		if (detailMesh != null) {

			for (Mesh mesh : detailMesh.getMeshes()) {

				if (mesh.getName().equals("basemesh"))
					continue;

				for (int d = 0; d < mesh.getTotalItems(); ++d) {
					this.sceneGeometryToRender.add((GLESIndexedSetFace) mesh
							.getFace(d));
				}
			}

			fixedGeometryManager = new GLESVertexArrayManager();
			fixedGeometryManager.init(sceneGeometryToRender.size());

			for (GLESIndexedSetFace face : sceneGeometryToRender) {

				fixedGeometryManager.pushIntoFrameAsStatic(
						face.getVertexData(), face.getColorData());
			}

			sceneGeometryToRender.clear();
		}
		System.gc();
		Decal decal;
		GameSector s;

		for (Sector baseSector : world) {

			if (baseSector == null)
				continue;

			s = (GameSector) baseSector;

			if (!s.isMaster())
				continue;

			for (int d = 0; d < 6; ++d) {

				if (s.meshWalls[d] != null) {
					if (s.meshWalls[d] instanceof Decal) {
						((Decal) s.meshWalls[d]).preBuffer();

						if (!meshes.contains(s.meshWalls[d]))
							meshes.add(s.meshWalls[d]);

					}
				}

				if (s.getDoor(d) != null
						&& s.getDoor(d).getMesh() instanceof Decal) {

					decal = (Decal) s.getDoor(d).getMesh();
					decal.preBuffer();

					if (!meshes.contains(decal))
						meshes.add(decal);
				}
			}
		}
	}

	/**
	 * 
	 * @param originalSector
	 * @param candelas
	 */
	final private void lit(int originalSector, int candelas) {

		int top = 0;
		lights[1] = candelas;
		lights[0] = originalSector;
		top = 2;
		int sector;
		int factor;
		GameSector sec;
		while (top > 0) {

			factor = lights[top - 1];
			top--;
			sector = lights[top - 1];
			top--;
			sec = (GameSector) this.world.getSector(sector);

			factor = (factor - sec.visibleFaces) / 2;

			sec.addCandelas(factor);

			if (factor <= 0)
				continue;

			for (int c = 0; c < 6; ++c) {
				try {
					if (sec.isOpenAt(c)) {

						lights[top] = sec.getLink(c);
						lights[top + 1] = factor;
						top += 2;

					}
				} catch (InvalidSlotException e) {
					e.printStackTrace();
				}
			}

		}

	}

	// ------------------------------------------------------------------------------------------------------------

	// ------------------------------------------------------------------------------------------------------------
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

		if (GLESVersion == 1) {

			if (height == 0) {
				height = 1;
			}

			glViewport(0, 0, width, height);
			glMatrixMode(GLES10.GL_PROJECTION);
			glLoadIdentity();

			GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f,
					1024.0f);

			glMatrixMode(GLES10.GL_MODELVIEW);
			glLoadIdentity();

		} else {
			onSurfaceChangedGLES20(width, height);
		}
	}

	public void onSurfaceChangedGLES20(int width, int height) {
		GLES20.glViewport(0, 0, width, height);

		float ratio = (float) width / height;
		float xmin, xmax, ymin, ymax;

		ymax = (float) (0.1f * Math.tan(45.0f * Math.PI / 360.0));
		ymin = -ymax;
		xmin = ymin * ratio;
		xmax = ymax * ratio;

		Matrix.frustumM(mProjMatrix, 0, xmin, xmax, ymin, ymax, 0.1f, 1024.0f);
		muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
	}

	// ------------------------------------------------------------------------------------------------------------
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		if (GLESVersion == 1) {

			glShadeModel(GLES10.GL_FLAT);

			glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			glClearDepthf(1.0f);
			glEnable(GLES10.GL_DEPTH_TEST);
			glDepthFunc(GLES10.GL_LEQUAL);
			glHint(GLES10.GL_PERSPECTIVE_CORRECTION_HINT, GLES10.GL_FASTEST);
		} else {
			onSurfaceCreatedGLES20(config);
		}
	}

	public void onSurfaceCreatedGLES20(EGLConfig config) {

		// Set the background frame color
		GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		GLES20.glClearDepthf(1.0f);
		GLES20.glEnable(GLES10.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES10.GL_LEQUAL);
		GLES20.glHint(GLES10.GL_PERSPECTIVE_CORRECTION_HINT, GLES10.GL_FASTEST);

		// initialize the triangle vertex array
		String vertexShaderCode;
		String fragmentShaderCode;
		vertexShaderCode = ((PlayGameActivity) PlayGameActivity.getInstance())
				.readTextFully("vertex.glsl");
		fragmentShaderCode = ((PlayGameActivity) PlayGameActivity.getInstance())
				.readTextFully("fragment.glsl");

		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
				fragmentShaderCode);

		mProgram = GLES20.glCreateProgram(); // create empty OpenGL Program
		GLES20.glAttachShader(mProgram, vertexShader); // add the vertex shader
														// to program
		GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment
															// shader to program
		GLES20.glLinkProgram(mProgram); // creates OpenGL program executables

		// get handle to the vertex shader's vPosition member
		maPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		colorHandle = GLES20.glGetAttribLocation(mProgram, "a_color");
	}

	// -------------------------------------------------------------------------------------------------
	@Override
	public void onDrawFrame(GL10 gl) {

		if (GLESVersion == 1) {

			if (!attached)
				return;

			++frame;

			// ////////////////////////////
			if (checkVisibilityFlagStatus() && shouldCheckForBailingOut)
				return;

			// ////////////////////////////
			glClear(GLES10.GL_DEPTH_BUFFER_BIT);

			setCamera(gl);

			// glEnable(GLES10.GL_DEPTH_TEST);
			glFrontFace(GLES10.GL_CW);
			glEnableClientState(GLES10.GL_VERTEX_ARRAY);
			glEnableClientState(GLES10.GL_COLOR_ARRAY);

			if (fixedGeometryManager != null) {

				fixedGeometryManager.flush();
				fixedGeometryManager.draw();
			}

			for (GameActor actor : actors) {

				if (actor == cameraActor)
					continue;

				glPushMatrix();
				glTranslatef(actor.getPosition().x, actor.getPosition().y,
						actor.getPosition().z);
				glRotatef(360.0f - actor.getAngleXZ(), 0.0f, 1.0f, 0.0f);

				drawObjMesh(gl, actor.getMainMesh());

				glPopMatrix();
			}

			for (GLESIndexedSetFace face : sceneGeometryToRender) {

				face.draw();
			}

			for (Mesh mesh : meshes) {
				drawMesh(gl, mesh);
			}

			manager.flush();
			manager.draw();

			glDisableClientState(GLES10.GL_COLOR_ARRAY);
			glDisableClientState(GLES10.GL_VERTEX_ARRAY);
		} else {

			renderSceneGLES20();
		}

	}

	private boolean checkVisibilityFlagStatus() {

		if (shouldResetView) {
			shouldResetView = false;
			manager.onFrameRenderingFinished();
			setView();
			return true;
		}
		return false;
	}

	/**
	 * @param gl
	 * @param objMesh
	 */
	private void drawObjMesh(GL10 gl, ObjMesh objMesh) {

		if (!objMesh.isVisible())
			return;

		for (Mesh mesh : objMesh.getMeshes()) {
			drawMesh(gl, mesh);
		}
	}

	/**
	 * @param gl
	 * @param mesh
	 */
	private void drawMesh(GL10 notUsed, Mesh mesh) {

		if (!mesh.isVisible())
			return;

		if (mesh.manager != null) {
			mesh.manager.flush();
			mesh.manager.draw();
		} else {
			for (IndexedSetFace face : mesh.faces) {
				((GLESIndexedSetFace) face).draw();
			}
		}
	}

	/**
	 * @param gl
	 * @param mesh
	 */
	private void drawMeshGLES2(Mesh mesh) {

		if (!mesh.isVisible())
			return;

		if (mesh.manager != null) {
			mesh.manager.flush();
			((GLESVertexArrayManager) mesh.manager).drawGLES2(maPositionHandle,
					colorHandle);
		} else {
			for (IndexedSetFace face : mesh.faces) {
				((GLESIndexedSetFace) face).draw();
			}
		}
	}

	// ------------------------------------------------------------------------------------------------------------
	public void setAngle(float Angle) {
		angle = Angle;
		this.needsToResetView(true);
	}

	private void setCamera(GL10 gl) {
		glLoadIdentity();
		glRotatef(-7.5f + accel.x, 1, 0, 0);
		glRotatef(0.125f + accel.y, 0, 1, 0);
		glRotatef(angle, 0, 1, 0);

		glTranslatef(-this.cameraActor.getPosition().x,
				-this.cameraActor.getPosition().y,
				-this.cameraActor.getPosition().z);
	}

	private void setCamera() {
		mVMatrix = new float[] { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0,
				1, };

		Matrix.rotateM(mVMatrix, 0, -7.5f + accel.x, 1.0f, 0, 0);
		Matrix.rotateM(mVMatrix, 0, 0.125f + accel.y, 0, 1.0f, 0);

		Matrix.rotateM(mVMatrix, 0, angle, 0, 1.0f, 0);
		Matrix.translateM(mVMatrix, 0, -this.cameraActor.getPosition().x,
				-this.cameraActor.getPosition().y,
				-this.cameraActor.getPosition().z);

		Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mMMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);

		// Apply a ModelView Projection transformation
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);

		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
		GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mMVPMatrix, 0);

	}

	private void renderSceneGLES20() {

		// Add program to OpenGL environment
		GLES20.glUseProgram(mProgram);

		GLES20.glEnable(GL10.GL_DEPTH_TEST);

		if (!attached)
			return;

		++frame;

		// ////////////////////////////
		if (checkVisibilityFlagStatus() && shouldCheckForBailingOut)
			return;

		// ////////////////////////////

		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT);

		setCamera();

		if (fixedGeometryManager != null) {

			fixedGeometryManager.flush();
			fixedGeometryManager.drawGLES2(maPositionHandle, colorHandle);
		}

		for (GLESIndexedSetFace face : sceneGeometryToRender) {

			face.drawGLES2(maPositionHandle, colorHandle);
		}

		for (Mesh mesh : meshes) {
			drawMeshGLES2(mesh);
		}

		manager.flush();
		manager.drawGLES2(maPositionHandle, colorHandle);
	}

	// ------------------------------------------------------------------------------------------------------------a

	public void setCurrentCamera(AndroidGameActor cameraActor) {
		this.cameraActor = cameraActor;
	}

	public void setView() {

		if (cameraActor.getCurrentSector() != -1) {

			hideAllMeshes();
			direction = cameraActor.getDirection();
			parcel = ((cameraActor.getDiscreetAngle()) / 4.0f) - direction;
			back = Utils.getOppositeDirection(direction);
			computeVisibilityFromSectorAndDirection(
					cameraActor.getCurrentSector(), direction);
		}
	}

	private void hideAllMeshes() {

		for (Mesh mesh : meshes) {
			mesh.setVisibility(false);
		}
	}

	/**
	 * @param world
	 *            the world to set
	 */
	public void setWorld(GameWorld world) {
		this.world = world;
	}

	// ------------------------------------------------------------------------------------------------------------
	@Override
	public String toString() {
		return displayList;
	}

	final private void computeVisibilityFromSectorAndDirection(int sec,
			int currentDirection) {
		top = 0;
		int offset;
		toVisit[top] = (GameSector) world.getSector(sec);
		Vec3 currentPos = toVisit[top].relPos;
		toVisit[top].relPos.set(currentPos);
		float x = currentPos.x;
		float y = currentPos.y;
		Vec3 currentRelative;
		GameSector s;
		GameSector n;
		GLES1Square square;
		++top;

		while (top > 0) {

			if (shouldResetView && shouldCheckForBailingOut)
				return;

			--top;
			s = toVisit[top];

			if (s.frame == frame) {
				s.relPos.x = s.relPos.y = s.relPos.z = 0;
				continue;
			}

			s.frame = frame;
			currentPos = s.relPos;
			x = currentPos.x;
			y = currentPos.y;

			for (int c = 0; c < 6; ++c) {

				if (c != back) {

					if ((s.visibleFaces == 0 && s.doorCount == 0)
							|| s.isOpenAt(c)) {

						if (c < 4) {

							offset = (c - currentDirection);
							// offset = offset % 4;

							if (offset > 4)
								offset -= 4;

							if (offset < 0)
								offset += 4;

							switch (offset) {

							case 4:
								if (y > 0)
									continue;
								break;

							case 5:
								if (y < 0)
									continue;
								break;

							case 3:
								if (x > 0)
									continue;

								break;

							case 1:
								if (x < 0)
									continue;

								break;
							}
						} else {
							offset = c;
						}

						if (top < toVisit.length) {
							n = s.cachedNeighBours[c];
							if (n != null) {

								toVisit[top] = n;
								currentRelative = relativePositions[offset];
								n.relPos.x = (currentPos.x + currentRelative.x);
								n.relPos.y = (currentPos.y + currentRelative.y);
								n.relPos.z = (currentPos.z + currentRelative.z);
								++top;
							}
						}
					} else {
						if (s.meshWalls[c] == null) {

							if (s.getDoor(c) != null) {

								if (!s.getDoor(c).getMesh().isVisible())
									s.getDoor(c).getMesh().setVisibility(true);

							} else {
								// if ( c == 4 ) {

								square = s.face[c];
								manager.pushIntoFrameAsStatic(
										square.verticesBits, square.colorBits);
								// }
							}
						} else {
							s.meshWalls[c].setVisibility(true);
						}
					}
				}
			}
		}
	}

	public synchronized void needsToResetView(boolean fastReset) {
		shouldResetView = true;
		shouldCheckForBailingOut = fastReset;
	}

	public synchronized void NoNeedsToSetView() {
		shouldResetView = false;
	}

	public void addToFixedGeometryToScreen(GLES1Triangle[] graphic) {
		for (int c = 0; c < graphic.length; ++c) {

			graphic[c].flatten(-1.0f);
			graphic[c].flushToGLES();
			this.fixedScreenShapesToRender.add(graphic[c]);
		}
	}

	public Actor3D getCameraActor() {

		return cameraActor;
	}

	public synchronized void attach() {

		attached = true;
	}

	public void detach() {

		attached = false;
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (fixedGeometryManager != null)
			fixedGeometryManager.reinit();

		actors.clear();
		angle = 0;
		frame = 0;
		manager.reinit();
		sceneGeometryToRender.clear();
		screenShapesToRender.clear();
		top = 0;
		visiblePolys = 0;
		world = null;
		lightEmissors.clear();
		meshes.clear();
		cameraActor = null;
		fixedGeometryManager = null;
	}

	public void clearScreenGeometry() {
		screenShapesToRender.clear();
	}

	public void clearActors() {
		actors.clear();
		cameraActor = null;
	}

	public void addToMovingGeometryToScreen(GLES1Triangle[] graphic) {

		for (int c = 0; c < graphic.length; ++c) {

			graphic[c].flatten(-1.0f);
			graphic[c].flushToGLES();
			this.addGeometryToScreen(graphic[c]);
		}
	}
}
