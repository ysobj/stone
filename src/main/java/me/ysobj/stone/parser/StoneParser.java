package me.ysobj.stone.parser;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.ASTNodeList;
import me.ysobj.stone.model.BinaryExpression;
import me.ysobj.stone.model.DivideOperator;
import me.ysobj.stone.model.MinusOperator;
import me.ysobj.stone.model.MultiplyOperator;
import me.ysobj.stone.model.Operator;
import me.ysobj.stone.model.PlusOperator;
import me.ysobj.stone.model.SubstituteOperator;
import me.ysobj.stone.tokenizer.Tokenizer;

public class StoneParser implements Parser {
	Parser parser;

	public StoneParser() {
		ParenthesesParser parenthesesExp = new ParenthesesParser();
		Parser factor = new ChoiceParser(parenthesesExp, new NumberParser(), new StringParser(),
				new IdentifierParser());
		Parser operator = new OperatorParser();
		Parser expressionOption = new OptionalParser(new RepeatParser(new SequenceParser(operator, factor)));
		Parser expression = new SequenceParser(factor, expressionOption) {

			@Override
			protected ASTNode build(ASTNode[] children) {
				if (children[1] == null) {
					return children[0];
				}
				ASTNodeList list = (ASTNodeList) children[1];
				ASTNode[] tmp = new ASTNode[list.size() * 2 + 1];
				tmp[0] = children[0];
				ASTNode[] nodeArray = list.getNodes();
				for (int i = 0; i < nodeArray.length; i++) {
					ASTNodeList tmpNodeList = (ASTNodeList) nodeArray[i];
					tmp[i * 2 + 1] = tmpNodeList.getNodes()[0];
					tmp[i * 2 + 2] = tmpNodeList.getNodes()[1];
				}
				return buildRecursively(tmp);
			}

			protected Operator convertOperator(ASTNode node) {
				String tmp = node.evaluate(null).toString();
				switch (tmp) {
				case "*":
					return new MultiplyOperator();
				case "+":
					return new PlusOperator();
				case "-":
					return new MinusOperator();
				case "/":
					return new DivideOperator();
				case "=":
					return new SubstituteOperator();
				}
				return null;
			}

			protected ASTNode buildRecursively(ASTNode[] nodes) {
				Operator left = convertOperator(nodes[1]);
				if (nodes.length == 3) {
					return new BinaryExpression(nodes[0], left, nodes[2]);
				}
				Operator right = convertOperator(nodes[3]);
				if (isRight(left, right)) {
					ASTNode[] tmp = new ASTNode[nodes.length - 2];
					System.arraycopy(nodes, 2, tmp, 0, nodes.length - 2);
					return new BinaryExpression(nodes[0], left, buildRecursively(tmp));
				} else {
					ASTNode[] tmp = new ASTNode[nodes.length - 2];
					tmp[0] = new BinaryExpression(nodes[0], left, nodes[2]);
					System.arraycopy(nodes, 3, tmp, 1, nodes.length - 3);
					return buildRecursively(tmp);
				}
			}

			protected boolean isRight(Operator left, Operator right) {
				return left.order() < right.order();
			}
		};
		parenthesesExp.setParser(expression);
		parser = new SequenceParser(expression, new OptionalParser(new RepeatParser(expression))) {
			@Override
			protected ASTNode build(ASTNode[] children) {
				return new ASTNodeList(children);
			}
		};

	}

	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		return this.parser.parse(tokenizer);
	}

	// factor := "(" expression ")"| NUMBER | STRING | IDENTIFIER
	// expression := factor [OPERATOR factor]*
	// code := expression [TERMINATER expression]*
}
