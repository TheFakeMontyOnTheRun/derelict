package br.odb.derelict2d

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import br.odb.gamerendering.rendering.DisplayList
import br.odb.gamerendering.rendering.RenderingNode
import br.odb.gamerendering.rendering.SVGRenderingNode
import br.odb.gameutils.math.Vec2
import kotlinx.android.synthetic.main.activity_show_outcome.*

class ShowOutcomeActivity : Activity() {

    private var theme: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_outcome)

        txtOutcome.text = intent.extras!!.getString("outcome")

        if (theme == null
                && (application as Derelict2DApplication).mayEnableSound()) {

            theme = MediaPlayer.create(this, R.raw.derelicttheme)

            if ( theme != null ) {
                theme!!.isLooping = true
                theme!!.start()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val dl = DisplayList("dl")

        val resManager = (application as Derelict2DApplication)
                .assetManager

        var graphic = resManager.getGraphics("logo")
        var scale = 1f
        val trans = Vec2()
        val gvSplash = gvGameLogoOutcome

        if (gvSplash.width > 0 && gvSplash.height > 0) {
            val bound = graphic.makeBounds()
            // não me interessa a parte acima da "página".
            val newWidth = bound.p1.x
            val newHeight = bound.p1.y
            if (newWidth > newHeight) {
                scale = gvSplash.width / newWidth
                trans.y = (gvSplash.height - bound.p1.y * scale) / 2.0f
            } else {
                scale = gvSplash.height / newHeight
                trans.x = (gvSplash.width - bound.p1.x * scale) / 2.0f
            }
        }
        graphic = graphic.scale(scale, scale)
        val node = SVGRenderingNode(graphic, "title")
        node.translate.set(trans)
        dl.items = arrayOf<RenderingNode>(node)
        gvSplash.setRenderingContent(dl)
        gvSplash.postInvalidate()
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
}