package me.ysobj.stone.exception;

public class VariableAlreadyDefinedException extends RuntimeException {
	private String name;

	public VariableAlreadyDefinedException(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("Variable %s is already defined.", this.name);
	}
}
