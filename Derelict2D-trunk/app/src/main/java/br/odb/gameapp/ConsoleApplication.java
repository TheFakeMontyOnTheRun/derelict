package br.odb.gameapp;

import br.odb.gameapp.command.UserCommandLineAction;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * @author monty
 */
public abstract class ConsoleApplication {


	//extra concern 1
	public static String extractPathFrom(String filePath) {
		return filePath.substring(0, filePath.lastIndexOf(File.separator) + 1);
	}

	public static String extractFilenameFrom(String filePath) {
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}
	////////////////

	private ApplicationClient client;
	private final HashMap<String, UserCommandLineAction> commands = new HashMap<>();
	private String appName;
	private String authorName;
	private String licenseName;
	private int yearRelease;
	public boolean continueRunning;
	private final ArrayList<String> cmdHistory = new ArrayList<>();
	private boolean saveInHistory = true;


	// another part
	public void runCmd(String entry) throws Exception {

		if (entry == null || entry.length() == 0) {
			return;
		}

		entry = sanitize(entry);

		String[] tokens = entry.trim().split("[ ]+");
		String operator = tokens[0];
		String operand = entry.replace(operator, "").trim();

		UserCommandLineAction cmd = getCommand(tokens[0]);

		if (cmd != null) {

			cmd.run(this, operand);
		}

	}
	/////////////

	//leaky
	private UserCommandLineAction getCommand(String cmdName) {
		return commands.get(cmdName);
	}

	public HashMap<String, UserCommandLineAction> getCommandList() {
		return commands;
	}
////


	//maybe?
	protected void registerCommand(UserCommandLineAction cmd) {
		commands.put(cmd.toString(), cmd);
	}

	//You must get you
	public static class ConsoleClient implements ApplicationClient {

		final Scanner in;
		private final ConsoleApplication consoleApplication;

		public ConsoleClient(ConsoleApplication consoleApplication) {
			in = new Scanner(System.in);
			in.useDelimiter("\n");
			this.consoleApplication = consoleApplication;
		}

		@Override
		public void printWarning(final String msg) {
			printNormal("*WARNING* " + msg);
		}

		@Override
		public void printError(final String msg) {
			printNormal("*ERROR* " + msg);
		}

		@Override
		public void printVerbose(final String msg) {
			printNormal("*...* " + msg);
		}

		@Override
		public String requestFilenameForSave() {

			return getInput("Enter file name to save:");
		}

		@Override
		public String requestFilenameForOpen() {
			printNormal("File for open?");
			return "";
		}

		@Override
		public String getInput(String msg) {
			System.out.println(msg);
			System.out.print(">");
			return in.nextLine();
		}

		@Override
		public int chooseOption(final String question, final String[] options) {

			printNormal(question);

			int optionNum = 1;

			for (String option : options) {
				printNormal(optionNum + ") " + option);
				++optionNum;
			}

			String option = "-1";

			option = getInput("Please select one option [ 1 - "
					+ (options.length) + " ] ?");

			return Integer.parseInt(option);
		}

		@Override
		public FileServerDelegate getFileServer() {
			return null;
		}

		@Override
		public void printNormal(final String msg) {
			System.out.println(msg);
		}

		void destroy() {
			in.close();
		}

		@Override
		public void alert(final String string) {
			printNormal("*" + string + "*");

		}

		@Override
		public void playMedia(final String uri, final String alt) {
			printNormal("#" + alt + "#");

		}

		@Override
		public void sendQuit() {
			consoleApplication.continueRunning = false;
		}

	}

	///////////////////
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

	public ConsoleApplication setLicenseName(final String licenseName) {
		this.licenseName = sanitize(licenseName);
		return this;
	}

	public static String defaultJavaHTTPGet(String url, ApplicationClient client) {

		StringBuilder response = new StringBuilder();
		BufferedReader in = null;
		String inputLine;
		URLConnection conn = null;
		URL myURL = null;

		try {
			myURL = new URL(url);

			conn = myURL.openConnection();
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			while ((inputLine = in.readLine()) != null) {

				response.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {
			client.printError("Something went wrong with your request");
		}

		return response.toString();
	}

	public ConsoleApplication init() {
		return this;
	}

	protected void setShouldSaveHistory(boolean shouldSave) {
		this.saveInHistory = shouldSave;
	}

	public void onDataEntered(final String data) {
		if (saveInHistory) {

			cmdHistory.add(data);
		}
	}

	private String getInputFromClient(String string) {
		return sanitize(getClient().getInput(string));
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

	public void sendData(String data) {
		if (data != null && data.length() > 0) {
			data = sanitize(data);
			onDataEntered(data);
		}
		showUI();
	}

	protected List<String> getCommandHistory() {
		return cmdHistory;
	}

	//maybe?
	public UserCommandLineAction[] getAvailableCommands() {
		return commands.values().toArray(new UserCommandLineAction[]{});
	}

}
