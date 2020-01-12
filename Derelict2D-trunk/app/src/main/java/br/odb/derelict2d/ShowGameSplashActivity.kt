package br.odb.derelict2d

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import br.odb.gamelib.android.AndroidUtils
import br.odb.gamelib.android.GameView
import kotlinx.android.synthetic.main.activity_show_game_splash.*

class ShowGameSplashActivity : Activity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private var theme: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_game_splash)

        chkSound.setChecked((application as Derelict2DApplication)
                .mayEnableSound())

        chkSound.setOnCheckedChangeListener(this)
        gvSplash.setVisibility(View.INVISIBLE)
        gvLogo.setVisibility(View.INVISIBLE)
        btnExploreStation.setOnClickListener(this)
        btnAbout.setOnClickListener(this)
        btnHowToPlay.setOnClickListener(this)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
    }

    private fun startLoading() {
        Thread(Runnable {
            runOnUiThread {
                initImage()
                gvSplash!!.postInvalidate()
                gvLogo!!.postInvalidate()
                gvSplash!!.visibility = View.VISIBLE
                gvLogo!!.visibility = View.VISIBLE
            }
        }).start()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        startLoading()
        startTheme()
    }

    private fun startTheme() {
        if (theme == null
                && (application as Derelict2DApplication).mayEnableSound()) {
            theme = MediaPlayer.create(this, R.raw.derelicttheme)

            if ( theme != null ) {
                theme!!.setLooping(true)
                theme!!.start()
            }
        }
    }

    private fun initImage() {
        val app = application as Derelict2DApplication
        AndroidUtils.initImage(gvLogo, "logo", app.assetManager)
        AndroidUtils.initImage(gvSplash, "title", app.assetManager)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        finish()
    }

    override fun onPause() {
        if (theme != null) {
            theme!!.stop()
        }
        super.onPause()
    }

    override fun onDestroy() {
        if (theme != null) {
            theme!!.stop()
        }
        super.onDestroy()
    }

    override fun onClick(v: View) {
        if (theme != null) {
            theme!!.stop()
        }
        val intent: Intent
        val bundle = Bundle()
        bundle.putString("hasSound", if (chkSound!!.isChecked) "y" else "n")
        when (v.id) {
            R.id.btnExploreStation -> {
                (this.application as Derelict2DApplication).startNewGame()
                intent = Intent(this, ExploreStationActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }
            R.id.btnHowToPlay -> {
                intent = Intent(this, ShowHowToPlayActivity::class.java)
                startActivity(intent)
            }
            R.id.btnAbout -> {
                intent = Intent(this, ShowCreditsActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (!isChecked) {
            if (theme != null) {
                theme!!.stop()
                theme = null
            }
        } else {
            startTheme()
        }
    }
}