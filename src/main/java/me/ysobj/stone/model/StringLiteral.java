package me.ysobj.stone.model;

public class StringLiteral extends ASTNode {
	private Token token;

	public StringLiteral(Token token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return token.getOriginal();
	}
}
