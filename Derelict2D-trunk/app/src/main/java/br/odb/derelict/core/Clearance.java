package br.odb.derelict.core;

public enum Clearance {

	DEFAULT_CLEARANCE("default-clearance"),
	LOW_RANK("low-rank"),
	HIGH_RANK("high-rank"),
	LAB_RANK("root-access");

	final private String prettyName;

	Clearance(String name) {
		prettyName = name;
	}

	@Override
	public String toString() {
		return prettyName;
	}
}