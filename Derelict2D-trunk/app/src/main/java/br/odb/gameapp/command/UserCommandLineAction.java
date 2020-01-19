package br.odb.gameapp.command;


import org.jetbrains.annotations.NotNull;

import br.odb.gameapp.ConsoleApplication;

public abstract class UserCommandLineAction {

	public abstract void run(ConsoleApplication application, String operands) throws Exception;

	@NotNull
	@Override
	public abstract String toString();

	public abstract int requiredOperands();

	protected abstract String getDescription();
}
