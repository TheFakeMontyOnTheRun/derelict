package br.odb.derelict2d;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import br.odb.derelict.core.DerelictGame;
import br.odb.derelict.core.commands.DerelictUserMetaCommandLineAction;
import br.odb.derelict.core.commands.DerelictUserMoveCommandLineAction;
import br.odb.gameapp.GameUpdateDelegate;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.gamelib.android.AndroidUtils;
import br.odb.gamelib.android.GameView;
import br.odb.gameworld.ActiveItem;
import br.odb.gameworld.Item;

public class ManageInventoryFragment extends Fragment implements
		GameUpdateDelegate, OnClickListener, OnItemSelectedListener {

	private DerelictGame game;
	private Spinner spnCollectedItems;
	private Spinner spnLocationItems;
	GameView gvPick;
	GameView gvUseWith;
	GameView gvUse;
	GameView gvDrop;
	GameView gvToggle;
	private WebView wvDescription;
	private Button btnInfo;
	private Button btnInfoToCollect;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View toReturn = inflater.inflate(R.layout.activity_manage_inventory,
				container, false);

		spnCollectedItems = (Spinner) toReturn.findViewById(R.id.spnCollected);
		spnLocationItems = (Spinner) toReturn
				.findViewById(R.id.spnLocationItems);
		
		spnCollectedItems.setOnItemSelectedListener( this );
		spnLocationItems.setOnItemSelectedListener( this );
		
		wvDescription = (WebView) toReturn.findViewById(R.id.wvDescription);

		btnInfo = (Button) toReturn.findViewById(R.id.btnInfo);
		btnInfoToCollect = (Button) toReturn.findViewById(R.id.btnInfoToCollect);
		btnInfo.setOnClickListener(this);
		btnInfoToCollect.setOnClickListener(this);

		gvPick = (GameView) toReturn.findViewById(R.id.gvPick);
		gvUseWith = (GameView) toReturn.findViewById(R.id.gvUseWith);
		gvUse = (GameView) toReturn.findViewById(R.id.gvUse);
		gvDrop = (GameView) toReturn.findViewById(R.id.gvDrop);
		gvToggle = (GameView) toReturn.findViewById(R.id.gvToggle);
		
		gvPick.setOnClickListener( this );
		gvUseWith.setOnClickListener( this );
		gvUse.setOnClickListener( this );
		gvDrop.setOnClickListener( this );
		gvToggle.setOnClickListener( this );

		AndroidUtils.initImage(gvPick, "icon-pick", ((Derelict2DApplication) getActivity() .getApplication()).getAssetManager());
		AndroidUtils.initImage(gvUseWith, "icon-use-with", ((Derelict2DApplication) getActivity() .getApplication()).getAssetManager());
		AndroidUtils.initImage(gvUse, "icon-use", ((Derelict2DApplication) getActivity() .getApplication()).getAssetManager());
		AndroidUtils.initImage(gvDrop, "icon-drop", ((Derelict2DApplication) getActivity() .getApplication()).getAssetManager());
		AndroidUtils.initImage(gvToggle, "icon-toggle", ((Derelict2DApplication) getActivity() .getApplication()).getAssetManager());

		buildCommandList();

		return toReturn;
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		game = ((Derelict2DApplication) this.getActivity().getApplication()).game;
		((ExploreStationActivity) activity).addUpdateListener(this);
	}

	private void buildCommandList() {
		UserCommandLineAction[] cmds = game.getAvailableCommands();

		ArrayList<UserCommandLineAction> cmdsFiltered = new ArrayList<UserCommandLineAction>();

		for (UserCommandLineAction ucmd : cmds) {
			if (!(ucmd instanceof DerelictUserMetaCommandLineAction || ucmd instanceof DerelictUserMoveCommandLineAction)) {
				cmdsFiltered.add(ucmd);
			}
		}
	}

	@Override
	public void update() {

		ArrayList<Item> tmp = new ArrayList<Item>();

		for (Item i : game.getCollectableItems()) {
			tmp.add(0, i);
		}

		Item[] items = tmp.toArray(new Item[tmp.size()]);

		setListFor(spnLocationItems, items);
		setListFor(spnCollectedItems, game.getCollectedItems());
		updateWidgets();
	}

	private void setListFor(Spinner widget, Item[] itemList) {

		Item previousSelection;
		int itemIndex = -1;

		previousSelection = (Item) widget.getSelectedItem();

		widget.setAdapter(new ArrayAdapter<Item>(getActivity(),
				android.R.layout.simple_spinner_item, itemList));

		for (int c = 0; c < widget.getCount(); ++c) {
			if (previousSelection == widget.getItemAtPosition(c)) {
				itemIndex = c;
			}
		}

		if (itemIndex != -1) {
			widget.setSelection(itemIndex, true);
			widget.postInvalidate();
		}

	}

	private void updateWidgets() {


		spnCollectedItems.setEnabled(spnCollectedItems.getCount() > 0);
		btnInfo.setEnabled(spnCollectedItems.isEnabled());
		
		gvUseWith.setEnabled( spnCollectedItems.isEnabled() );
		gvUse.setEnabled( spnCollectedItems.isEnabled() );		
		gvToggle.setEnabled( spnCollectedItems.getCount() > 0 && ( spnCollectedItems.getSelectedItem() instanceof ActiveItem ) );
		gvDrop.setEnabled( spnCollectedItems.getCount() > 0 );
		gvPick.setEnabled( spnLocationItems.getCount() > 0 );
		
		if ( gvToggle.isEnabled() ) {
			gvToggle.setAlpha( 1.0f );
		} else {			
			gvToggle.setAlpha( 0.5f );
		}
		
		if ( gvUse.isEnabled() ) {
			gvUse.setAlpha( 1.0f );
		} else {			
			gvUse.setAlpha( 0.5f );
		}
		
		if ( gvUseWith.isEnabled() ) {
			gvUseWith.setAlpha( 1.0f );
		} else {			
			gvUseWith.setAlpha( 0.5f );
		}
		
		if ( gvDrop.isEnabled() ) {
			gvDrop.setAlpha( 1.0f );
		} else {			
			gvDrop.setAlpha( 0.5f );
		}
		
		if ( gvPick.isEnabled() ) {
			gvPick.setAlpha( 1.0f );
		} else {			
			gvPick.setAlpha( 0.5f );
		}
		
		String desc = game.getTextOutput().replaceAll("\n", "<br/>");
		wvDescription.getSettings().setJavaScriptEnabled(false);
		wvDescription.loadDataWithBaseURL(null,
				"<html><body bgcolor = '#0D0' >" + desc + "</body></html>",
				"text/html", "utf-8", null);
	}

	@Override
	public void onClick(View v) {

		String itemName;
		String itemName2;
		
		switch (v.getId()) {
		
		case R.id.gvDrop:
			
			itemName = getCurrentHoldingItemName();
			
			if ( itemName != null ) {				
				game.sendData( "drop " + itemName  );
			}
			((ExploreStationActivity) getActivity()).update();
			break;
		case R.id.gvToggle:
			
			itemName = getCurrentHoldingItemName();
			
			if ( itemName != null ) {				
				game.sendData( "toggle " + itemName  );
			}
			((ExploreStationActivity) getActivity()).update();
			break;
			
		case R.id.gvPick:
			itemName = getCurrentLocationItemName();
			
			if ( itemName != null ) {				
				game.sendData( "pick " + itemName  );
			}
			((ExploreStationActivity) getActivity()).update();
			break;
			
		case R.id.gvUseWith:
			itemName = getCurrentHoldingItemName();
			itemName2 = getCurrentLocationItemName();
			
			if ( itemName != null ) {				
				game.sendData( "useWith " + itemName2 + " " + itemName  );
			}
			((ExploreStationActivity) getActivity()).update();
			break;
		case R.id.btnInfo:
			showInfoDialog();
			break;
		case R.id.btnInfoToCollect:
			showInfoToCollectDialog();
			break;
		}
	}

	private String getCurrentLocationItemName() {
		if ( spnLocationItems.getCount() > 0 ) {		
			return ((Item) spnLocationItems.getSelectedItem()).getName();
		} else {
			return null;
		}
	}

	private String getCurrentHoldingItemName() {
		
		if ( spnCollectedItems.getCount() > 0 ) {			
			return ((Item) spnCollectedItems.getSelectedItem()).getName();
		} else {
			return null;
		}
	}
	
	public void showInfoToCollectDialog() {
		FragmentManager fm = getFragmentManager();
		ShowItemStatsDialogFragment showItemStatsFragment = new ShowItemStatsDialogFragment();
		Bundle args = new Bundle();
		Item item = ((Item) spnLocationItems.getSelectedItem());
		args.putString("name", item.getName());
		args.putString("desc", item.getDescription());
		showItemStatsFragment.setArguments(args);
		showItemStatsFragment.show(fm, "show_item_stats_layout");		
	}

	private void showInfoDialog() {
		FragmentManager fm = getFragmentManager();
		ShowItemStatsDialogFragment showItemStatsFragment = new ShowItemStatsDialogFragment();
		Bundle args = new Bundle();
		Item item = ((Item) spnCollectedItems.getSelectedItem());
		args.putString("name", item.getName());
		args.putString("desc", item.getDescription());
		showItemStatsFragment.setArguments(args);
		showItemStatsFragment.show(fm, "show_item_stats_layout");
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

		updateWidgets();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
}
