package br.odb.gameapp.command;


import br.odb.gameapp.ConsoleApplication;

public abstract class UserCommandLineAction {
	
	public class InvalidCommandRunException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5986983118464310069L;
		
	}
	
	public abstract void run(ConsoleApplication application, String operands) throws Exception;

	@Override
	public abstract String toString();

    public abstract int requiredOperands();

	public String getHelp() {

		return ">" + toString() + " : " + getDescription() + " " + "" + "(" + requiredOperands() + ");";
	}

	protected abstract String getDescription();
}
