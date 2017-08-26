package me.ysobj.stone.model;

public class Assign extends ASTNode{
	private Name name;
	private ASTNode right;
	public Assign(Name name, ASTNode right) {
		this.name = name;
		this.right = right;
	}
	@Override
	public Object evaluate(Context context) {
		context.put(name.getName(), right.evaluate(context));
		return Void.VOID;
	}
	
}
