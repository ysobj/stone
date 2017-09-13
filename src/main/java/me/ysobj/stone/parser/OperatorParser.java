package me.ysobj.stone.parser;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.OperatorNode;
import me.ysobj.stone.model.Token;
import me.ysobj.stone.model.Token.TokenType;
import me.ysobj.stone.tokenizer.Tokenizer;

public class OperatorParser implements Parser {
	private String operator;

	public OperatorParser() {
	}

	public OperatorParser(String operator) {
		this.operator = operator;
	}

	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		Token token = tokenizer.peek();
		if (token == Token.EOF) {
			throw new ParseException();
		}
		if (match(token)) {
			tokenizer.next();
			return new OperatorNode(token);
		}
		throw new ParseException();
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
