package br.odb.derelict2d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;

import br.odb.derelict.core.DerelictGame;
import br.odb.derelict2d.graphics2d.DerelictGraphicsAdapter;
import br.odb.gamelib.android.PointerNodeSelectableScrollableView;
import br.odb.gamerendering.rendering.AssetManager;
import br.odb.gamerendering.rendering.DisplayList;

/**
 * @author monty Here is the view - no model allowed!
 */
public class ExploreStationView extends PointerNodeSelectableScrollableView {

    private final DerelictGraphicsAdapter adapter = new DerelictGraphicsAdapter();

    public ExploreStationView(Context context) {
        super(context);
    }

    public ExploreStationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSnapshot(DerelictGame game, AssetManager resManager, boolean showText) {

        DisplayList node = adapter.parse(game, resManager, showText);

        super.setSelectedItem(node
                .getElementById("SVGRenderingNode_heroGraphic"));
        super.shouldFollowTarget = true;

        this.setRenderingContent(node);
    }


    @Override
    public void doDraw(Canvas canvas) {

        Paint paint = new Paint();

        int gridSize = 1024;

        paint.setARGB(128, 0, 255, 0);
        paint.setStyle(Style.STROKE);

        for (int c = 0; c < gridSize; c += 25) {

            canvas.drawLine(c + accScroll.x, accScroll.y, c + accScroll.x, accScroll.y + gridSize, paint);
        }

        for (int c = 0; c < gridSize; c += 25) {

            canvas.drawLine(accScroll.x, c + accScroll.y, gridSize + accScroll.x, c + accScroll.y, paint);
        }

        canvas.drawLine(accScroll.x, accScroll.y, gridSize + accScroll.x, accScroll.y, paint);
        canvas.drawLine(accScroll.x, gridSize + accScroll.y, gridSize + accScroll.x, gridSize + accScroll.y, paint);
        canvas.drawLine(gridSize + accScroll.x, accScroll.y, gridSize + accScroll.x, gridSize + accScroll.y, paint);
        canvas.drawLine(accScroll.x, accScroll.y, accScroll.x, gridSize + accScroll.y, paint);


        super.doDraw(canvas);
    }
}
