package me.ysobj.stone.model;

public class MultiplyOperator extends PlusOperator {

	@Override
	protected Number calculate(Number a, Number b) {
		return a.longValue() * b.longValue();
	}

	@Override
	public String toString() {
		return "*";
	}

	@Override
	public int order() {
		return 2;
	}

}
