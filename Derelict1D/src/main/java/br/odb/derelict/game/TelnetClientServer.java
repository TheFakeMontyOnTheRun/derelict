package br.odb.derelict.game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import br.odb.gameapp.ApplicationClient;
import br.odb.utils.FileServerDelegate;

public class TelnetClientServer implements ApplicationClient {

	ServerSocket server;
	Socket socket;
	DataOutputStream out;
	DataInputStream din;

	TelnetClientServer() {

		try {
			server = new ServerSocket(1337);
			socket = server.accept();
			System.out.println("Client Connected!");
			out = new DataOutputStream(socket.getOutputStream());
			din = new DataInputStream(socket.getInputStream());
			out.writeUTF("connected");
		} catch (IOException e) {
			// TODO There must be some way out.
		}
	}

	//
	// finalise() {
	// out.close();// clos out
	// skt.close();// close skt
	// srvr.close();// close srvr
	// din.close(); // close din
	// }

	@SuppressWarnings("deprecation")
	public String getInput(String msg) {
		try {
			return din.readLine();
		} catch (IOException e) {
			// TODO find better way to do this.
			e.printStackTrace();
		}

		return "";
	}

	public void printNormal(String string) {
		System.out.println("sending " + string);
		try {
			out.writeUTF(string);
		} catch (IOException irrelevantSinceCantPrintAnyway) {			
		}
	}

	public void setClientId(String id) {
	}

	public void printWarning(String msg) {
		printNormal("!" + msg + "!");
	}

	public void printError(String msg) {
		printNormal("##" + msg + "##");
	}

	public void printVerbose(String msg) {
		printNormal(">" + msg + "< ");
	}

	public String requestFilenameForSave() {
		// TODO implement proper questioning? Is it applicable?
		return null;
	}

	public String requestFilenameForOpen() {
		// TODO implement proper questioning. Probably the same from ConsoleClient;
		return null;
	}

	public int chooseOption(String question, String[] options) {
		// TODO implement proper selection. Probably the same from ConsoleClient;
		return 0;
	}

	public FileServerDelegate getFileServer() {
		// TODO Is it applicable?
		return null;
	}

	public void alert(String string) {
		printNormal("*" + string + "* ");

	}

	public void playMedia(String uri, String alt) {
		printNormal("media: " + alt + " @ " + uri);

	}

	public void clear() {
	}

	public void sendQuit() {
		try {
			out.close();
		} catch (IOException e) {
			// TODO What happens in this case?
			e.printStackTrace();
		}
		out = null;
		System.out.println();
	}

	public boolean isConnected() {
		return out != null;
	}

	@Override
	public String openHTTP(String url) {
		return "";
	}

	@Override
	public void shortPause() {
		// TODO Auto-generated method stub
		
	}
}
