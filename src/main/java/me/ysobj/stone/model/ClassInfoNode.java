package me.ysobj.stone.model;

public class ClassInfoNode extends ASTNode {
	private Identifier identifier;
	private ASTNode classBody;

	public ClassInfoNode(Identifier identifier, ASTNode classBody) {
		super();
		this.identifier = identifier;
		this.classBody = classBody;
	}

	@Override
	public String toString() {
		return String.format("class %s{%s}", identifier.getName(), classBody.toString());
	}

	@Override
	public Object evaluate(Context context) {
		context.put(identifier.getName(), this);
		return Void.VOID;
	}

}
