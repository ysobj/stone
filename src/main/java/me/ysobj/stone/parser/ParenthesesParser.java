package me.ysobj.stone.parser;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.Token;
import me.ysobj.stone.tokenizer.Tokenizer;

public class ParenthesesParser implements Parser {
	Parser parser;
	
	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		Token token = tokenizer.peek();
		if(token.getType() != Token.TokenType.PAREN_OPEN) {
			throw new ParseException();
		}
		tokenizer.next();
		ASTNode astNode = this.parser.parse(tokenizer);
		token = tokenizer.peek();
		if(token.getType() != Token.TokenType.PAREN_CLOSE) {
			throw new ParseException();
		}
		tokenizer.next();
		return astNode;
	}

	public void setParser(Parser parser) {
		this.parser = parser;
	}

}
