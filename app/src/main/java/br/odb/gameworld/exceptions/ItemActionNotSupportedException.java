package br.odb.gameworld.exceptions;

import org.jetbrains.annotations.NotNull;

public class ItemActionNotSupportedException extends Exception {

	private final String message;


	public ItemActionNotSupportedException(String msg) {
		this.message = msg;
	}

	@NotNull
	@Override
	public String toString() {
		return this.message;
	}
}
