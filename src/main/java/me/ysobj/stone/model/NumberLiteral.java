package me.ysobj.stone.model;

public class NumberLiteral extends ASTNode {
	private Token token;

	public NumberLiteral(Token token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return token.getOriginal();
	}

}
