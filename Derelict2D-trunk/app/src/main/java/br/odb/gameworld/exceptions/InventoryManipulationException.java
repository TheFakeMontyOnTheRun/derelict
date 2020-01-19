package br.odb.gameworld.exceptions;

import org.jetbrains.annotations.NotNull;

public class InventoryManipulationException extends Exception {

	private final String message;

	public InventoryManipulationException(String msg) {
		this.message = msg;
	}

	@NotNull
	@Override
	public String toString() {
		return message;
	}
}
