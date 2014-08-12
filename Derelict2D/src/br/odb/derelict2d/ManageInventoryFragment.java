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
import br.odb.gamerendering.rendering.AssetManager;
import br.odb.gamerendering.rendering.DisplayList;
import br.odb.gamerendering.rendering.RenderingNode;
import br.odb.gamerendering.rendering.SVGRenderingNode;
import br.odb.gameworld.Item;
import br.odb.libsvg.SVGGraphic;
import br.odb.utils.Rect;
import br.odb.utils.math.Vec2;

public class ManageInventoryFragment extends Fragment implements
		GameUpdateDelegate, OnClickListener, OnItemSelectedListener {

	private DerelictGame game;
	private Spinner spnCollectedItems;
	private Spinner spnLocationItems;
//	private Spinner spnActions;
	GameView gvPick;
	GameView gvUseWith;
	GameView gvUse;
	GameView gvDrop;
	GameView gvToggle;
	private WebView wvDescription;
//	Button btnDo;
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
//		spnActions = (Spinner) toReturn.findViewById(R.id.spnActions);
//		spnActions.setOnItemSelectedListener(this);
		wvDescription = (WebView) toReturn.findViewById(R.id.wvDescription);

//		btnDo = (Button) toReturn.findViewById(R.id.btnDo);
		btnInfo = (Button) toReturn.findViewById(R.id.btnInfo);
		btnInfoToCollect = (Button) toReturn.findViewById(R.id.btnInfoToCollect);
//		btnDo.setOnClickListener(this);
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

//		spnActions.setAdapter(new ArrayAdapter<UserCommandLineAction>(
//				getActivity(), android.R.layout.simple_spinner_item,
//				cmdsFiltered));
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

//		UserCommandLineAction cmd = ((UserCommandLineAction) spnActions
//				.getSelectedItem());

		spnCollectedItems.setEnabled(spnCollectedItems.getCount() > 0);
		btnInfo.setEnabled(spnCollectedItems.isEnabled());

//		spnLocationItems.setEnabled(cmd.requiredOperands() > 1
//				&& spnLocationItems.getCount() > 0);
//
//		btnDo.setEnabled((spnCollectedItems.getCount() > 0 && cmd
//				.requiredOperands() == 1)
//				|| (spnCollectedItems.getCount() > 0
//						&& spnLocationItems.getCount() > 0 && cmd
//						.requiredOperands() == 2)
//				|| (spnLocationItems.getCount() > 0 && cmd.requiredOperands() == 1)
//				|| (cmd.requiredOperands() == 0));

		String desc = game.getTextOutput().replaceAll("\n", "<br/>");
		wvDescription.getSettings().setJavaScriptEnabled(false);
		wvDescription.loadDataWithBaseURL(null,
				"<html><body bgcolor = '#0D0' >" + desc + "</body></html>",
				"text/html", "utf-8", null);
	}

	@Override
	public void onClick(View v) {

		String line;
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
		
//		case R.id.btnDo:
//			UserCommandLineAction cmd = ((UserCommandLineAction) spnActions
//					.getSelectedItem());
//			String actionName = cmd.toString();
//			String operand = "";
//			int operandsTakenSoFar = 0;
//			String operand2 = "";
//			UserCommandLineAction action;
//			action = (UserCommandLineAction) spnActions.getSelectedItem();
//
//			if (spnCollectedItems.getCount() > 0
//					&& action.requiredOperands() > operandsTakenSoFar) {
//				operand = getCurrentHoldingItemName();
//
//				++operandsTakenSoFar;
//			}
//
//			if (spnLocationItems.getCount() > 0
//					&& action.requiredOperands() > operandsTakenSoFar) {
//				operand2 = getCurrentLocationItemName();
//				++operandsTakenSoFar;
//			}
//
//			line = actionName + " " + operand2 + " " + operand;
//			System.out.println(">" + line);
//			game.sendData(line);
//			((ExploreStationActivity) getActivity()).update();
//			break;

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

		// porque preciso disso?

		// UserCommandLineAction cmd = ((UserCommandLineAction) spnActions
		// .getSelectedItem());
		//
		// spnLocationItems.setEnabled(cmd.requiredOperands() > 1
		// && spnLocationItems.getCount() > 0);

		updateWidgets();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
}
