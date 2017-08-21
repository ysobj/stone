package me.ysobj.stone.parser;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.NumberLiteral;
import me.ysobj.stone.model.Token;
import me.ysobj.stone.model.Token.TokenType;
import me.ysobj.stone.tokenizer.Tokenizer;

public class NumberParser extends KeywordParser {
	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		Token token = tokenizer.peek();
		if (token == Token.EOF) {
			throw new ParseException();
		}
		if (keyword == null && token.getType() == TokenType.NUMBER) {
			tokenizer.next();
			return new NumberLiteral(token);
		}
		throw new ParseException();
	}
	@Override
	public String toString() {
		return keyword != null ? keyword : "NUMBER";
	}
}
