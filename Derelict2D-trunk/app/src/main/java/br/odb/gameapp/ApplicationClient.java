package br.odb.gameapp;

public interface ApplicationClient {

    void printNormal(String string);

    void alert(String string);

    void playMedia(String uri, String alt);

    void sendQuit();
}
