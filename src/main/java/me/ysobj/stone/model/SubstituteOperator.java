package me.ysobj.stone.model;

public class SubstituteOperator implements Operator {

	@Override
	public Object evaluate(Context context, ASTNode... astnode) {
		Identifier identifier = (Identifier) astnode[0];
		context.put(identifier.getName(), astnode[1].evaluate(context));
		return Void.VOID;
	}

	@Override
	public String toString() {
		return "=";
	}

	@Override
	public int order() {
		return 0;
	}

}
