package br.odb.gamerendering.rendering;

public class GameRenderer {
	//TODO: render subnodes recursively
	private DisplayList currentDisplayList;
	private int currentItem;
	private int jobSize;

	private RenderingContext currentRenderingContext;

	public void startRendering(DisplayList displayList) {
		this.currentDisplayList = displayList;
		this.jobSize = displayList.length();
		this.currentItem = 0;
	}

	public boolean hasJobs() {
		if (currentDisplayList == null)
			return false;
		else {
			return currentItem < jobSize;
		}
	}

	public void renderNext() {
		renderNode(currentDisplayList.items[currentItem]);
		++currentItem;
	}

	public void resetRenderingContext() {
		currentItem = 0;
		jobSize = 0;
		currentDisplayList = null;
	}

	public void renderNode(RenderingNode node) {
		
		if ( !node.isVisible() ) {
			return;
		}

		float previousAlpha = currentRenderingContext.getCurrentAlpha();

		currentRenderingContext.currentOrigin.addTo(node.translate);
		currentRenderingContext.setCurrentAlpha(node.alpha * previousAlpha);
		node.render(currentRenderingContext);
		currentRenderingContext.currentOrigin.addTo(node.translate.negated());

		currentRenderingContext.setCurrentAlpha(previousAlpha);
	}

	public void setCurrentRenderingContext(RenderingContext renderingContext) {

		this.currentRenderingContext = renderingContext;
	}
}
