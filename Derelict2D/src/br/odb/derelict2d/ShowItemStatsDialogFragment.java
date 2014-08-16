package br.odb.derelict2d;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import br.odb.gamelib.android.GameView;
import br.odb.gamerendering.rendering.AssetManager;
import br.odb.gamerendering.rendering.DisplayList;
import br.odb.gamerendering.rendering.RenderingNode;
import br.odb.gamerendering.rendering.SVGRenderingNode;
import br.odb.libsvg.SVGGraphic;

public class ShowItemStatsDialogFragment extends DialogFragment implements
		OnClickListener {

	private WebView wvStats;
	private GameView gvItemView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.show_item_stats_layout,
				container, true);
		view.findViewById(R.id.btnCloseItemStat).setOnClickListener(this);
		wvStats = (WebView) view.findViewById(R.id.wvStats);
		gvItemView = (GameView) view.findViewById(R.id.gvItemView);
		Bundle args = getArguments();
		initImage(args.getString("name"));
		updateDescription(args.getString("desc"));

		return view;
	}

	private void updateDescription(String desc) {
		desc = desc.replaceAll("\n", "<br/>");
		wvStats.getSettings().setJavaScriptEnabled(false);
		wvStats.loadDataWithBaseURL(null, "<html><body bgcolor = '#0D0' >"
				+ desc + "</body></html>", "text/html", "utf-8", null);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCloseItemStat:
			dismiss();
			break;
		}
	}

	void initImage(String name) {

		DisplayList dl = new DisplayList("dl");
		AssetManager resManager = ((Derelict2DApplication) getActivity()
				.getApplication()).getAssetManager();

		SVGGraphic graphic = resManager.getGraphics(name);
		graphic = graphic.scaleTo( 200, 200 );
		SVGRenderingNode node = new SVGRenderingNode(graphic, "title");

		dl.setItems(new RenderingNode[] { node });

		gvItemView.setRenderingContent(dl);

		gvItemView.postInvalidate();
	}
}
