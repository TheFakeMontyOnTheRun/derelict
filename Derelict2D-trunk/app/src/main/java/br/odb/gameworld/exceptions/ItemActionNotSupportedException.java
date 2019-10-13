package br.odb.gameworld.exceptions;

public class ItemActionNotSupportedException extends Exception {

    private final String message;


    public ItemActionNotSupportedException(String msg) {
        this.message = msg;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
