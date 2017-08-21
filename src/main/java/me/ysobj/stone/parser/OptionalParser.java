package me.ysobj.stone.parser;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.tokenizer.Tokenizer;

public class OptionalParser implements Parser {
	private Parser parser;

	public OptionalParser(Parser parser) {
		this.parser = parser;
	}

	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		try {
			return parser.parse(tokenizer);
		} catch (ParseException e) {
		}
		return null;
	}

	@Override
	public String toString() {
		return "[" + parser.toString() + "]";
	}

}
