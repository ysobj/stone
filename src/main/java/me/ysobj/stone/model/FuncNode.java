
package me.ysobj.stone.model;

public class FuncNode extends ASTNode {
	private Identifier identifier;
	private ParamList paramList;
	private ASTNode block;
	private Context context;

	public FuncNode(Identifier identifier, ParamList paramList, ASTNode block) {
		super();
		this.identifier = identifier;
		this.paramList = paramList;
		this.block = block;
	}

	public FuncNode(ParamList paramList, ASTNode block) {
		super();
		this.paramList = paramList;
		this.block = block;
	}

	@Override
	public Object evaluate(Context context) {
		if (identifier != null) {
			if(context instanceof NestedContext) {
				((NestedContext)context).putNew(identifier.getName(), this);
			}else {
			context.put(identifier.getName(), this);
			}
		}else {
			this.context = context;
		}
		return this;
	}

	public ParamList getParamList() {
		return paramList;
	}

	public ASTNode getBlock() {
		return block;
	}

	@Override
	public String toString() {
		if (this.identifier != null) {
			return String.format("func %s( %s ){%s}", this.identifier, this.paramList, this.block);
		}
		return String.format("func( %s ){ %s }", this.paramList, this.block);
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

}
