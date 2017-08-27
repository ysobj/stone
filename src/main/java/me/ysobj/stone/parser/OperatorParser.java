package me.ysobj.stone.parser;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.Token;
import me.ysobj.stone.model.Token.TokenType;
import me.ysobj.stone.tokenizer.Tokenizer;

public class OperatorParser implements Parser {
	public OperatorParser() {
	}

	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		Token token = tokenizer.peek();
		if (token == Token.EOF) {
			throw new ParseException();
		}
		if (token.getType() == TokenType.OPERATOR) {
			tokenizer.next();
			return new ASTNode(token);
		}
		throw new ParseException();
	}

	@Override
	public String toString() {
		return "OPERATOR";
	}

}
