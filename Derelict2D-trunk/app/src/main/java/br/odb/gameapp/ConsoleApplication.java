package br.odb.gameapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.odb.gameapp.command.UserCommandLineAction;

public abstract class ConsoleApplication {

    private final HashMap<String, UserCommandLineAction> commands = new HashMap<>();
    private final ArrayList<String> cmdHistory = new ArrayList<>();
    public boolean continueRunning;
    private ApplicationClient client;
    private String appName;
    private String authorName;
    private String licenseName;
    private int yearRelease;
    private boolean saveInHistory = true;

    protected HashMap<String, UserCommandLineAction> getCommandList() {
        return commands;
    }

    public void setApplicationClient(
            final ApplicationClient client) {
        this.client = client;
    }

    public void printPreamble() {
        if (client != null) {
            client.printNormal(getApplicationName() + " - " + getAuthor()
                    + " - copyright " + getYearRelease() + " - licensed under "
                    + getLicenseName() + ". ");
        }

    }

    private int getYearRelease() {
        return yearRelease;
    }

    public ConsoleApplication setReleaseYear(final int year) {
        this.yearRelease = year;
        return this;
    }

    private String getApplicationName() {

        return appName;
    }

    public ApplicationClient getClient() {
        return client;
    }

    public ConsoleApplication setAppName(final String appName) {
        this.appName = sanitize(appName);
        return this;
    }

    public ConsoleApplication setAuthorName(final String authorName) {
        this.authorName = sanitize(authorName);
        return this;
    }

    public ConsoleApplication init() {
        return this;
    }

    protected void setShouldSaveHistory() {
        this.saveInHistory = false;
    }

    public void onDataEntered(final String data) {
        if (saveInHistory) {

            cmdHistory.add(data);
        }
    }

    private String sanitize(String input) {
        return input.replace('"', ' ').replace("'", "");
    }

    public ConsoleApplication showUI() {
        return this;
    }

    private String getAuthor() {
        return authorName;
    }

    private String getLicenseName() {
        return licenseName;
    }

    public ConsoleApplication setLicenseName(final String licenseName) {
        this.licenseName = sanitize(licenseName);
        return this;
    }

    public void sendData(String data) {
        if (data != null && data.length() > 0) {
            data = sanitize(data);
            onDataEntered(data);
        }
        showUI();
    }

    public UserCommandLineAction[] getAvailableCommands() {
        return commands.values().toArray(new UserCommandLineAction[]{});
    }

}
