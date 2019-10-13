package br.odb.derelict2d;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import br.odb.derelict.core.DerelictGame;
import br.odb.derelict.core.commands.PickCommand;
import br.odb.derelict.core.commands.ToggleCommand;
import br.odb.derelict.core.commands.UseCommand;
import br.odb.derelict.core.commands.UseWithCommand;
import br.odb.gameapp.ApplicationClient;
import br.odb.gameapp.command.UserCommandLineAction;
import br.odb.gamerendering.rendering.AssetManager;
import br.odb.gameworld.Item;

public class ManageInventoryActivity extends Activity implements ApplicationClient, GameUpdateDelegate, OnClickListener {

    private DerelictGame game;
    private Spinner spnCollectedItems;
    private Spinner spnLocationItems;
    private Spinner spnActions;
    private AssetManager resManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_manage_inventory);

        resManager = ((Derelict2DApplication) getApplication()).getAssetManager();

        spnCollectedItems = findViewById(R.id.spnCollected);
        spnLocationItems = findViewById(R.id.spnLocationItems);
        spnActions = findViewById(R.id.spnActions);
        findViewById(R.id.btnDo).setOnClickListener(this);

        game = ((Derelict2DApplication) getApplication()).game;
        game.setApplicationClient(this);
        update();
    }

    @Override
    public void update() {
        spnLocationItems.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, game.getCollectableItems()));
        spnCollectedItems.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, game.getCollectedItems()));
        spnActions.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, game.getAvailableCommands()));
    }


    @Override
    public void onClick(View v) {
        int index;
        Item locationItem = null;
        Item collectedItem = null;

        try {
            UserCommandLineAction cmd = ((UserCommandLineAction) spnActions.getSelectedItem());
            index = spnLocationItems.getSelectedItemPosition();

            if (index != -1) {

                locationItem = game.getCollectableItems()[index];
            }

            index = spnCollectedItems.getSelectedItemPosition();

            if (index != -1) {

                collectedItem = game.getCollectedItems()[index];
            }

            String data = "";

            if (cmd instanceof PickCommand) {
                data = cmd.toString() + " " + locationItem.getName();
            } else if (cmd instanceof ToggleCommand) {
                data = cmd.toString() + " " + collectedItem.getName();
            } else if (cmd instanceof UseCommand) {
                data = cmd.toString() + " " + collectedItem.getName();
            } else if (cmd instanceof UseWithCommand) {
                data = cmd.toString() + " " + locationItem.getName() + " " + collectedItem.getName();
            } else {
                Toast.makeText(this, "Action not supported", Toast.LENGTH_SHORT).show();
            }

            game.sendData(data);
            Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Action not supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void alert(String string) {

    }

    @Override
    public void playMedia(String uri, String alt) {
        MediaPlayer.create(this, resManager.getResIdForUri(uri)).start();
    }


    @Override
    public void sendQuit() {
        // TODO Auto-generated method stub

    }
}
