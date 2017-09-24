package me.ysobj.stone.parser;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.ASTNodeList;
import me.ysobj.stone.tokenizer.Tokenizer;

public class OptionalParser implements Parser {
	private Parser parser;

	private boolean returnNullIfCatchException;

	public OptionalParser(Parser parser) {
		this.parser = parser;
		this.returnNullIfCatchException = true;
	}

	public OptionalParser(Parser parser, boolean returnNullIfCatchException) {
		this.parser = parser;
		this.returnNullIfCatchException = returnNullIfCatchException;
	}

	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		try {
			return parser.parse(tokenizer);
		} catch (ParseException e) {
		}
		if (returnNullIfCatchException) {
			return null;
		}
		return new ASTNodeList(0);
	}

	@Override
	public String toString() {
		return "[" + parser.toString() + "]";
	}

}
