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
import br.odb.gameworld.exceptions.InvalidLocationException;
import br.odb.gameworld.exceptions.InvalidSlotException;
import br.odb.libsvg.SVGGraphic;
import br.odb.utils.Direction;
import br.odb.utils.Utils;


public class ExploreStationFragment extends Fragment implements
		GameUpdateDelegate, OnClickListener, OnItemSelectedListener {

//	Spinner spnItems;
	EditText edtOutput;
	EditText edtEntry;
	Button btnSend;
//	Button btnPick;
	ExploreStationView gameView;
	private Spinner spnLocations;
	private Spinner spnDirections;
	private GameLevel currentLevel;
	DerelictGame game;
	private MediaPlayer fiveSteps;
	private MediaPlayer ding;
	private AssetManager resManager;
	
	
	SVGGraphic stationGraphics;
//	Derelict3DView view3D;
	private GameView gvMove;
	private GameView gvTurn;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View toReturn = inflater.inflate(R.layout.fragment_explore_station,
				container, false);

		btnSend = (Button) toReturn.findViewById(R.id.btnSend);
		gameView = (ExploreStationView) toReturn.findViewById(R.id.overviewMap);
//		spnItems = (Spinner) toReturn.findViewById(R.id.spnObjectsToPick);
		spnLocations = (Spinner) toReturn.findViewById(R.id.spnLocations);
		spnDirections = (Spinner) toReturn.findViewById(R.id.spnDirection);
		

//		view3D = (Derelict3DView) toReturn.findViewById( R.id.view3D );
		
		
		spnDirections.setOnItemSelectedListener(this);
		
//		btnPick = (Button) toReturn.findViewById(R.id.btnPick);
//		btnPick.setOnClickListener(this);
		toReturn.findViewById(R.id.btnGo).setOnClickListener(this);
		
		gvMove = (GameView) toReturn.findViewById(R.id.gvMove);		
		gvMove.setOnClickListener( this );
		AndroidUtils.initImage(gvMove, "icon-move", ((Derelict2DApplication) getActivity() .getApplication()).getAssetManager());

		gvTurn = (GameView) toReturn.findViewById(R.id.gvTurn);		
		gvTurn.setOnClickListener( this );
		AndroidUtils.initImage(gvTurn, "icon-turn", ((Derelict2DApplication) getActivity() .getApplication()).getAssetManager());
		

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
//		case R.id.btnPick:
//			game.sendData("pick "
//					+ ((Item) spnItems.getSelectedItem()).getName());
//			break;

		case R.id.btnGo:
			
//			if ( view3D != null ) {
//				
//				stationGraphics = null;
//				view3D.clearScene();
//			}
//			
			Direction d;
			Location l;

			try {
				l = game.station.getLocation((String) spnLocations
						.getSelectedItem());
				d = game.station.getAstronaut().getLocation()
						.getConnectionDirectionForLocation(l);
				if (game.station.canMove(game.station.getAstronaut(),
						(String) spnLocations.getSelectedItem())) {

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
			} catch (InvalidLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidSlotException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			game.sendData("move " + spnLocations.getSelectedItem());
			break;
		}
	}

	@Override
	public void update() {

		ExploreStationActivity explore = (ExploreStationActivity) getActivity();

		resManager = explore.resManager;
		currentLevel = explore.currentLevel;

		if (gameView != null && currentLevel != null && resManager != null) {

//			if ( stationGraphics == null && view3D != null ) {
//				
//				stationGraphics = resManager.getGraphics("floor1");		
//				view3D.addScene( stationGraphics, game.hero.getLocation().getName() );
//			}

			
			gameView.setSnapshot(game, resManager);
			
			ArrayList< Item > tmp = new ArrayList< Item >();
			
			
			for ( Item i : game.getCollectableItems() ) {
				tmp.add( 0, i );
			}
			
			Item[] items = tmp.toArray( new Item[ tmp.size() ] );
			
//			spnItems.setAdapter(new ArrayAdapter<Item>(getActivity(),
//					android.R.layout.simple_spinner_item, 
//					items ) );
//			
			Utils.reverseArray( game.getCollectableItems() );
			
			spnLocations.setAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_spinner_item, game
							.getConnectionNames()));
			
			

			spnDirections.setAdapter(new ArrayAdapter<Direction>(getActivity(),
					android.R.layout.simple_spinner_item, Direction.values()));


			spnDirections.setSelection(game.hero.direction.ordinal());
//			btnPick.setEnabled((game.getCollectableItems().length > 0));
//			spnItems.setEnabled(btnPick.isEnabled());
	
			gameView.setBackgroundColor( Color.argb( 255, ( int )Utils.clamp( game.station.hullTemperature, 0, 255 ), 0, 64 ) );
			gameView.update( 100 );
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View v, int arg2,
			long arg3) {

//		if ( v == spnDirections ) {
			
			game.hero.direction = (Direction) spnDirections.getSelectedItem();
			spnLocations.setAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_spinner_item, game
					.getConnectionNames()));
//		} else {
//			try {
//				spnDirections.setSelection( game.hero.getLocation().getConnectionDirectionForLocation( game.station.getLocation( (String) spnLocations.getSelectedItem() ) ).ordinal() );
//			} catch (InvalidSlotException e) {
//				e.printStackTrace();
//			} catch (InvalidLocationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}
