package br.odb.gamerendering.rendering;

import br.odb.gameutils.Rect;


public class Sprite extends RenderingNode {

    private int currentFrame = 0;
    private RasterImage currentImage;
    private final RasterImage[] images;
    private int rotation;
    public boolean insivisibleOnStop = false;
    
    public Sprite(RasterImage image) {
        super("sprite_" + image.resId);
        images = new RasterImage[ 1 ];
        images[ 0 ] = image;
        this.currentImage = image;
        bounds.set( new Rect( 0.0f, 0.0f, image.getWidth(), image.getHeight() ) );
    }

    public void setFrame(int frame) {
        this.currentFrame = frame;
    }

    private void nextFrame() {
    	
    	if ( getFrameCount() < 2 ) {
    		return;
    	}
    	
        if (currentFrame != getFrameCount() - 1) {
            currentFrame++;
            this.currentImage = images[ currentFrame ];            
        } else {
        	visible = false;
            currentFrame = 0;
        }
    }

    public void previousFrame() {
    	
    	if ( getFrameCount() < 2 ) {
    		return;
    	}
    	
        if (currentFrame != 0) {
            currentFrame--;
            this.currentImage = images[ currentFrame ];            
        } else {
        	visible = false;
            currentFrame = getFrameCount() - 1;
        }
    }

    @Override
    public void update(long ms) {
    
    	super.update(ms);
    	
    	nextFrame();
    }
    
    private int getFrameCount() {
        return images.length;
    }

    @Override
    public void render(RenderingContext currentRenderingContext) {

        if (!visible) {
            return;
        }

        currentRenderingContext.saveClipRect();        
        currentRenderingContext.drawBitmap( currentImage, translate, scale, rotation );
        currentRenderingContext.restoreClipRect();
    }
}
