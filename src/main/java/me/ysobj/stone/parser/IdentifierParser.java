package me.ysobj.stone.parser;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.Identifier;
import me.ysobj.stone.model.Token;
import me.ysobj.stone.model.Token.TokenType;
import me.ysobj.stone.tokenizer.Tokenizer;

public class IdentifierParser implements Parser {

	public IdentifierParser() {
	}

	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		Token token = tokenizer.peek();
		if (token == Token.EOF) {
			throw new ParseException(token);
		}
		if (token.getType() == TokenType.IDENTIFIER) {
			tokenizer.next();
			return new Identifier(token);
		}
		throw new ParseException(token);
	}

	@Override
	public String toString() {
		return "IDENTIFIER";
	}

}
