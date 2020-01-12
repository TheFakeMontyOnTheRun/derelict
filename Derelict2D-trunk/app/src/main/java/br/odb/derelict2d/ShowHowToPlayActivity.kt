package br.odb.derelict2d

import android.app.Activity
import android.os.Bundle
import br.odb.gamelib.android.AndroidUtils
import kotlinx.android.synthetic.main.activity_show_how_to_play.*

class ShowHowToPlayActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_how_to_play)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        AndroidUtils.initImage(gvHowToPlayHero, "hero-centered", (application as Derelict2DApplication).assetManager, 200, 200, "")
        AndroidUtils.initImage(gvHowToPlayItem, "plasma-gun", (application as Derelict2DApplication).assetManager)
        AndroidUtils.initImage(gvHowToPlayPick, "icon-pick", (application as Derelict2DApplication).assetManager)
        AndroidUtils.initImage(gvHowToPlayToggle, "icon-toggle", (application as Derelict2DApplication).assetManager)
        AndroidUtils.initImage(gvHowToPlayUseWith, "icon-use-with", (application as Derelict2DApplication).assetManager)
        AndroidUtils.initImage(gvHowToPlayUse, "icon-use", (application as Derelict2DApplication).assetManager)
    }
}