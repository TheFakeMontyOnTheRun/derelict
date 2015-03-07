package br.odb.derelict2d;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import br.odb.derelict.core.Astronaut;
import br.odb.derelict.core.DerelictGame;
import br.odb.derelict.core.commands.DerelictUserMetaCommandLineAction;
import br.odb.derelict.core.commands.DerelictUserMoveCommandLineAction;
import br.odb.derelict.core.items.Book;
import br.odb.gameapp.GameUpdateDelegate;
import br.odb.gameapp.UserCommandLineAction;
import br.odb.gamelib.android.AndroidUtils;
import br.odb.gamelib.android.GameView;
import br.odb.gamerendering.rendering.DisplayList;
import br.odb.gamerendering.rendering.SVGRenderingNode;
import br.odb.gameworld.ActiveItem;
import br.odb.gameworld.Item;
import br.odb.libsvg.ColoredPolygon;

public class ManageInventoryFragment extends Fragment implements
		GameUpdateDelegate, OnClickListener {

	private DerelictGame game;
	GameView gvPick;
	GameView gvUseWith;
	GameView gvUse;
	GameView gvDrop;
	GameView gvToggle;
	// private WebView wvDescription;
	private Button btnInfo;
	private Button btnInfoToCollect;
	Item selectedCollectedItem;
	Item selectedLocationItem;
	LinearLayout llCollectedItems;
	LinearLayout llLocationItems;
	HorizontalScrollView hsvCollected;
	HorizontalScrollView lvLocationItems;
	Button btnLocationInfo;
	Button btnMakeNotes;
	TextView tvLocationName;

	TextView tvToxicty;
	TextView tvFloor;
	TextView tvFacing;
	TextView tvMoney;
	TextView tvTemperature;
	TextView tvTime;

	final HashMap<GameView, Item> itemForView = new HashMap<GameView, Item>();
	final HashMap<Item, GameView> viewForItem = new HashMap<Item, GameView>();

	String oldText;
	private TextView tvCapacity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View toReturn = inflater.inflate(
				R.layout.activity_manage_inventory, container, false);

		llCollectedItems = (LinearLayout) toReturn
				.findViewById(R.id.llCollectedItems);
		llLocationItems = (LinearLayout) toReturn
				.findViewById(R.id.llLocationItems);

		hsvCollected = (HorizontalScrollView) toReturn
				.findViewById(R.id.lvCollected);
		lvLocationItems = (HorizontalScrollView) toReturn
				.findViewById(R.id.lvLocationItems);

		// wvDescription = (WebView) toReturn.findViewById(R.id.wvDescription);
		// wvDescription.getSettings().setJavaScriptEnabled(false);

		btnInfo = (Button) toReturn.findViewById(R.id.btnInfo);
		btnInfoToCollect = (Button) toReturn
				.findViewById(R.id.btnInfoToCollect);
		btnMakeNotes = (Button) toReturn.findViewById( R.id.btnMakeNotes );
		btnMakeNotes.setOnClickListener( this );
		btnInfo.setOnClickListener(this);
		btnInfoToCollect.setOnClickListener(this);

		gvPick = (GameView) toReturn.findViewById(R.id.gvPick);
		gvUseWith = (GameView) toReturn.findViewById(R.id.gvUseWith);
		gvUse = (GameView) toReturn.findViewById(R.id.gvUse);
		gvDrop = (GameView) toReturn.findViewById(R.id.gvDrop);
		gvToggle = (GameView) toReturn.findViewById(R.id.gvToggle);

		gvPick.setOnClickListener(this);
		gvUseWith.setOnClickListener(this);
		gvUse.setOnClickListener(this);
		gvDrop.setOnClickListener(this);
		gvToggle.setOnClickListener(this);

		tvLocationName = (TextView) toReturn.findViewById(R.id.tvLocationName);
		btnLocationInfo = (Button) toReturn.findViewById(R.id.btnLocationInfo);
		btnLocationInfo.setOnClickListener(this);

		tvToxicty = (TextView) toReturn.findViewById(R.id.txtToxicity);
		tvFacing = (TextView) toReturn.findViewById(R.id.txtFacing);
		tvMoney = (TextView) toReturn.findViewById(R.id.txtMoney);
		tvTemperature = (TextView) toReturn.findViewById(R.id.txtTemperature);
		tvTime = (TextView) toReturn.findViewById(R.id.txtTime);
		tvCapacity = (TextView) toReturn.findViewById(R.id.txtCapacity);

		llCollectedItems.setOnClickListener(this);

		buildCommandList();

		toReturn.post(new Runnable() {
			@Override
			public void run() {
				AndroidUtils
						.initImage(gvPick, "icon-pick",
								((Derelict2DApplication) getActivity()
										.getApplication()).getAssetManager());
				AndroidUtils
						.initImage(gvUseWith, "icon-use-with",
								((Derelict2DApplication) getActivity()
										.getApplication()).getAssetManager());
				AndroidUtils
						.initImage(gvUse, "icon-use",
								((Derelict2DApplication) getActivity()
										.getApplication()).getAssetManager());
				AndroidUtils
						.initImage(gvDrop, "icon-drop",
								((Derelict2DApplication) getActivity()
										.getApplication()).getAssetManager());
				AndroidUtils
						.initImage(gvToggle, "icon-toggle",
								((Derelict2DApplication) getActivity()
										.getApplication()).getAssetManager());
				scheduleUpdate(toReturn);
			}
		});

		return toReturn;
	}

	public void scheduleUpdate(View view) {
		view.post(new Runnable() {
			@Override
			public void run() {
				update();
			}
		});
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
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		update();
	}

	@Override
	public void update() {

		GameView gv;
		ColoredPolygon active;
		DisplayList dl;
		ViewGroup vg;

		int size = llLocationItems.getHeight();

		if (size == 0) {
			return;
		}

		llLocationItems.removeAllViews();
		for (Item i : game.getCollectableItems()) {

			if (!viewForItem.containsKey(i)) {

				gv = new GameView(llLocationItems.getContext());
				gv.setLayoutParams(new LayoutParams(size, size));
				gv.setOnClickListener(this);
				gv.setAlpha(1.0f);
				AndroidUtils
						.initImage(gv, i.getName(),
								((Derelict2DApplication) getActivity()
										.getApplication()).getAssetManager(),
								size, size, "");
				itemForView.put(gv, i);
				viewForItem.put(i, gv);

			} else {
				gv = viewForItem.get(i);
			}

			dl = (DisplayList) gv.getRenderingContent();
			active = ((SVGRenderingNode) dl.getItems()[0]).graphic
					.getShapeById("active");

			if (active != null) {
				active.visible = ((ActiveItem) i).isActive();
			}

			if (gv.getParent() != null) {

				vg = (ViewGroup) gv.getParent();
				vg.removeView(gv);
			}
			llLocationItems.addView(gv);
		}

		vg = llLocationItems;

		for (int c = 0; c < vg.getChildCount(); ++c) {
			vg.getChildAt(c).setAlpha(1.0f);
			vg.getChildAt(c).setBackgroundColor( Color.GRAY );
		}

		if (selectedLocationItem != null) {
			viewForItem.get(selectedLocationItem).setAlpha(0.75f);
			viewForItem.get(selectedLocationItem).setBackgroundColor( Color.MAGENTA );
		}

		llCollectedItems.removeAllViews();
		for (Item i : game.getCollectedItems()) {

			if (!viewForItem.containsKey(i)) {

				gv = new GameView(llCollectedItems.getContext());
				gv.setLayoutParams(new LayoutParams(size, size));
				gv.setOnClickListener(this);
				gv.setAlpha(1.0f);
				AndroidUtils
						.initImage(gv, i.getName(),
								((Derelict2DApplication) getActivity()
										.getApplication()).getAssetManager(),
								size, size, "");
				itemForView.put(gv, i);
				viewForItem.put(i, gv);

			} else {
				gv = viewForItem.get(i);
			}

			dl = (DisplayList) gv.getRenderingContent();
			active = ((SVGRenderingNode) dl.getItems()[0]).graphic
					.getShapeById("active");

			if (active != null) {
				active.visible = ((ActiveItem) i).isActive();
			}

			if (gv.getParent() != null) {

				vg = (ViewGroup) gv.getParent();
				vg.removeView(gv);
			}

			llCollectedItems.addView(gv);
		}
		vg = llCollectedItems;
		for (int c = 0; c < vg.getChildCount(); ++c) {
			vg.getChildAt(c).setAlpha(1.0f);
			vg.getChildAt(c).setBackgroundColor( Color.GRAY );
		}

		if (selectedCollectedItem != null) {
			viewForItem.get(selectedCollectedItem).setAlpha(0.75f);
			viewForItem.get(selectedCollectedItem).setBackgroundColor( Color.YELLOW );
		}

		updateWidgets();
	}

	private void updateWidgets() {

		llCollectedItems.setEnabled(llCollectedItems.getChildCount() > 0);
		btnInfo.setEnabled(selectedCollectedItem != null);
		btnInfoToCollect.setEnabled(selectedLocationItem != null);
		gvUseWith.setEnabled(selectedCollectedItem != null
				&& selectedLocationItem != null);
		gvUse.setEnabled(selectedCollectedItem != null);
		gvToggle.setEnabled(selectedCollectedItem != null);
		gvDrop.setEnabled(selectedCollectedItem != null);
		gvPick.setEnabled(selectedLocationItem != null);

		if (gvToggle.isEnabled()) {
			gvToggle.setAlpha(1.0f);
		} else {
			gvToggle.setAlpha(0.5f);
		}

		if (gvUse.isEnabled()) {
			gvUse.setAlpha(1.0f);
		} else {
			gvUse.setAlpha(0.5f);
		}

		if (gvUseWith.isEnabled()) {
			gvUseWith.setAlpha(1.0f);
		} else {
			gvUseWith.setAlpha(0.5f);
		}

		if (gvDrop.isEnabled()) {
			gvDrop.setAlpha(1.0f);
		} else {
			gvDrop.setAlpha(0.5f);
		}

		if (gvPick.isEnabled()) {
			gvPick.setAlpha(1.0f);
		} else {
			gvPick.setAlpha(0.5f);
		}

		tvLocationName.setText("" + game.hero.getLocation().getName());

		tvToxicty.setText("" + game.hero.toxicity + "%");
		tvFacing.setText("" + game.hero.direction.prettyName);
		tvMoney.setText("" + game.hero.getMaterialWorth() + "$");
		tvTemperature.setText("" + game.station.hullTemperature + "C");
		tvTime.setText("" + game.getFormatedElapsedTime());
		tvCapacity.setText("" + game.getCollectedItems().length + "/" + Astronaut.INVENTORY_LIMIT );

	}

	@Override
	public void onClick(View v) {

		String itemName;
		String itemName2;

		switch (v.getId()) {

		case R.id.gvDrop:

			itemName = getCurrentHoldingItemName();

			if (itemName != null) {
				game.sendData("drop " + itemName);
			}

			selectedCollectedItem = null;
			((ExploreStationActivity) getActivity()).update();
			break;
		case R.id.gvToggle:

			itemName = getCurrentHoldingItemName();

			if (itemName != null) {
				game.sendData("toggle " + itemName);
			}
			((ExploreStationActivity) getActivity()).update();
			break;

		case R.id.gvPick:
			itemName = getCurrentLocationItemName();

			if (itemName != null) {
				game.sendData("pick " + itemName);
			}
			selectedCollectedItem = selectedLocationItem;
			selectedLocationItem = null;
			((ExploreStationActivity) getActivity()).update();
			break;

		case R.id.gvUse:
			itemName = getCurrentHoldingItemName();

			if (itemName != null) {
				game.sendData("use " + itemName);
			}
			((ExploreStationActivity) getActivity()).update();
			break;

		case R.id.gvUseWith:
			itemName = getCurrentHoldingItemName();
			itemName2 = getCurrentLocationItemName();

			if (itemName != null) {
				game.sendData("useWith " + itemName2 + " " + itemName);
			}
			((ExploreStationActivity) getActivity()).update();
			break;
		case R.id.btnInfo:
			showInfoDialog();
			break;
		case R.id.btnMakeNotes:
			showMakeNotesDialog();
			break;
		case R.id.btnLocationInfo:

			FragmentManager fm = getFragmentManager();
			LocationInfoDialogFragment showItemStatsFragment = new LocationInfoDialogFragment();
			showItemStatsFragment.show(fm, "fragment_location_dialog");

			break;
		case R.id.btnInfoToCollect:
			showInfoToCollectDialog();
			break;
		default:

			Item i = itemForView.get(v);

			if (i == null) {
				return;
			}

			if (v.getParent() == llLocationItems) {

				selectedLocationItem = i;

			} else if (v.getParent() == llCollectedItems) {
				selectedCollectedItem = i;
			} else {
				return;
			}

			((ExploreStationActivity) getActivity()).update();
		}
	}


	private String getCurrentLocationItemName() {
		if (selectedLocationItem != null) {
			return selectedLocationItem.getName();
		} else {
			return null;
		}
	}

	private String getCurrentHoldingItemName() {

		if (selectedCollectedItem != null) {
			return selectedCollectedItem.getName();
		} else {
			return null;
		}
	}

	public void showInfoToCollectDialog() {

		if (selectedLocationItem == null) {
			return;
		}

		FragmentManager fm = getFragmentManager();
		ShowItemStatsDialogFragment showItemStatsFragment = new ShowItemStatsDialogFragment();
		Bundle args = new Bundle();
		Item item = selectedLocationItem;

		if (item instanceof Book) {
			args.putString("name", "Book: " + ((Book) item).title);
		} else {
			args.putString("name", item.getName());
		}

		args.putString("image", item.getName());
		args.putString("desc", item.getDescription());
		showItemStatsFragment.setArguments(args);
		showItemStatsFragment.show(fm, "show_item_stats_layout");
	}

	private void showMakeNotesDialog() {
 		FragmentManager fm = getFragmentManager();
		MakeNotesDialog makeNotesDialog = new MakeNotesDialog();
		makeNotesDialog.show(fm, "activity_make_notes");
	}
	
	private void showInfoDialog() {

		if (selectedCollectedItem == null) {
			return;
		}

		FragmentManager fm = getFragmentManager();
		ShowItemStatsDialogFragment showItemStatsFragment = new ShowItemStatsDialogFragment();
		Bundle args = new Bundle();
		Item item = selectedCollectedItem;

		if (item instanceof Book) {
			args.putString("name", "Book: " + ((Book) item).title);
		} else {
			args.putString("name", item.getName());
		}
		args.putString("image", item.getName());
		args.putString("desc", item.getDescription());

		showItemStatsFragment.setArguments(args);
		showItemStatsFragment.show(fm, "show_item_stats_layout");
	}
}
