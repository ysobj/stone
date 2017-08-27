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
}
