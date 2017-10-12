package me.ysobj.stone.model;

public class CallObjectNode extends ASTNode {
	private Identifier objectIdentifier;
	private Identifier identifier;
	private ASTNodeList args;

	public CallObjectNode(Identifier objectIdentifier, Identifier identifier) {
		this.objectIdentifier = objectIdentifier;
		this.identifier = identifier;
	}

	public CallObjectNode(Identifier objectIdentifier, Identifier identifier, ASTNodeList args) {
		this.objectIdentifier = objectIdentifier;
		this.identifier = identifier;
		this.args = args;
	}

	@Override
	public Object evaluate(Context context) {
		Object tmp = context.get(objectIdentifier.getName());
		if (tmp instanceof ClassInfoNode) {
			ClassInfoNode clazz = (ClassInfoNode) tmp;
			if(identifier.getName().equals("new")) {
				NestedContext nc = new NestedContext();
				return new StoneObject(clazz, nc);
			}
			throw new IllegalArgumentException();
		}
		if (tmp instanceof StoneObject) {
			StoneObject obj = (StoneObject)tmp;
			Object callee = obj.get(identifier.getName());
			if(callee instanceof FuncNode) {
				FuncNode func = (FuncNode)callee;
				return func.evaluate(context);
			}
			if(callee instanceof ASTNode) {
				return ((ASTNode)callee).evaluate(context);
			}
			throw new IllegalArgumentException();
		}
		return null;
		// FuncNode func = (FuncNode) context.get(objectIdentifier.getName());
		// Identifier[] ids = null;
		// if (func.getParamList() != null) {
		// ids = func.getParamList().getNodes();
		// }
		// ASTNode[] argArray = args.nodes;
		// if (func.getContext() != null) {
		// ((NestedContext) func.getContext()).setOuter(context);
		// context = func.getContext();
		// }
		// NestedContext nestedContext = new NestedContext(context);
		// for (int i = 0; i < argArray.length; i++) {
		// ASTNode arg = argArray[i];
		// Identifier identifier = ids[i];
		// nestedContext.putNew(identifier.getName(), arg.evaluate(context));
		// }
		// return func.getBlock().evaluate(nestedContext);
	}

	@Override
	public String toString() {
		return identifier + "(" + args + ")";
	}

}
