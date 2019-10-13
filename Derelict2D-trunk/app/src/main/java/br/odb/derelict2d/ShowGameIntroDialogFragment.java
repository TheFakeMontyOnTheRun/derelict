package br.odb.derelict2d;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import br.odb.gamelib.android.AndroidUtils;
import br.odb.gamelib.android.GameView;

public class ShowGameIntroDialogFragment extends DialogFragment implements
        OnClickListener {

    private final String[] buttonTitles = {"Next", "Next", "Dismiss"};
    private final String[] imagePath = {"intro-comics2", "intro-comics2", "intro-comics2"};
    private WebView wvStory;
    private GameView gvIntroComics;
    private Button btNextFinish;
    private String[] storyBits;
    private int currentStoryPoint = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.show_game_intro,
                container, true);

        btNextFinish = view.findViewById(R.id.btnDismissStory);
        btNextFinish.setOnClickListener(this);

        wvStory = view.findViewById(R.id.wvStory);
        gvIntroComics = view.findViewById(R.id.gvIntroComics);
        Bundle args = getArguments();


        getDialog().setTitle("..And so, here we go...");
        storyBits = args.getString("desc").split("\n\n\n");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateDescription(0);
            }
        }, 100);


        return view;
    }


    private void updateDescription(final int storyPoint) {

        wvStory.getSettings().setJavaScriptEnabled(false);
        wvStory.loadDataWithBaseURL(null, "<html><body bgcolor = '#0D0' >"
                + storyBits[storyPoint].replaceAll("\n", "<br/>") + "</body></html>", "text/html", "utf-8", null);

        if (imagePath[storyPoint] != null) {

            gvIntroComics.setVisibility(View.VISIBLE);
            AndroidUtils.initImage(gvIntroComics, imagePath[storyPoint], ((Derelict2DApplication) getActivity()
                    .getApplication()).getAssetManager());
        } else {
            gvIntroComics.setVisibility(View.INVISIBLE);
        }


        btNextFinish.setText(buttonTitles[storyPoint]);
    }

    @Override
    public void onClick(View v) {
        if (++currentStoryPoint >= storyBits.length) {

            dismiss();
        } else {
            updateDescription(currentStoryPoint);
        }
    }
}
