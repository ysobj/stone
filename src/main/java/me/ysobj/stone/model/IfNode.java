package me.ysobj.stone.model;

public class IfNode extends ASTNode {
	private ASTNode condition;
	private ASTNode ifBlock;
	private ASTNode elseBlock;

	public IfNode(ASTNode condition, ASTNode ifBlock, ASTNode elseBlock) {
		super();
		this.condition = condition;
		this.ifBlock = ifBlock;
		this.elseBlock = elseBlock;
	}

	public IfNode(ASTNode condition, ASTNode ifBlock) {
		this(condition, ifBlock, null);
	}

	@Override
	public Object evaluate(Context context) {
		Object cond = condition.evaluate(context);
		if (((Boolean) cond).booleanValue()) {
			return ifBlock.evaluate(context);
		} else if (elseBlock != null) {
			return elseBlock.evaluate(context);
		}
		return Void.VOID;
	}

	@Override
	public String toString() {
		String ret = "if (" + condition + "){" + ifBlock + "}";
		if (elseBlock == null) {
			return ret;
		}
		return ret + "else{" + elseBlock + "}";
	}

}
