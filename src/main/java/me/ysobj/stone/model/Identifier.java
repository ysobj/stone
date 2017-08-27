package me.ysobj.stone.model;

import me.ysobj.stone.exception.VariableNotFoundException;

public class Identifier extends ASTNode {
	private Token token;

	public Identifier(Token t) {
		token = t;
	}

	@Override
	public Object evaluate(Context context) {
		String name = token.getOriginal();
		if (!context.has(name)) {
			throw new VariableNotFoundException(name);
		}
		return context.get(name);
	}

	public String getName() {
		return token.getOriginal();
	}
}
