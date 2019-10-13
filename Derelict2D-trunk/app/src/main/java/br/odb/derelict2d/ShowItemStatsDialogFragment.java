package br.odb.derelict2d;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;

import br.odb.gamelib.android.AndroidUtils;
import br.odb.gamelib.android.GameView;

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
		wvStats = view.findViewById(R.id.wvStats);
		gvItemView = view.findViewById(R.id.gvItemView);
		final Bundle args = getArguments();
		final String title = args.getString("name").substring(0, 1).toUpperCase() + args.getString("name").substring(1);
		getDialog().setTitle(title.replace('-', ' '));


		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				updateDescription(title, args.getString("desc"), args);
			}
		}, 100);

		return view;
	}

	private void updateDescription(String title, String desc, Bundle args) {

		AndroidUtils.initImage(gvItemView, args.getString("image"), ((Derelict2DApplication) getActivity()
				.getApplication()).getAssetManager());

		desc = desc.replaceAll("\n", "<br/>");
		wvStats.getSettings().setJavaScriptEnabled(false);
		wvStats.loadDataWithBaseURL(null, "<html><body bgcolor = '#0D0' >"
				+ desc + "</body></html>", "text/html", "utf-8", null);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnCloseItemStat) {
			dismiss();
		}
	}
}
