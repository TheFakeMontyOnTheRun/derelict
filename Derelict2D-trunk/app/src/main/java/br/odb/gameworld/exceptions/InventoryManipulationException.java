package br.odb.gameworld.exceptions;

public class InventoryManipulationException extends Exception {

    private final String message;

    public InventoryManipulationException(String msg) {
        this.message = msg;
    }

    @Override
    public String toString() {
        return message;
    }
}
