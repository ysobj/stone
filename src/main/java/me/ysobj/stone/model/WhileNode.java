package me.ysobj.stone.model;

public class WhileNode extends ASTNode {
	private ASTNode condition;
	private ASTNode whileBlock;

	public WhileNode(ASTNode condition, ASTNode whileBlock) {
		super();
		this.condition = condition;
		this.whileBlock = whileBlock;
	}

	@Override
	public Object evaluate(Context context) {
		while (((Boolean) condition.evaluate(context)).booleanValue()) {
			whileBlock.evaluate(context);
		}
		return Void.VOID;
	}

}
