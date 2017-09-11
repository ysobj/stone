package me.ysobj.stone.model;

public class CallFuncNode extends ASTNode {
	private Identifier identifier;

	public CallFuncNode(Identifier identifier) {
		this.identifier = identifier;
	}

	@Override
	public Object evaluate(Context context) {
		return super.evaluate(context);
	}

}
