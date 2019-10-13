package br.odb.derelict2d;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ShowCreditsActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_credits);

		setTitle("About this game");

		findViewById(R.id.llAdrian).setOnClickListener(this);
		findViewById(R.id.llCindy).setOnClickListener(this);
		findViewById(R.id.llPaulo).setOnClickListener(this);
		findViewById(R.id.llPedro).setOnClickListener(this);
		findViewById(R.id.llDaniel).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		Intent i;

		switch (v.getId()) {
			case R.id.llAdrian:
				i = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http://www.aiyra.com"));

				break;
			case R.id.llCindy:
				i = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http://cindydalfovo.com/"));

				break;
			case R.id.llPaulo:
				i = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http://nideck.com.br/"));

				break;
			case R.id.llPedro:
				i = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http://twitter.com/ferphen"));

				break;
			case R.id.llDaniel:

			default:
				i = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http://about.me/danielmonteiro"));
		}

		this.startActivity(i);
	}
}
