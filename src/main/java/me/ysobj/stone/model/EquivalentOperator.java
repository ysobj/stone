package me.ysobj.stone.model;

public class EquivalentOperator implements Operator {

	@Override
	public Object evaluate(Context context, ASTNode... astnode) {
		Object left = astnode[0].evaluate(context);
		Object right = astnode[1].evaluate(context);
		return left.equals(right) ? Boolean.TRUE : Boolean.FALSE;
	}

	@Override
	public String toString() {
		return "==";
	}

	@Override
	public int order() {
		return 0;
	}

}
