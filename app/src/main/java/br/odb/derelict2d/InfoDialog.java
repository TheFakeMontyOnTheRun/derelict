package br.odb.derelict2d;

import android.app.Activity;
import android.app.DialogFragment;
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

import java.util.ArrayList;

import br.odb.derelict.core.DerelictGame;
import br.odb.derelict.core.commands.DerelictUserMetaCommandLineAction;
import br.odb.derelict.core.commands.DerelictUserMoveCommandLineAction;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.gameworld.Item;

public class InfoDialog extends DialogFragment implements GameUpdateDelegate, OnClickListener, OnItemSelectedListener {

	private DerelictGame game;
	private Spinner spnCollectedItems;
	private Spinner spnLocationItems;
	private Spinner spnActions;

	private WebView wvDescription;
	private Button btnDo;
	private Button btnInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View toReturn = inflater.inflate(R.layout.activity_manage_inventory,
				container, false);

		spnCollectedItems = toReturn.findViewById(R.id.spnCollected);
		spnLocationItems = toReturn
				.findViewById(R.id.spnLocationItems);
		spnActions = toReturn.findViewById(R.id.spnActions);
		spnActions.setOnItemSelectedListener(this);
		wvDescription = toReturn.findViewById(R.id.wvDescription);


		btnDo = toReturn.findViewById(R.id.btnDo);
		btnInfo = toReturn.findViewById(R.id.btnInfo);
		btnDo.setOnClickListener(this);
		btnInfo.setOnClickListener(this);
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

		ArrayList<UserCommandLineAction> cmdsFiltered = new ArrayList<>();

		for (UserCommandLineAction ucmd : cmds) {
			if (!(ucmd instanceof DerelictUserMetaCommandLineAction || ucmd instanceof DerelictUserMoveCommandLineAction)) {
				cmdsFiltered.add(ucmd);
			}
		}

		spnActions.setAdapter(new ArrayAdapter<>(
				getActivity(), android.R.layout.simple_spinner_item,
				cmdsFiltered));
	}

	@Override
	public void update() {

		ArrayList<Item> tmp = new ArrayList<>();


		for (Item i : game.getCollectableItems()) {
			tmp.add(0, i);
		}

		Item[] items = tmp.toArray(new Item[0]);

		setListFor(spnLocationItems, items);
		setListFor(spnCollectedItems, game.getCollectedItems());
		updateWidgets();
	}

	private void setListFor(Spinner widget, Item[] itemList) {

		Item previousSelection;
		int itemIndex = -1;

		previousSelection = (Item) widget.getSelectedItem();

		widget.setAdapter(new ArrayAdapter<>(getActivity(),
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
		wvDescription.loadDataWithBaseURL(null, "<html><body bgcolor = '#0D0' >" + desc
				+ "</body></html>", "text/html", "utf-8", null);
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
					operand = ((Item) spnCollectedItems.getSelectedItem()).getName();

					++operandsTakenSoFar;
				}

				if (spnLocationItems.getCount() > 0
						&& action.requiredOperands() > operandsTakenSoFar) {
					operand2 = ((Item) spnLocationItems.getSelectedItem()).getName();
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
		updateWidgets();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
}
