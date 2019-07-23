package br.odb.gamelib.android;

import android.graphics.Bitmap;

import br.odb.gamerendering.rendering.RasterImage;


/**
 * @author monty
 */
public class AndroidRasterImage extends RasterImage {

    public AndroidRasterImage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    final Bitmap bitmap;

    @Override
    public int getHeight() {

        return bitmap.getHeight();
    }

    @Override
    public int getWidth() {

        return bitmap.getWidth();
    }
}
