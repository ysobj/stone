package me.ysobj.stone.model;

public class NumberLiteral extends ASTNode {

	public NumberLiteral(Token token) {
		super(token);
	}

	@Override
	public String toString() {
		return token.getOriginal();
	}

	@Override
	public Object evaluate(Context context) {
		return Long.parseLong(token.getOriginal());
	}

}
