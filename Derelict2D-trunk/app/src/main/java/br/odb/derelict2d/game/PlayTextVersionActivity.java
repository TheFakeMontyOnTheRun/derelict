package br.odb.derelict2d.game;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import br.odb.derelict.core.DerelictGame;
import br.odb.derelict2d.Derelict2DApplication;
import br.odb.derelict2d.R;
import br.odb.derelict2d.RootGameMenuActivity;
import br.odb.gameapp.ApplicationClient;
import br.odb.gameapp.GameUpdateDelegate;
import br.odb.gamerendering.rendering.AssetManager;
import br.odb.utils.FileServerDelegate;

public class PlayTextVersionActivity extends Activity implements View.OnClickListener, ApplicationClient, DerelictGame.EndGameListener, GameUpdateDelegate {

    StringBuilder sb = new StringBuilder( "" );
    EditText outputEdt;
    EditText cmdEdt;
    boolean shouldPlaySound;
    AssetManager resManager;
    MediaPlayer playerSound;
    HashMap<String, MediaPlayer> mediaPlayers = new HashMap<String, MediaPlayer>();
    private DerelictGame game;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resManager = ((Derelict2DApplication) getApplication())
                .getAssetManager();

        game = ((Derelict2DApplication) getApplication()).game;
        game.endGameListener = this;
        game.createDefaultClient();
        game.setGameUpdateDelegate(this);


        Intent intent;
        intent = getIntent();

        String hasSpeech = intent.getExtras().getString("speech");
        String hasSound = intent.getExtras().getString("hasSound");


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE);

        shouldPlaySound = (hasSound != null && hasSound.equals("y"));

        if (shouldPlaySound) {
            playerSound = MediaPlayer.create(this, R.raw.playersounds);
            // music = MediaPlayer.create(this, R.raw.ravelbolero);
        }

        setContentView(R.layout.activity_play_text_version);

        findViewById( R.id.btnSend ).setOnClickListener( this );
        findViewById( R.id.btnClearOutput ).setOnClickListener( this );

        outputEdt = (  (EditText ) findViewById( R.id.edtOutput ) );
        cmdEdt = (  (EditText ) findViewById( R.id.edtCommand ) );

        cmdEdt.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if ( keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER ) {
                    onSend();
                    updateOutput();
                    return true;
                }

                return false;
            }
        });


        clear();
    }

    @Override
    protected void onResume() {
        super.onStart();
        ((Derelict2DApplication) getApplication()).game.setApplicationClient( this );
    }

    @Override
    protected void onPause() {
        super.onStop();
        ((Derelict2DApplication) getApplication()).game.setApplicationClient( null );
    }

    void updateOutput() {
        flush();
    }

    void flush() {
        outputEdt.setText(sb.toString());
        outputEdt.setSelection( outputEdt.getText().length() );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play_text_version, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        switch ( view.getId() ) {
            case R.id.btnClearOutput:
                clear();
                break;

            case R.id.btnSend:
                onSend();
                break;
        }

        updateOutput();
    }

    void onSend() {
        String cmd = cmdEdt.getText().toString();
        cmdEdt.setText( "" );
        ( (Derelict2DApplication)getApplication() ).game.sendData( cmd );
    }

    @Override
    public void setClientId(String s) {

    }

    @Override
    public void clear() {

        if ( sb.length() > 0 ) {
            sb.delete( 0, sb.length() - 1 );
        }

        sb.append( ( (Derelict2DApplication)getApplication() ).game.getTextOutput() );
        flush();
    }

    @Override
    public void printWarning(String s) {
        s = s.substring(0, 1).toUpperCase() + s.substring(1);

        Toast.makeText(this, s.replace('-', ' '), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void printError(String s) {
        sb.append( s );
        flush();
    }

    @Override
    public void printVerbose(String s) {
        sb.append( s );
        flush();
    }

    @Override
    public String requestFilenameForSave() {
        return null;
    }

    @Override
    public String requestFilenameForOpen() {
        return null;
    }

    @Override
    public String getInput(String s) {
        return ( (EditText) findViewById( R.id.edtCommand ) ).getText().toString();
    }

    @Override
    public int chooseOption(String s, String[] strings) {
        return 0;
    }

    @Override
    public FileServerDelegate getFileServer() {
        return null;
    }

    @Override
    public void printNormal(String s) {
        sb.append( "\n" + s );
        flush();
    }

    @Override
    public void alert(String s) {
        s = s.substring(0, 1).toUpperCase() + s.substring(1);

        Toast.makeText(this, s.replace('-', ' '), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void playMedia(String uri, String alt) {
        if (!shouldPlaySound) {
            return;
        }

        MediaPlayer mp;

        if (this.mediaPlayers.containsKey(uri)) {
            mp = mediaPlayers.get(uri);
        } else {
            mp = MediaPlayer.create(this, resManager.getResIdForUri(uri));
            mediaPlayers.put(uri, mp);
        }

        mp.start();
    }

    @Override
    public void sendQuit() {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public String openHTTP(String s) {
        return null;
    }

    @Override
    public void shortPause() {

    }

    @Override
    public void onGameEnd(int i) {

    }

    @Override
    public void update() {

    }
}
