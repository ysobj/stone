package me.ysobj.stone.parser;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.Token;
import me.ysobj.stone.tokenizer.Tokenizer;

public class TerminatorParser implements Parser {

	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		Token token = tokenizer.peek();
		if (token.getType() == Token.TokenType.TERMINATOR) {
			tokenizer.next();
			return null;
		}
		throw new ParseException();
	}

	@Override
	public String toString() {
		return "TERMINATOR";
	}

}
