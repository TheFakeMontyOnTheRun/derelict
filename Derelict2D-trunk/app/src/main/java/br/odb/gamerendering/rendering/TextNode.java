package br.odb.gamerendering.rendering;

import br.odb.gameutils.Color;

public class TextNode extends RenderingNode {

	private final String content;
	private final Color color;
	private final int fontSize;

	public TextNode(String id, String content, Color color, int fontSize) {
		super("textnode_" + id + "_" + content);
		this.fontSize = fontSize;
		this.color = color;
		this.content = content;
	}

	@Override
	public void render(RenderingContext rc) {
		rc.drawText(this.translate, content, this.color, fontSize);
	}
}
