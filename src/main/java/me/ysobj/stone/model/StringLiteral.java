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

	@Override
	public Object evaluate(Context context) {
		String org = token.getOriginal();
		return org.substring(1, org.length() - 1);
	}

}
