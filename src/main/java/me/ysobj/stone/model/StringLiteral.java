package me.ysobj.stone.model;

public class StringLiteral extends ASTNode {

	public StringLiteral(Token token) {
		super(token);
	}

	@Override
	public String toString() {
		return token.getOriginal();
	}

	@Override
	public Object evaluate(Context context) {
		String org = token.getOriginal();
		return org.substring(1, org.length() - 1);
	}

}
