package me.ysobj.stone.parser;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.StringLiteral;
import me.ysobj.stone.model.Token;
import me.ysobj.stone.model.Token.TokenType;
import me.ysobj.stone.tokenizer.Tokenizer;

public class StringParser extends KeywordParser {
	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		Token token = tokenizer.peek();
		if (token == Token.EOF) {
			throw new ParseException(token);
		}
		if (keyword == null && token.getType() == TokenType.STRING) {
			tokenizer.next();
			return new StringLiteral(token);
		}
		throw new ParseException(token);
	}
	@Override
	public String toString() {
		return keyword != null ? keyword : "STRING";
	}
}
