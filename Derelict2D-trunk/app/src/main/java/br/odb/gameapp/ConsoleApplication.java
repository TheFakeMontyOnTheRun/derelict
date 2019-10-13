package br.odb.gameapp;

import java.util.HashMap;

import br.odb.gameapp.command.UserCommandLineAction;

public abstract class ConsoleApplication {

    private final HashMap<String, UserCommandLineAction> commands = new HashMap<>();
    public boolean continueRunning;
    private ApplicationClient client;

    protected HashMap<String, UserCommandLineAction> getCommandList() {
        return commands;
    }

    public void setApplicationClient(
            final ApplicationClient client) {
        this.client = client;
    }

    public ApplicationClient getClient() {
        return client;
    }

    public ConsoleApplication init() {
        return this;
    }

    public void onDataEntered(final String data) {
    }

    private String sanitize(String input) {
        return input.replace('"', ' ').replace("'", "");
    }

    public void sendData(String data) {
        if (data != null && data.length() > 0) {
            data = sanitize(data);
            onDataEntered(data);
        }
    }

    public UserCommandLineAction[] getAvailableCommands() {
        return commands.values().toArray(new UserCommandLineAction[]{});
    }

}
