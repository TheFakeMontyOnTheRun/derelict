package br.odb.gameutils;

import org.jetbrains.annotations.NotNull;

public enum Direction {

	N("N", "North"), E("E", "East"), S("S", "South"), W("W", "West"), FLOOR(
			"D", "Down"), CEILING("U", "Up");

	final public String prettyName;
	public final String simpleName;

	Direction(String simpleName, String name) {
		prettyName = name;
		this.simpleName = simpleName;
	}

	public static Direction getDirectionForPrettyName(String prettyName) {

		if (prettyName == null || prettyName.length() == 0) {
			return null;
		}

		for (Direction d : Direction.values()) {
			if (d.prettyName.toUpperCase().equals(prettyName.toUpperCase())) {
				return d;
			}
		}

		return null;
	}

	public static Direction getDirectionForSimpleName(String simpleName) {

		if (simpleName == null || simpleName.length() == 0) {
			return null;
		}

		for (Direction d : Direction.values()) {
			if (d.simpleName.equals(simpleName.toUpperCase())) {
				return d;
			}
		}

		return null;
	}

	@NotNull
	@Override
	public String toString() {
		return prettyName;
	}
}