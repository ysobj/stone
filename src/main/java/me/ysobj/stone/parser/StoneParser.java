package me.ysobj.stone.parser;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.Assign;
import me.ysobj.stone.model.Identifier;
import me.ysobj.stone.tokenizer.Tokenizer;

public class StoneParser implements Parser {
	Parser parser;

	public StoneParser() {
		parser = new SequenceParser(new IdentifierParser(), new KeywordParser("="), new NumberParser()) {

			@Override
			protected ASTNode build(ASTNode[] children) {
				return new Assign((Identifier) children[0], children[2]);
			}

		};

	}

	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		return this.parser.parse(tokenizer);
	}

	// factor := NUMBER | STRING | IDENTIFIER
	// expression := factor [OPERATOR factor]
	// assign := IDENTIFIER '=' expression
}
