package me.ysobj.stone.model;

public class CallFuncNode extends ASTNode {
	private Identifier identifier;
	private ASTNodeList args;

	public CallFuncNode(Identifier identifier, ASTNodeList args) {
		this.identifier = identifier;
		this.args = args;
	}

	@Override
	public Object evaluate(Context context) {
		FuncNode func = (FuncNode)context.get(identifier.getName());
		Identifier[] ids = func.getParamList().getNodes();
		ASTNode[] argArray = args.nodes;
		NestedContext nestedContext = new NestedContext(context);
		for (int i = 0; i < argArray.length; i++) {
			ASTNode arg = argArray[i];
			Identifier identifier = ids[i];
			nestedContext.putNew(identifier.getName(), arg.evaluate(context));
		}
		return func.getBlock().evaluate(nestedContext);
	}

}
