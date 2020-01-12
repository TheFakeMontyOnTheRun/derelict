package br.odb.derelict2d

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_show_credits.*

class ShowCreditsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_credits)
        title = "About this game"
    }
}