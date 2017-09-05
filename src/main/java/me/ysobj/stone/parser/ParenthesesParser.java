package me.ysobj.stone.parser;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.Token;
import me.ysobj.stone.model.Token.TokenType;
import me.ysobj.stone.tokenizer.Tokenizer;

public class ParenthesesParser implements Parser {
	public static enum BracketType {
		PARENTHESIS, BRACKET
	}

	Parser parser;
	TokenType open;
	TokenType close;

	public ParenthesesParser(BracketType type) {
		if (type == BracketType.PARENTHESIS) {
			open = TokenType.PAREN_OPEN;
			close = TokenType.PAREN_CLOSE;
		} else {
			open = TokenType.BRACE_OPEN;
			close = TokenType.BRACE_CLOSE;
		}
	}

	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		Token token = tokenizer.peek();
		if (token.getType() != open) {
			throw new ParseException();
		}
		tokenizer.next();
		ASTNode astNode = this.parser.parse(tokenizer);
		token = tokenizer.peek();
		if (token.getType() != close) {
			throw new ParseException();
		}
		tokenizer.next();
		return astNode;
	}

	public void setParser(Parser parser) {
		this.parser = parser;
	}

}
