package br.odb.gameapp;

public interface ApplicationClient {

    void printVerbose(String msg);

    int chooseOption(String question, String[] options);

    void printNormal(String string);

    void alert(String string);

    void playMedia(String uri, String alt);

    void sendQuit();
}
