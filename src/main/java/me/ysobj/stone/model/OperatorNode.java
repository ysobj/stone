package me.ysobj.stone.model;

public class OperatorNode extends ASTNode {
	private Operator operator;

	public OperatorNode(Token token) {
		super(token);
		switch (token.getOriginal()) {
		case "+":
			this.operator = new PlusOperator();
			break;
		case "-":
			this.operator = new MinusOperator();
			break;
		case "*":
			this.operator = new MultiplyOperator();
			break;
		case "/":
			this.operator = new DivideOperator();
			break;
		}
	}

	public Operator getOperator() {
		return operator;
	}
	
}
