package br.odb.gamelib.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import br.odb.gamerendering.rendering.DisplayList;
import br.odb.gamerendering.rendering.GameRenderer;
import br.odb.gamerendering.rendering.RenderingNode;
import br.odb.gamerendering.rendering.SolidSquareRenderingNode;
import br.odb.gameutils.Color;
import br.odb.gameutils.Rect;
import br.odb.gameutils.Updatable;

public class GameView extends View implements Updatable {

	private final Paint paint = new Paint();
	public Updater updater;
	private GameRenderer gameRenderer;
	private AndroidCanvasRenderingContext renderingContext;
	volatile private DisplayList renderingNode;
	private long renderingBudget;
	private RenderingNode defaultRenderingNode;

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	public GameView(Context context) {
		super(context);

		init();
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			return performClick();
		}
		return true;
	}

	public void update(long ms) {
		if (renderingNode != null) {

			renderingNode.update(ms);
		}
	}

	private void init() {
		this.requestFocus();
		this.setFocusableInTouchMode(true);

		paint.setAntiAlias(true);
		renderingBudget = 100;
		renderingContext = new AndroidCanvasRenderingContext();
		gameRenderer = new GameRenderer();
		gameRenderer.setCurrentRenderingContext(renderingContext);

		DisplayList dl = new DisplayList("dl");
		dl.setItems(new RenderingNode[]{new SolidSquareRenderingNode(new Rect(10, 10, 100, 100), new Color(255, 0, 0)),
				new SolidSquareRenderingNode(new Rect(110, 10, 100, 100), new Color(0, 255, 0)),
				new SolidSquareRenderingNode(new Rect(210, 10, 100, 100), new Color(0, 0, 255))});
		defaultRenderingNode = dl;


		updater = new Updater(this);
		updater.setRunning(true);
		Thread updateThread = new Thread(updater);
		updateThread.start();

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		doDraw(canvas);
	}

	public RenderingNode getRenderingContent() {
		return renderingNode;
	}

	public void setRenderingContent(DisplayList displayList) {
		this.renderingNode = displayList;
		this.renderingContext.currentOrigin.set(0.0f, 0.0f);
	}

	void doDraw(Canvas canvas) {

		long t0 = System.currentTimeMillis();
		long tn = t0;

		renderingContext.prepareWithCanvasAndPaint(canvas, paint);

		if (renderingNode == null) {
			renderDefaultEmptyScreen();
			return;
		}

		renderingContext.currentOrigin.addTo(renderingNode.translate);
		gameRenderer.startRendering(renderingNode);

		while (gameRenderer.hasJobs() && ((tn - t0) < this.renderingBudget)) {
			gameRenderer.renderNext();
			tn = System.currentTimeMillis();
		}

		gameRenderer.resetRenderingContext();
		renderingContext.currentOrigin.addTo(renderingNode.translate.negated());
	}

	private void renderDefaultEmptyScreen() {
		if (defaultRenderingNode == null) {

			defaultRenderingNode = new SolidSquareRenderingNode(new Rect(100,
					100, 200, 200), new Color(255, 255, 0));
		}
		gameRenderer.renderNode(defaultRenderingNode);
	}

	void setAntiAliasing(boolean b) {

		boolean previous;

		if (renderingContext.paint != null) {

			previous = renderingContext.paint.isAntiAlias();

			if (b != previous) {
				renderingContext.setAntiAlias(b);
				postInvalidate();
			}
		}
	}

	public class Updater implements Runnable {

		final GameView view;
		final long latency = 100;
		boolean stillRunning;

		Updater(GameView view) {
			this.stillRunning = false;
			this.view = view;
		}

		public void setRunning(boolean running) {
			stillRunning = running;
		}

		@Override
		public void run() {
			while (stillRunning) {
				try {
					Thread.sleep(latency);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				update(latency);
				postInvalidate();

			}
		}
	}
}
