package me.ysobj.stone.parser;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.OperatorNode;
import me.ysobj.stone.model.Token;
import me.ysobj.stone.model.Token.TokenType;
import me.ysobj.stone.tokenizer.Tokenizer;

public class OperatorParser implements Parser {
	private String operator;
	private boolean inVarContext;

	public OperatorParser() {
	}

	public OperatorParser(String operator) {
		this.operator = operator;
	}

	public OperatorParser(String operator, boolean inVarContext) {
		this.operator = operator;
		this.inVarContext = inVarContext;
	}

	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		Token token = tokenizer.peek();
		if (token == Token.EOF) {
			throw new ParseException(token);
		}
		if (match(token)) {
			tokenizer.next();
			return new OperatorNode(token, inVarContext);
		}
		throw new ParseException(token);
	}

	protected boolean match(Token token) {
		if (token.getType() == TokenType.OPERATOR) {
			if (operator == null) {
				return true;
			} else {
				return this.operator.equals(token.getOriginal());
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "OPERATOR";
	}

}
