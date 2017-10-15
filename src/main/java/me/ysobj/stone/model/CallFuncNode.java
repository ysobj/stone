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
		FuncNode func = (FuncNode) context.get(identifier.getName());
		if (func.getContext() != null) {
			// is closure
			((NestedContext) func.getContext()).setOuter(context);
			context = func.getContext();
		}
		NestedContext nestedContext = new NestedContext(context);
		Identifier[] ids = null;
		if (func.getParamList() != null) {
			ids = func.getParamList().getNodes();
		}
		setupContext(nestedContext, ids, args.nodes);
		return func.getBlock().evaluate(nestedContext);
	}

	protected void setupContext(NestedContext context, Identifier[] ids, ASTNode[] args) {
		for (int i = 0; i < args.length; i++) {
			ASTNode arg = args[i];
			Identifier identifier = ids[i];
			context.putNew(identifier.getName(), arg.evaluate(context));
		}
	}

	@Override
	public String toString() {
		return identifier + "(" + args + ")";
	}

}
