package br.odb.gamerendering.rendering;

/**
 * @author monty
 * 
 */
public abstract class RasterImage {
	
	final ResourceIdentificator resId = ResourceIdentificator.BLANK;

	public abstract int getHeight();

	public abstract int getWidth();
}
