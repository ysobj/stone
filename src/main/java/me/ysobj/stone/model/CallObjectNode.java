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
				obj.getContext().setOuter(context);
				NestedContext nc = new NestedContext(obj.getContext());
				Identifier[] ids = null;
				if (func.getParamList() != null) {
					ids = func.getParamList().getNodes();
				}
				setupContext(nc, ids, args.nodes);
				return func.getBlock().evaluate(nc);
			}
			if(callee instanceof ASTNode) {
				return ((ASTNode)callee).evaluate(context);
			}
			return callee;
		}
		return null;
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
		if(args != null) {
			return String.format("%s.%s(%s)", objectIdentifier.toString(), identifier.toString() ,args);
		}
		return String.format("%s.%s",objectIdentifier.toString() ,identifier.toString()) ;
	}

}
