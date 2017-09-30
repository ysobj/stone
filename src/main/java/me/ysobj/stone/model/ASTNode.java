package me.ysobj.stone.model;

public class ASTNode {
	Token token;

	public ASTNode() {

	}

	public ASTNode(Token t) {
		this.token = t;
	}

	public Object evaluate(Context context) {
		return token.getOriginal();
	}

	@Override
	public String toString() {
		if (token != null) {
			return token.toString();
		}
		return "";
	}

	public Token getToken() {
		return token;
	}

}
