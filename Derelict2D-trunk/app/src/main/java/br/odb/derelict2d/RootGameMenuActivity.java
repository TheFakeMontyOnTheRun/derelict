package br.odb.derelict2d;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import br.odb.gamelib.android.AndroidUtils;
import br.odb.gamelib.android.GameView;
import br.odb.gamerendering.rendering.AssetManager;
import br.odb.gamerendering.rendering.DisplayList;
import br.odb.gamerendering.rendering.RenderingNode;
import br.odb.gamerendering.rendering.SVGRenderingNode;
import br.odb.gameutils.Rect;
import br.odb.gameutils.math.Vec2;
import br.odb.libsvg.SVGGraphic;

public class RootGameMenuActivity extends Activity implements OnClickListener, OnTouchListener, CompoundButton.OnCheckedChangeListener {

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        ((Derelict2DApplication) getApplication()).toggleSpeech();
    }

    private android.widget.CheckBox chkSound;
    private android.widget.CheckBox chkSpeech;
    private Spinner spnLevel;

    private GameView gvLogoInkscape;
    private GameView gvGithub;
    private GameView gvBeer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root_game_menu);

        findViewById(R.id.btnExploreStation).setOnClickListener(this);
        findViewById(R.id.btnAbout).setOnClickListener(this);
        findViewById(R.id.btnHowToPlay).setOnClickListener(this);

        chkSound = findViewById(R.id.chkSound);
        chkSpeech = findViewById(R.id.chkSpeech);

        chkSpeech.setOnCheckedChangeListener(this);

        chkSound.setChecked(((Derelict2DApplication) getApplication())
                .mayEnableSound());


        gvSplash = findViewById(R.id.gvbg);
        gvLogoInkscape = findViewById(R.id.gvLogoInkscape);
        gvGithub = findViewById(R.id.gvLogoGithub);
        gvBeer = findViewById(R.id.gvBeer);
        findViewById(R.id.llGithub).setOnTouchListener(this);
        findViewById(R.id.llBeer).setOnTouchListener(this);
        findViewById(R.id.llInkscape).setOnTouchListener(this);
    }

    private GameView gvSplash;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        initImage();

        AndroidUtils.initImage(gvGithub, "logo_github",
                ((Derelict2DApplication) getApplication()).getAssetManager());
        AndroidUtils.initImage(gvLogoInkscape, "logo_inkscape",
                ((Derelict2DApplication) getApplication()).getAssetManager());
        AndroidUtils.initImage(gvBeer, "beer",
                ((Derelict2DApplication) getApplication()).getAssetManager());
    }

    private void initImage() {

        DisplayList dl = new DisplayList("dl");
        AssetManager resManager = ((Derelict2DApplication) getApplication())
                .getAssetManager();

        SVGGraphic graphic = resManager.getGraphics("logo");

        float scale = 1;
        Vec2 trans = new Vec2();

        if (gvSplash.getWidth() > 0 && gvSplash.getHeight() > 0) {

            Rect bound = graphic.makeBounds();

            // não me interessa a parte acima da "página".
            float newWidth = bound.p1.x;
            float newHeight = bound.p1.y;

            if (newWidth > newHeight) {
                scale = gvSplash.getWidth() / newWidth;
                trans.y = (gvSplash.getHeight() - (bound.p1.y * scale)) / 2.0f;
            } else {
                scale = gvSplash.getHeight() / newHeight;
                trans.x = (gvSplash.getWidth() - (bound.p1.x * scale)) / 2.0f;
            }
        }

        graphic = graphic.scale(scale, scale);
        SVGRenderingNode node = new SVGRenderingNode(graphic, "title");
        node.translate.set(trans);

        dl.setItems(new RenderingNode[]{node});

        gvSplash.setRenderingContent(dl);
        gvSplash.postInvalidate();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Bundle bundle = new Bundle();
        bundle.putString("hasSound", chkSound.isChecked() ? "y" : "n");
        bundle.putString("speech", chkSpeech.isChecked() ? "y" : "n");

        switch (v.getId()) {

            case R.id.btnExploreStation:

                ((Derelict2DApplication) this.getApplication()).startNewGame();

                intent = new Intent(getBaseContext(), ExploreStationActivity.class);

                intent.putExtras(bundle);
                startActivityForResult(intent, 1);

                break;

            case R.id.btnHowToPlay:
                intent = new Intent(this, ShowHowToPlayActivity.class);
                startActivity(intent);
                break;
            case R.id.btnAbout:
                intent = new Intent(this, ShowCreditsActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent arg1) {
        Intent i;

        switch (v.getId()) {
            case R.id.llBeer:
                Toast.makeText(this, "Soon! Still have some things to sort out.", Toast.LENGTH_LONG).show();
                break;
            case R.id.llInkscape:
                i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://inkscape.org/"));
                startActivity(i);
                break;
            case R.id.llGithub:
                i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/TheFakeMontyOnTheRun/derelict"));
                startActivity(i);
                break;
        }
        return true;
    }
}
