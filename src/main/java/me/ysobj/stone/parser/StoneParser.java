package me.ysobj.stone.parser;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.Assign;
import me.ysobj.stone.model.Identifier;
import me.ysobj.stone.tokenizer.Tokenizer;

public class StoneParser implements Parser {
	Parser parser;

	public StoneParser() {
		Parser factor = new ChoiceParser(new NumberParser(), new StringParser(), new IdentifierParser());
		Parser operator = new OperatorParser();
		Parser expression = new SequenceParser(factor, new OptionalParser(new SequenceParser(operator, factor))) {

			@Override
			protected ASTNode build(ASTNode[] children) {
				if (children[1] == null) {
					return children[0];
				}
				return super.build(children);
			}

		};
		parser = new SequenceParser(new IdentifierParser(), new KeywordParser("="), expression) {

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
