package me.ysobj.stone.model;

public class BinaryExpression extends ASTNode {
	private ASTNode left;
	private ASTNode right;
	private OperatorNode operatorNode;
	public BinaryExpression(ASTNode l,OperatorNode o,ASTNode r) {
		this.left = l;
		this.operatorNode = o;
		this.right = r;
	}
	@Override
	public Object evaluate(Context context) {
		return operatorNode.getOperator().evaluate(context, left, right);
	}

}
