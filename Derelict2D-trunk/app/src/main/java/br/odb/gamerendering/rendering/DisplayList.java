package br.odb.gamerendering.rendering;

public class DisplayList extends RenderingNode {
	RenderingNode[] items;
	
	public RenderingNode[] getItems() {
		return items;
	}

	public DisplayList(String id) {
		super(id);
	}
	
	@Override
	public void update(long ms) {
	
		super.update( ms );
		
		for ( RenderingNode rn : items ) {
			rn.update( ms );
		}
	}

	public int length() {

		if (items != null)
			return items.length;
		else
			return 0;
	}

	public void setItems(RenderingNode[] items) {
		this.items = items;
	}

	public RenderingNode getElementById(String id) {

		for (RenderingNode rn : items) {
			if (rn.getId().equals(id)) {
				return rn;
			}
		}

		return null;
	}

	@Override
	public void render(RenderingContext rc) {

		if (items != null) {
			for (RenderingNode rn : items) {
				rn.render(rc);
			}
		}
	}
}
