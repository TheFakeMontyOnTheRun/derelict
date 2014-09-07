package br.odb.derelict2d;

import java.util.ArrayList;

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
import android.widget.Button;
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
import br.odb.libsvg.SVGGraphic;
import br.odb.utils.Direction;
import br.odb.utils.Utils;


public class ExploreStationFragment extends Fragment implements
		GameUpdateDelegate, OnClickListener, OnItemSelectedListener {

	EditText edtOutput;
	EditText edtEntry;
	Button btnSend;
	ExploreStationView gameView;
	private Spinner spnDirections;
	private GameLevel currentLevel;
	DerelictGame game;
	private MediaPlayer fiveSteps;
	private MediaPlayer ding;
	private AssetManager resManager;
	
	
	SVGGraphic stationGraphics;
	private GameView gvMove;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View toReturn = inflater.inflate(R.layout.fragment_explore_station,
				container, false);

		btnSend = (Button) toReturn.findViewById(R.id.btnSend);
		gameView = (ExploreStationView) toReturn.findViewById(R.id.overviewMap);
		spnDirections = (Spinner) toReturn.findViewById(R.id.spnDirection);
		spnDirections.setOnItemSelectedListener(this);
		gvMove = (GameView) toReturn.findViewById(R.id.gvMove);		
		gvMove.setOnClickListener( this );
		AndroidUtils.initImage(gvMove, "icon-move", ((Derelict2DApplication) getActivity() .getApplication()).getAssetManager());

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
				
			d = (Direction) spnDirections.getSelectedItem();
			Location l = game.hero.getLocation();
			game.sendData("move " + spnDirections.getSelectedItem());
			
			//not good!
			if ( l != game.hero.getLocation() && game.station.getAstronaut().getLocation().getConnections()[ d.ordinal() ] != null ) {
			
				if (d != Direction.CEILING && d != Direction.FLOOR) {
					if (fiveSteps != null) {

						fiveSteps.start();
					}
				} else {

					if (ding != null) {

						ding.start();
					}
				}

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
						
			gameView.setSnapshot(game, resManager);
			
			ArrayList< Item > tmp = new ArrayList< Item >();
			
			
			for ( Item i : game.getCollectableItems() ) {
				tmp.add( 0, i );
			}
			
			Utils.reverseArray( game.getCollectableItems() );
			
			spnDirections.setAdapter(new ArrayAdapter<Direction>(getActivity(),
					android.R.layout.simple_spinner_item, Direction.values()));


			spnDirections.setSelection(game.hero.direction.ordinal());
	
			gameView.setBackgroundColor( Color.argb( 255, ( int )Utils.clamp( game.station.hullTemperature, 0, 255 ), 0, 64 ) );
			gameView.update( 100 );
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View v, int arg2,
			long arg3) {
			
			game.hero.direction = (Direction) spnDirections.getSelectedItem();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
}
