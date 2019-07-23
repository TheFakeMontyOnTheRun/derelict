package br.odb.gameworld;

/**
 * @author monty
 * 
 */
public class CharacterIsNotMovableException extends Exception {

	private final String reason;
	
	public CharacterIsNotMovableException(String string) {
		
		reason = string;
	}
	
	@Override
	public String getMessage() {
	
		return reason;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5934779209339680717L;

}
