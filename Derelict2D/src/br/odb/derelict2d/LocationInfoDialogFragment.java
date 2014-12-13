package br.odb.derelict2d;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import br.odb.derelict.core.DerelictGame;
import br.odb.gamelib.android.AndroidUtils;
import br.odb.gamelib.android.GameView;

public class LocationInfoDialogFragment extends DialogFragment {

	private WebView wvStats;
	DerelictGame game;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_location_dialog,
				container, true);
		
		wvStats = (WebView) view.findViewById(R.id.wvDescription);
		
		
		game = ((Derelict2DApplication) this.getActivity().getApplication()).game;
		
		String locationName = game.hero.getLocation().getName();
		String title = locationName.substring(0, 1).toUpperCase() + locationName.substring(1);
		
		getDialog().setTitle( title.replace( '-', ' ' ) );
		
		String desc = game.getTextOutput().replaceAll("\n", "<br/>");

		String newText = "<html><body bgcolor = '#0D0' >" + desc
				+ "</body></html>";

		
		wvStats.loadDataWithBaseURL(null, newText, "text/html",
					"utf-8", null);
		
	
		((ExploreStationActivity) getActivity()).say( title + ": " + game.getTextOutput() );
		
		return view;
	}
	
}
