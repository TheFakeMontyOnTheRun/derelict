package br.odb.derelict1d;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.odb.derelict.core.DerelictGame;
import br.odb.derelict2d.Derelict2DApplication;
import br.odb.derelict2d.R;
import br.odb.gameapp.ApplicationClient;
import br.odb.gameapp.ConsoleApplication;
import br.odb.utils.FileServerDelegate;

public class Derelict1DActivity extends Activity implements ApplicationClient,
		OnClickListener, TextToSpeech.OnInitListener {

	public TextToSpeech tts;

	EditText edtOutput;
	EditText edtEntry;
	Button btnSend;
	DerelictGame app;
	String buffer = "";

	private Button btnListen;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_derelict1_d);

		btnSend = (Button) findViewById(R.id.btnSend);
		btnListen = (Button) findViewById(R.id.btnListen);
		btnListen.setOnClickListener( this );
		edtOutput = (EditText) findViewById(R.id.edtOutput);
		edtEntry = (EditText) findViewById(R.id.edtEntry);
		edtEntry.setFocusableInTouchMode(true);
		edtEntry.requestFocus();

		edtEntry.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {

					Derelict1DActivity.this.onClick(null);

					return true;
				}
				return false;
			}
		});

		btnSend.setOnClickListener(this);

		app = ((Derelict2DApplication) getApplication()).game;
		app.setApplicationClient(this).printPreamble().showUI();

		tts = new TextToSpeech(this, this);
	}

	// public class Derelict1DMainApp extends ConsoleApplication {
	//
	// TotautisSpaceStation station;
	// DerelictGameSession session;
	// Astronaut hero;
	// HashMap<String, UserCommandLineAction> commands;
	//
	// /**
	// * @param args
	// */
	// public Derelict1DMainApp( String[] args ) {
	// super();
	// }
	//
	// public ConsoleApplication init() {
	//
	// commands = new HashMap<String, UserCommandLineAction>();
	// commands.put("move", new MoveCommand());
	// commands.put("toggle", new ToggleCommand());
	// commands.put("use", new UseCommand());
	// commands.put("usewith", new UseWithCommand());
	// commands.put("items", new ItemsCommand());
	// commands.put("pick", new PickCommand());
	// commands.put("drop", new DropCommand());
	//
	// session = new DerelictGameSession();
	// station = (TotautisSpaceStation) session.getCurrentLevel();
	// hero = ((Astronaut) station.getCharacter("hero"));
	// hero.setGender(getClient().getInput("Please specify a gender?"));
	//
	// return super.init();
	// }
	//
	// public ConsoleApplication showUI() {
	//
	// getClient().printNormal("\n" + hero.toString());
	//
	// return super.showUI();
	// }
	//
	//
	//
	// public void onDataEntered(String entry) {
	//
	// entry = entry.toLowerCase();
	// String[] tokens = Utils.tokenize(entry.trim(), " ");
	// String operator = tokens[0];
	// String operand = entry = entry.replace(operator, "").trim();
	//
	// try {
	//
	// UserCommandLineAction cmd = commands.get(tokens[0]);
	//
	// if (cmd != null) {
	// cmd.run(tokens, station, hero, operator, operand, getClient());
	// }
	// } catch (CharacterIsNotMovableException e) {
	// getClient().alert("You can't seem to move!");
	// } catch (InvalidLocationException e) {
	// getClient().alert("You can't move there!");
	// } catch (InvalidSlotException e) {
	// getClient().alert("You can't move in that direction!");
	// } catch (ClearanceException e) {
	// getClient().alert("You are not allowed to move there.");
	// } catch (InventoryManipulationException e) {
	// getClient().alert("You can't do that.");
	// } catch (ItemNotFoundException e) {
	// getClient().alert("Item not found.");
	// } catch (ItemActionNotSupportedException e) {
	// getClient()
	// .alert("This item does not support this kind of action.");
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	//
	// @Override
	// public UserCommandLineAction[] getAvailableCommands() {
	//
	// return commands.keySet().toArray( new UserCommandLineAction[]{});
	// }
	// }

	@Override
	public void setClientId(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void printWarning(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void printError(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void printVerbose(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public String requestFilenameForSave() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String requestFilenameForOpen() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInput(String msg) {
		String toReturn = edtEntry.getText().toString();
		edtEntry.setText("");
		return toReturn;
	}

	@Override
	public int chooseOption(String question, String[] options) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FileServerDelegate getFileServer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void printNormal(String string) {

		edtOutput.append("\n---------------\n" + string);
//		if ( tts != null ) {
//			
//			tts.speak( string, TextToSpeech.QUEUE_FLUSH, null);
//		}
	}

	@Override
	public void alert(String string) {
		printNormal("*" + string + "*");
	}

	@Override
	public void onClick(View v) {
		String data = edtEntry.getText().toString();

		switch (v.getId()) {

		case R.id.btnSend:
			edtEntry.setText("");
			app.sendData(data);
			break;
		case R.id.btnListen:
//			tts.speak(data, TextToSpeech.QUEUE_FLUSH, null);
			tts.speak( edtOutput.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		if ( tts != null ) {
			tts.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public void onInit(int code) {
		if (code == TextToSpeech.SUCCESS) {
			tts.setLanguage(Locale.getDefault());

		} else {
			tts = null;
			Toast.makeText(this, "Failed to initialize TTS engine.",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void playMedia(String uri, String alt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendQuit() {
		finish();
		
	}

	@Override
	public boolean isConnected() {
		
		return true;
	}

	@Override
	public String openHTTP(String url) {

		return ConsoleApplication.defaultJavaHTTPGet( url, this );
	}

	@Override
	public void shortPause() {
		// TODO Auto-generated method stub
		
	}
}