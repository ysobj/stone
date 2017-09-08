package me.ysobj.stone.model;

public class GreaterOperator implements Operator {
	private boolean includeEqual;

	public GreaterOperator() {
		this(false);
	}

	public GreaterOperator(boolean includeEqual) {
		super();
		this.includeEqual = includeEqual;
	}

	@Override
	public Object evaluate(Context context, ASTNode... astnode) {
		long left = ((Number) astnode[0].evaluate(context)).longValue();
		long right = ((Number) astnode[1].evaluate(context)).longValue();
		if (includeEqual) {
			return left >= right;
		}
		return left > right;
	}

	@Override
	public String toString() {
		if (includeEqual) {
			return ">=";
		}
		return ">";
	}

	@Override
	public int order() {
		return 1;
	}

}
