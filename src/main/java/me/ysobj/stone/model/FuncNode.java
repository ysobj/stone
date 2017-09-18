
package me.ysobj.stone.model;

public class FuncNode extends ASTNode {
	private Identifier identifier;
	private ParamList paramList;
	private ASTNode block;

	public FuncNode(Identifier identifier, ParamList paramList, ASTNode block) {
		super();
		this.identifier = identifier;
		this.paramList = paramList;
		this.block = block;
	}

	@Override
	public Object evaluate(Context context) {
		context.put(identifier.getName(), this);
		return Void.VOID;
	}

	public ParamList getParamList() {
		return paramList;
	}

	public ASTNode getBlock() {
		return block;
	}

	@Override
	public String toString() {
		return "func " + identifier + "( " + paramList + " ){" + block + "}";
	}

}
