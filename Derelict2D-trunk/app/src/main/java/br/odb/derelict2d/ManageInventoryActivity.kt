package br.odb.derelict2d

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import br.odb.derelict.core.DerelictGame
import br.odb.derelict.core.commands.PickCommand
import br.odb.derelict.core.commands.ToggleCommand
import br.odb.derelict.core.commands.UseCommand
import br.odb.derelict.core.commands.UseWithCommand
import br.odb.gameapp.ApplicationClient
import br.odb.gameapp.command.UserCommandLineAction
import br.odb.gamerendering.rendering.AssetManager
import br.odb.gameworld.Item
import kotlinx.android.synthetic.main.fragment_info_dialog.*

class ManageInventoryActivity : Activity(), ApplicationClient, GameUpdateDelegate, View.OnClickListener {
    private var game: DerelictGame? = null
    private var resManager: AssetManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_inventory)
        resManager = (application as Derelict2DApplication).assetManager
        btnDo.setOnClickListener(this)
        game = (application as Derelict2DApplication).game
        game!!.setApplicationClient(this)
        update()
    }

    override fun update() {
        spnLocationItems!!.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, game!!.collectableItems)
        spnCollected!!.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, game!!.collectedItems)
        spnActions!!.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, game!!.availableCommands)
    }

    override fun onClick(v: View) {
        var index: Int
        var locationItem: Item? = null
        var collectedItem: Item? = null
        try {
            val cmd = spnActions!!.selectedItem as UserCommandLineAction
            index = spnLocationItems!!.selectedItemPosition
            if (index != -1) {
                locationItem = game!!.collectableItems[index]
            }
            index = spnCollected!!.selectedItemPosition
            if (index != -1) {
                collectedItem = game!!.collectedItems[index]
            }
            var data = ""
            if (cmd is PickCommand) {
                data = cmd.toString() + " " + locationItem!!.name
            } else if (cmd is ToggleCommand) {
                data = cmd.toString() + " " + collectedItem!!.name
            } else if (cmd is UseCommand) {
                data = cmd.toString() + " " + collectedItem!!.name
            } else if (cmd is UseWithCommand) {
                data = cmd.toString() + " " + locationItem!!.name + " " + collectedItem!!.name
            } else {
                Toast.makeText(this, "Action not supported", Toast.LENGTH_SHORT).show()
            }
            game!!.sendData(data)
            Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Action not supported", Toast.LENGTH_SHORT).show()
        }
    }

    override fun alert(string: String) {}
    override fun playMedia(uri: String, alt: String) {
        MediaPlayer.create(this, resManager!!.getResIdForUri(uri)).start()
    }

    override fun sendQuit() {}
}