package me.ysobj.stone.exception;

import me.ysobj.stone.model.Token;

public class VariableNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private Token token;
	private String name;

	public VariableNotFoundException(Token token) {
		this.token = token;
	}

	public VariableNotFoundException(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("Variable %s is not found.", this.name);
	}

}
