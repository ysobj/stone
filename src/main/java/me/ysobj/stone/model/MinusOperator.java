package me.ysobj.stone.model;

public class MinusOperator extends PlusOperator {

	protected Number calculate(Number a, Number b) {
		return a.longValue() - b.longValue();
	}

	@Override
	public String toString() {
		return "-";
	}
}
