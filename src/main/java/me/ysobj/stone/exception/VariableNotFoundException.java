package me.ysobj.stone.exception;

public class VariableNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String name;

	public VariableNotFoundException(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("Variable %s is not found.", this.name);
	}

}
