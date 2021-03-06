package br.odb.derelict2d;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import br.odb.derelict.core.DerelictGame;
import br.odb.derelict2d.game.GameLevel;
import br.odb.gameapp.GameUpdateDelegate;
import br.odb.gamelib.android.AndroidUtils;
import br.odb.gamelib.android.GameView;
import br.odb.gamerendering.rendering.AssetManager;
import br.odb.gameworld.Item;
import br.odb.gameworld.Location;
import br.odb.gameworld.exceptions.InvalidLocationException;
import br.odb.gameworld.exceptions.InvalidSlotException;
import br.odb.libsvg.SVGGraphic;
import br.odb.utils.Direction;
import br.odb.utils.Utils;

public class ExploreStationFragment extends Fragment implements
		GameUpdateDelegate, OnClickListener, OnItemSelectedListener,
		OnCheckedChangeListener {

	EditText edtOutput;
	EditText edtEntry;

	ExploreStationView gameView;
	private Spinner spnDirections;
	private GameLevel currentLevel;
	DerelictGame game;
	private MediaPlayer fiveSteps;
	private MediaPlayer ding;
	private AssetManager resManager;
	CheckBox chkShowPlaceNames;

	// Playing low here:
	final HashMap<String, String> locationPrettyNames = new HashMap<String, String>();

	SVGGraphic stationGraphics;
	private GameView gvMove;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View toReturn = inflater.inflate(R.layout.fragment_explore_station,
				container, false);

		gameView = (ExploreStationView) toReturn.findViewById(R.id.overviewMap);
		spnDirections = (Spinner) toReturn.findViewById(R.id.spnDirection);
		spnDirections.setOnItemSelectedListener(this);
		gvMove = (GameView) toReturn.findViewById(R.id.gvMove);
		chkShowPlaceNames = (CheckBox) toReturn
				.findViewById(R.id.chkShowPlaceNames);
		gvMove.setOnClickListener(this);

		chkShowPlaceNames.setOnCheckedChangeListener(this);

		toReturn.post(new Runnable() {
			@Override
			public void run() {

				Thread.yield();

				AndroidUtils
						.initImage(gvMove, "icon-move",
								((Derelict2DApplication) getActivity()
										.getApplication()).getAssetManager());
			}
		});

		return toReturn;
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);

		ExploreStationActivity explore = ((ExploreStationActivity) activity);

		game = ((Derelict2DApplication) activity.getApplication()).game;
		explore.addUpdateListener(this);

		String hasSound = getActivity().getIntent().getExtras()
				.getString("hasSound");

		if (hasSound != null && hasSound.equals("y")) {

			fiveSteps = MediaPlayer.create(explore, R.raw.fivesteps);
			ding = MediaPlayer.create(explore, R.raw.ding);
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.gvMove:
			Direction d;

			Location l = game.hero.getLocation();
			try {

				String locationName = (String) spnDirections.getSelectedItem();

				locationName = locationPrettyNames.get(locationName);

				d = l.getConnectionDirectionForLocation(game.station
						.getLocation(locationName));
				game.sendData("move " + d);
				game.station.update( 10000 );

				if (d == Direction.CEILING || d == Direction.FLOOR) {
					if (ding != null) {
						ding.start();
					}
				} else {
					if (fiveSteps != null) {
						fiveSteps.start();
					}
				}
				
				((ExploreStationActivity) getActivity()).say(locationName);
			} catch (InvalidSlotException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}

	@Override
	public void update() {

		ExploreStationActivity explore = (ExploreStationActivity) getActivity();

		resManager = explore.resManager;
		currentLevel = explore.currentLevel;

		if (gameView != null && currentLevel != null && resManager != null) {

			gameView.setSnapshot(game, resManager,
					chkShowPlaceNames.isChecked());

			ArrayList<Item> tmp = new ArrayList<Item>();

			for (Item i : game.getCollectableItems()) {
				tmp.add(0, i);
			}

			Utils.reverseArray(game.getCollectableItems());

			String[] locations = game.getConnectionNames();

			String newString;

			for (int c = 0; c < locations.length; ++c) {
				newString =  capitalizeString( locations[c].replace('-', ' ') );
				locationPrettyNames.put(newString, locations[c]);
				locations[c] = newString;
			}

			ArrayAdapter<String> adapter;
			adapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_spinner_item, locations);

			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			spnDirections.setAdapter(adapter);

			spnDirections.setSelection(0);

			gameView.setBackgroundColor(Color.argb(255,
					(int) Utils.clamp(game.station.hullTemperature, 0, 255), 0,
					64));
			gameView.update(100);
		}

	}

	//http://stackoverflow.com/questions/1892765/capitalize-first-char-of-each-word-in-a-string-java served as a basis...
	public static String capitalizeString(String string) {
		
		StringBuilder sb = new StringBuilder();
		String[] tokens = string.split( "[ ]+" );
		
		for ( String token : tokens ) {
			sb.append( token.substring(0, 1).toUpperCase() + token.substring(1) );
			sb.append( " " );
		}

		return sb.toString().trim();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View v, int arg2, long arg3) {

		try {
			String locationName = (String) spnDirections.getSelectedItem();
			game.hero.direction = game.hero.getLocation()
					.getConnectionDirectionForLocation(
							game.station.getLocation(locationName));

			((ExploreStationActivity) getActivity()).say(locationName);
		} catch (Exception e) {
			// We simply bail out. It's not a big deal...
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		update();
	}
}
