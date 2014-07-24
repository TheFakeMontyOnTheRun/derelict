package br.odb.derelict.test;

import br.odb.gameapp.ApplicationClient;
import br.odb.utils.FileServerDelegate;

public class DummyApplicationClient implements ApplicationClient, Runnable {

	public String buffer = "";
	boolean connected = true;

	
	public void setClientId(String id) {
	}

	
	public void printWarning(String msg) {
		buffer += msg;
	}

	
	public void printError(String msg) {
		buffer += msg;
	}

	
	public void printVerbose(String msg) {
		buffer += msg;
	}

	
	public String requestFilenameForSave() {
		return "dummy";
	}

	
	public String requestFilenameForOpen() {
		return "dummy";
	}

	
	public String getInput(String msg) {
		buffer += msg;
		return "dummy";
	}

	
	public int chooseOption(String question, String[] options) {
		buffer += question;
		return 0;
	}

	
	public FileServerDelegate getFileServer() {
		return null;
	}

	
	public void printNormal(String string) {
		buffer += string;
	}

	
	public void alert(String string) {
		buffer += string;
	}

	
	public void run() {
		buffer = "run!";
	}


	public void playMedia(String uri, String alt) {
		buffer = "media:" + uri + "/" + alt;
	}

	public void clear() {
		buffer = "";		
	}

	public void sendQuit() {
		connected = false;		
	}

	public boolean isConnected() {
		return connected ;
	}


	@Override
	public String openHTTP(String url) {
		buffer += "openHTTP: " + url + "\n";
		return "";
	}


	@Override
	public void shortPause() {
		// TODO Auto-generated method stub
		
	}
}