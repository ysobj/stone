package me.ysobj.stone.model;

public class IfNode extends ASTNode {
	private ASTNode condition;
	private ASTNode ifBlock;
	public IfNode(ASTNode condition, ASTNode ifBlock) {
		super();
		this.condition = condition;
		this.ifBlock = ifBlock;
	}
	@Override
	public Object evaluate(Context context) {
		Object cond = condition.evaluate(context);
		if(((Boolean)cond).booleanValue()) {
			return ifBlock.evaluate(context);
		}
		return Void.VOID;
	}
	
}
