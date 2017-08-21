package me.ysobj.stone.parser;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.Token;
import me.ysobj.stone.tokenizer.Tokenizer;

public class KeywordParser implements Parser {
	protected String keyword;

	public KeywordParser(String keyword) {
		this.keyword = keyword;
	}

	public KeywordParser() {
	}

	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		Token token = tokenizer.peek();
		if (token == Token.EOF) {
			throw new ParseException();
		}
		if (keyword == null || keyword.equals(token.getOriginal())) {
			tokenizer.next();
			return new ASTNode();
		}
		throw new ParseException();
	}

	@Override
	public String toString() {
		return keyword != null ? keyword : "KEYWORD";
	}

}
