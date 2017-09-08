package me.ysobj.stone.model;

public class EquivalentOperator implements Operator {
	private boolean not;

	public EquivalentOperator() {
		this(false);
	}

	public EquivalentOperator(boolean not) {
		super();
		this.not = not;
	}

	@Override
	public Object evaluate(Context context, ASTNode... astnode) {
		Object left = astnode[0].evaluate(context);
		Object right = astnode[1].evaluate(context);
		if(not) {
			return left.equals(right) ? Boolean.FALSE : Boolean.TRUE;
		}
		return left.equals(right) ? Boolean.TRUE : Boolean.FALSE;
	}

	@Override
	public String toString() {
		if (not) {
			return "!=";
		}
		return "==";
	}

	@Override
	public int order() {
		return 0;
	}

}
