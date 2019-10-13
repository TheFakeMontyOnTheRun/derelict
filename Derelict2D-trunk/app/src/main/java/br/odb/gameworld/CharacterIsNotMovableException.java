package br.odb.gameworld;

public class CharacterIsNotMovableException extends Exception {

    private final String reason;

    public CharacterIsNotMovableException(String string) {
        reason = string;
    }

    @Override
    public String getMessage() {
        return reason;
    }
}
