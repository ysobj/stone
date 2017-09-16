package me.ysobj.stone.model;

public class OperatorNode extends ASTNode {
	private Operator operator;
	
	public OperatorNode(Token token) {
		this(token,false);
	}

	public OperatorNode(Token token, boolean inVarContext) {
		super(token);
		switch (token.getOriginal()) {
		// "+", "-", "*", "/", "=",
		// "==", "<", ">", "<=", "!", ">=", "!=", "&&", "||"
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
		case "=":
			this.operator = new SubstituteOperator(inVarContext);
			break;
		case "==":
			this.operator = new EquivalentOperator();
			break;
		case "<":
			this.operator = new LessOperator();
			break;
		case ">":
			this.operator = new GreaterOperator();
			break;
		case "<=":
			this.operator = new LessOperator(true);
			break;
		case "!":
			throw new UnsupportedOperationException("! is unsupported.");
		case ">=":
			this.operator = new GreaterOperator(true);
			break;
		case "!=":
			this.operator = new EquivalentOperator(true);
		case "&&":
			throw new UnsupportedOperationException("&& is unsupported.");
		case "||":
			throw new UnsupportedOperationException("|| is unsupported.");
		}
		if(this.operator == null) {
			throw new RuntimeException(token.getOriginal());
		}
	}

	public Operator getOperator() {
		return operator;
	}

	@Override
	public String toString() {
		return this.operator.toString();
	}
}
