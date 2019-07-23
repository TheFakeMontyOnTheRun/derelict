package br.odb.gameapp;

/**
 * @author monty
 * 
 */
public interface ApplicationClient {
	void printWarning(String msg);

	void printError(String msg);

	void printVerbose(String msg);

	String requestFilenameForSave();

	String requestFilenameForOpen();

	String getInput(String msg);

	int chooseOption(String question, String[] options);

	FileServerDelegate getFileServer();

	void printNormal(String string);

	void alert(String string);

	void playMedia(String uri, String alt);

	void sendQuit();
}
