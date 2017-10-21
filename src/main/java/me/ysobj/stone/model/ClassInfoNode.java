package me.ysobj.stone.model;

public class ClassInfoNode extends ASTNode {
	private Identifier identifier;
	private Identifier superClassIdentifier;
	private ASTNode classBody;
	private ClassInfoNode superClass;

	public ClassInfoNode(Identifier identifier, ASTNode classBody) {
		super();
		this.identifier = identifier;
		this.classBody = classBody;
	}

	public ClassInfoNode(Identifier identifier, Identifier superClass, ASTNode classBody) {
		super();
		this.identifier = identifier;
		this.classBody = classBody;
		this.superClassIdentifier = superClass;
	}

	@Override
	public String toString() {
		if (this.superClassIdentifier != null) {
			return String.format("class %s extends %s{%s}", identifier.getName(), superClassIdentifier.getName(),
					classBody.toString());
		}
		return String.format("class %s{%s}", identifier.getName(), classBody.toString());
	}

	@Override
	public Object evaluate(Context context) {
		if (superClassIdentifier != null) {
			this.superClass = (ClassInfoNode) context.get(superClassIdentifier.getName());
		}
		if (context instanceof NestedContext) {
			((NestedContext) context).putNew(identifier.getName(), this);
		} else {
			context.put(identifier.getName(), this);
		}
		return Void.VOID;
	}

	public ASTNode getClassBody() {
		return this.classBody;
	}
	
	public ClassInfoNode getSuperClass() {
		return this.superClass;
	}
}
