package me.ysobj.stone.parser;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.ASTNodeList;
import me.ysobj.stone.model.Assign;
import me.ysobj.stone.model.BinaryExpression;
import me.ysobj.stone.model.Identifier;
import me.ysobj.stone.model.MultiplyOperator;
import me.ysobj.stone.model.Operator;
import me.ysobj.stone.model.OperatorNode;
import me.ysobj.stone.tokenizer.Tokenizer;

public class StoneParser implements Parser {
	Parser parser;

	public StoneParser() {
		Parser factor = new ChoiceParser(new NumberParser(), new StringParser(), new IdentifierParser());
		Parser operator = new OperatorParser();
		Parser expressionOption = new OptionalParser(new SequenceParser(operator, factor));
		Parser expression = new SequenceParser(factor, expressionOption) {

			@Override
			protected ASTNode build(ASTNode[] children) {
				if (children[1] == null) {
					return children[0];
				}
				ASTNodeList list = (ASTNodeList) children[1];
				Operator operator = ((OperatorNode) list.getNodes()[0]).getOperator();
				return new BinaryExpression(children[0], operator, list.getNodes()[1]);
			}

		};
		Parser assign = new SequenceParser(new IdentifierParser(), new KeywordParser("="), expression) {

			@Override
			protected ASTNode build(ASTNode[] children) {
				return new Assign((Identifier) children[0], children[2]);
			}

		};
		parser = new SequenceParser(assign, new OptionalParser(new RepeatParser(assign)));

	}

	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		return this.parser.parse(tokenizer);
	}

	// factor := NUMBER | STRING | IDENTIFIER
	// expression := factor [OPERATOR factor]
	// assign := IDENTIFIER '=' expression
	// code := assign [TERMINATER assign]*
}
