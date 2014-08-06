package br.odb.derelict2d;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebView.FindListener;
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
	private Spinner spnActions;
	GameView gvPick;
	GameView gvUseWith;
	GameView gvUse;
	GameView gvDrop;
	GameView gvToggle;
	private WebView wvDescription;
	Button btnDo;
	private Button btnInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View toReturn = inflater.inflate(R.layout.activity_manage_inventory,
				container, false);

		spnCollectedItems = (Spinner) toReturn.findViewById(R.id.spnCollected);
		spnLocationItems = (Spinner) toReturn
				.findViewById(R.id.spnLocationItems);
		spnActions = (Spinner) toReturn.findViewById(R.id.spnActions);
		spnActions.setOnItemSelectedListener(this);
		wvDescription = (WebView) toReturn.findViewById(R.id.wvDescription);

		btnDo = (Button) toReturn.findViewById(R.id.btnDo);
		btnInfo = (Button) toReturn.findViewById(R.id.btnInfo);
		btnDo.setOnClickListener(this);
		btnInfo.setOnClickListener(this);

		gvPick = (GameView) toReturn.findViewById(R.id.gvPick);
		gvUseWith = (GameView) toReturn.findViewById(R.id.gvUseWith);
		gvUse = (GameView) toReturn.findViewById(R.id.gvUse);
		gvDrop = (GameView) toReturn.findViewById(R.id.gvDrop);
		gvToggle = (GameView) toReturn.findViewById(R.id.gvToggle);

		initImage(gvPick, "icon-pick");
		initImage(gvUseWith, "icon-use-with");
		initImage(gvUse, "icon-use");
		initImage(gvDrop, "icon-drop");
		initImage(gvToggle, "icon-toggle");

		buildCommandList();

		return toReturn;
	}

	void initImage(GameView gv, String graphicPath) {

		DisplayList dl = new DisplayList("dl");
		AssetManager resManager = ((Derelict2DApplication) getActivity()
				.getApplication()).getAssetManager();

		SVGGraphic graphic = resManager.getGraphics(graphicPath);

		float scale = 1;
		Vec2 trans = new Vec2();

		if ( gv.getWidth() > 0 && gv.getHeight() > 0) {

			Rect bound = graphic.makeBounds();

			// não me interessa a parte acima da "página".
			float newWidth = bound.p1.x;
			float newHeight = bound.p1.y;

			if (newWidth > newHeight) {
				scale = gv.getWidth() / newWidth;
				trans.y = (gv.getHeight() - (bound.p1.y * scale)) / 2.0f;
			} else {
				scale = gv.getHeight() / newHeight;
				trans.x = (gv.getWidth() - (bound.p1.x * scale)) / 2.0f;
			}
		}
		
		graphic = graphic.scale( scale, scale );
		
		
		
		SVGRenderingNode node = new SVGRenderingNode(graphic, "graphic_"
				+ graphicPath);

		gv.setAntiAliasing( false );
		dl.setItems(new RenderingNode[] { node });
		gv.setRenderingContent(dl);
		gv.updater.setRunning( false );
		gv.postInvalidate();
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

		spnActions.setAdapter(new ArrayAdapter<UserCommandLineAction>(
				getActivity(), android.R.layout.simple_spinner_item,
				cmdsFiltered));
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

		UserCommandLineAction cmd = ((UserCommandLineAction) spnActions
				.getSelectedItem());

		spnCollectedItems.setEnabled(spnCollectedItems.getCount() > 0);
		btnInfo.setEnabled(spnCollectedItems.isEnabled());

		spnLocationItems.setEnabled(cmd.requiredOperands() > 1
				&& spnLocationItems.getCount() > 0);

		btnDo.setEnabled((spnCollectedItems.getCount() > 0 && cmd
				.requiredOperands() == 1)
				|| (spnCollectedItems.getCount() > 0
						&& spnLocationItems.getCount() > 0 && cmd
						.requiredOperands() == 2)
				|| (spnLocationItems.getCount() > 0 && cmd.requiredOperands() == 1)
				|| (cmd.requiredOperands() == 0));

		String desc = game.getTextOutput().replaceAll("\n", "<br/>");
		wvDescription.getSettings().setJavaScriptEnabled(false);
		wvDescription.loadDataWithBaseURL(null,
				"<html><body bgcolor = '#0D0' >" + desc + "</body></html>",
				"text/html", "utf-8", null);
	}

	@Override
	public void onClick(View v) {

		String line;

		switch (v.getId()) {
		case R.id.btnDo:
			UserCommandLineAction cmd = ((UserCommandLineAction) spnActions
					.getSelectedItem());
			String actionName = cmd.toString();
			String operand = "";
			int operandsTakenSoFar = 0;
			String operand2 = "";
			UserCommandLineAction action;
			action = (UserCommandLineAction) spnActions.getSelectedItem();

			if (spnCollectedItems.getCount() > 0
					&& action.requiredOperands() > operandsTakenSoFar) {
				operand = ((Item) spnCollectedItems.getSelectedItem())
						.getName();

				++operandsTakenSoFar;
			}

			if (spnLocationItems.getCount() > 0
					&& action.requiredOperands() > operandsTakenSoFar) {
				operand2 = ((Item) spnLocationItems.getSelectedItem())
						.getName();
				++operandsTakenSoFar;
			}

			line = actionName + " " + operand2 + " " + operand;
			System.out.println(">" + line);
			game.sendData(line);
			((ExploreStationActivity) getActivity()).update();
			break;

		case R.id.btnInfo:
			showInfoDialog();
			break;
		}
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
