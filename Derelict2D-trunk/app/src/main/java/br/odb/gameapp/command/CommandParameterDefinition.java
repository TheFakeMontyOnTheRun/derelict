package br.odb.gameapp.command;

/**
 * Created by monty on 12/03/16.
 */
public abstract class CommandParameterDefinition<T> {
	private final String name;
	private final String description;

	public CommandParameterDefinition( String name, String description ) {
		this.name = name;
		this.description = description;
	}

	String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return name;
	}

	public abstract T obtainFromArguments(String rawArguments, ApplicationData data );
}
