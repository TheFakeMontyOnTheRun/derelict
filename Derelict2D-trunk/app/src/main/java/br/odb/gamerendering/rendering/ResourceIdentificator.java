package br.odb.gamerendering.rendering;

/**
 * @author monty
 * 
 */
public class ResourceIdentificator {

	public static final ResourceIdentificator BLANK = new ResourceIdentificator(-1);
	
	private final int resId;
	
	private ResourceIdentificator(int i) {
		this.resId = i;
	}
}
