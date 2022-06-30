package br.odb.gameapp;

public interface ApplicationClient {

	void alert(String string);

	void playMedia(String uri, String alt);

	void sendQuit();

	void update();
}
