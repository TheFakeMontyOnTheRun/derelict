package br.odb.gameapp.command;


import br.odb.gameapp.ConsoleApplication;

public abstract class UserCommandLineAction {

	public abstract void run(ConsoleApplication application, String operands) throws Exception;

	@Override
	public abstract String toString();

	public abstract int requiredOperands();

	protected abstract String getDescription();
}
