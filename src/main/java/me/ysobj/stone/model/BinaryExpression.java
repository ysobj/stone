package me.ysobj.stone.model;

public class BinaryExpression extends ASTNode {
	private ASTNode left;
	private ASTNode right;
	private Operator operator;
	public BinaryExpression(ASTNode l,Operator o,ASTNode r) {
		this.left = l;
		this.operator = o;
		this.right = r;
	}
	@Override
	public Object evaluate(Context context) {
		return operator.evaluate(context, left, right);
	}

}
