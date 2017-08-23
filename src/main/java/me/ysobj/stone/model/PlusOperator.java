package me.ysobj.stone.model;

public class PlusOperator implements Operator {

	@Override
	public Object evaluate(Context context, ASTNode... astnode) {
		ASTNode left = astnode[0];
		ASTNode right = astnode[1];
		if(left == null || right == null) {
			return null;
		}
		Object lvalue = left.evaluate(context);
		Object rvalue = right.evaluate(context);
		if(lvalue == null || rvalue == null) {
			return null;
		}
		if(lvalue instanceof String || rvalue instanceof String) {
			return lvalue.toString() + rvalue.toString();
		}
		if(lvalue instanceof Number && rvalue instanceof Number) {
			return ((Number)lvalue).longValue() + ((Number)rvalue).longValue();
		}
		return null;
	}

}
