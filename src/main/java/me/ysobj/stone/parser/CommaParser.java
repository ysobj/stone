package me.ysobj.stone.parser;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.Token;
import me.ysobj.stone.tokenizer.Tokenizer;

public class CommaParser implements Parser {
	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		Token token = tokenizer.peek();
		if (token.getType() == Token.TokenType.COMMA) {
			tokenizer.next();
			return new ASTNode(token);
		}
		throw new ParseException(token);
	}

	@Override
	public String toString() {
		return ",";
	}

}
