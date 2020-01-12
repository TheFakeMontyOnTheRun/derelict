package br.odb.derelict2d

import android.app.Application
import android.content.Context
import android.media.AudioManager
import br.odb.derelict.core.DerelictGame
import br.odb.gamerendering.rendering.AssetManager
import br.odb.libsvg.SVGParsingUtils
import java.io.IOException
import java.io.InputStream

class Derelict2DApplication : Application() {
    val assetManager = AssetManager()
    @JvmField
	@Volatile
    var game: DerelictGame? = null

    override fun onCreate() {
        super.onCreate()
        loadAssets()
    }

    private fun loadAssets() {
        try {
            assetManager.putGraphic("floor1", SVGParsingUtils
                    .readSVG(openAsset("overview-map/floor1.svg")))
            assetManager.putGraphic("floor2", SVGParsingUtils
                    .readSVG(openAsset("overview-map/floor2.svg")))
            assetManager.putGraphic("floor3", SVGParsingUtils
                    .readSVG(openAsset("overview-map/floor3.svg")))
            assetManager.putGraphic("heroGraphic", SVGParsingUtils
                    .readSVG(openAsset("overview-map/astronaut-icon.svg")))
            assetManager.putGraphic("hero-centered", SVGParsingUtils
                    .readSVG(openAsset("overview-map/centered-astronaut.svg")))
            assetManager.putGraphic("blowtorch",
                    SVGParsingUtils.readSVG(openAsset("items/blowtorch.svg")))
            assetManager.putGraphic("bomb-remote-controller", SVGParsingUtils
                    .readSVG(openAsset("items/bomb-remote-controller.svg")))
            assetManager.putGraphic("book",
                    SVGParsingUtils.readSVG(openAsset("items/book.svg")))
            assetManager
                    .putGraphic("comm-system", SVGParsingUtils
                            .readSVG(openAsset("items/comm-system.svg")))
            assetManager.putGraphic("atmosphere-purifier", SVGParsingUtils
                    .readSVG(openAsset("items/atmosphere-purifier.svg")))
            assetManager.putGraphic("keycard-for-high-rank", SVGParsingUtils
                    .readSVG(openAsset("items/keycard-for-high-rank.svg")))
            assetManager.putGraphic("keycard-for-root-access", SVGParsingUtils
                    .readSVG(openAsset("items/keycard-for-lab-access.svg")))
            assetManager.putGraphic("keycard-for-low-rank", SVGParsingUtils
                    .readSVG(openAsset("items/keycard-for-low-rank.svg")))
            assetManager.putGraphic("lab-equipment", SVGParsingUtils
                    .readSVG(openAsset("items/lab-equipment.svg")))
            assetManager.putGraphic("electric-experiment", SVGParsingUtils
                    .readSVG(openAsset("items/lab-equipment.svg")))
            assetManager.putGraphic("backdrop", SVGParsingUtils
                    .readSVG(openAsset("overview-map/backdrop.svg")))
            assetManager.putGraphic("title",
                    SVGParsingUtils.readSVG(openAsset("title.svg")))
            assetManager.putGraphic("logo",
                    SVGParsingUtils.readSVG(openAsset("logo.svg")))
            assetManager.putGraphic("pattern",
                    SVGParsingUtils.readSVG(openAsset("pattern.svg")))
            assetManager.putGraphic("magboots",
                    SVGParsingUtils.readSVG(openAsset("items/magboots.svg")))
            assetManager.putGraphic("selection",
                    SVGParsingUtils.readSVG(openAsset("items/selection.svg")))
            assetManager
                    .putGraphic("metal-plate", SVGParsingUtils
                            .readSVG(openAsset("items/metal-plate.svg")))
            assetManager
                    .putGraphic("metal-scrap", SVGParsingUtils
                            .readSVG(openAsset("items/metal-plate.svg")))
            assetManager
                    .putGraphic("metal-sheet", SVGParsingUtils
                            .readSVG(openAsset("items/metal-plate.svg")))
            assetManager.putGraphic("plasma-gun",
                    SVGParsingUtils.readSVG(openAsset("items/plasma-gun.svg")))
            assetManager.putGraphic("plasma-pellet", SVGParsingUtils
                    .readSVG(openAsset("items/plasma-pellet.svg")))
            assetManager.putGraphic("plastic-pipes", SVGParsingUtils
                    .readSVG(openAsset("items/plastic-pipes.svg")))
            assetManager.putGraphic("time-bomb",
                    SVGParsingUtils.readSVG(openAsset("items/time-bomb.svg")))
            assetManager.putGraphic("computer-stand", SVGParsingUtils
                    .readSVG(openAsset("items/computer-stand.svg")))
            assetManager.putGraphic("ship-ignition-key", SVGParsingUtils
                    .readSVG(openAsset("items/ship-ignition-key.svg")))
            assetManager
                    .putGraphic("icon-move", SVGParsingUtils
                            .readSVG(openAsset("action-icons/move.svg")))
            assetManager
                    .putGraphic("icon-turn", SVGParsingUtils
                            .readSVG(openAsset("action-icons/turn.svg")))
            assetManager
                    .putGraphic("icon-pick", SVGParsingUtils
                            .readSVG(openAsset("action-icons/pick.svg")))
            assetManager.putGraphic("icon-use-with", SVGParsingUtils
                    .readSVG(openAsset("action-icons/useWith.svg")))
            assetManager.putGraphic("icon-use",
                    SVGParsingUtils.readSVG(openAsset("action-icons/use.svg")))
            assetManager
                    .putGraphic("icon-drop", SVGParsingUtils
                            .readSVG(openAsset("action-icons/drop.svg")))
            assetManager.putGraphic("icon-toggle", SVGParsingUtils
                    .readSVG(openAsset("action-icons/toggle.svg")))
            assetManager.putGraphic("intro-comics2",
                    SVGParsingUtils.readSVG(openAsset("chapters/intro3.svg")))
            assetManager.putGraphic("logo_github",
                    SVGParsingUtils.readSVG(openAsset("logo_github.svg")))
            assetManager.putGraphic("logo_inkscape",
                    SVGParsingUtils.readSVG(openAsset("logo_inkscape.svg")))
            assetManager.putGraphic("beer",
                    SVGParsingUtils.readSVG(openAsset("beer.svg")))
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        assetManager.addResId("atmosphereon", R.raw.atmosphereon)
        assetManager.addResId("atmosphereoff", R.raw.atmosphereoff)
        assetManager.addResId("magbootsuse", R.raw.magbootsused)
        assetManager.addResId("magbootsoff", R.raw.magbootsoff)
        assetManager.addResId("magbootson", R.raw.magbootson)
        assetManager.addResId("bonk", R.raw.bonk)
        assetManager.addResId("spooky1", R.raw.spooky1)
        assetManager.addResId("spooky2", R.raw.spooky2)
        assetManager.addResId("spooky3", R.raw.spooky3)
        assetManager.addResId("click", R.raw.click)
        assetManager.addResId("drop", R.raw.drop)
        assetManager.addResId("pick", R.raw.pick)
        assetManager.addResId("blowtorch-turned-on", R.raw.blowtorchon)
        assetManager.addResId("blowtorch-used", R.raw.blowtorchuse)
        assetManager.addResId("shot", R.raw.shot)
        assetManager.addResId("coughm", R.raw.coughm)
        assetManager.addResId("coughdeathm", R.raw.coughdeathm)
        startNewGame()
    }

    fun startNewGame() {
        game = DerelictGame()
        game!!.init()
    }

    @Throws(IOException::class)
    private fun openAsset(filename: String): InputStream {
        return assets.open(filename)
    }

    fun mayEnableSound(): Boolean {
        val am = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return when (am.ringerMode) {
            AudioManager.RINGER_MODE_NORMAL -> true
            AudioManager.RINGER_MODE_SILENT, AudioManager.RINGER_MODE_VIBRATE -> false
            else -> false
        }
    }
}