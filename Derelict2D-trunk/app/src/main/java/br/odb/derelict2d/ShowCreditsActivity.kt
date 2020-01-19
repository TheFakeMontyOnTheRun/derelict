package br.odb.derelict2d

import android.app.Activity
import android.os.Bundle

class ShowCreditsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_credits)
        title = "About this game"
    }
}