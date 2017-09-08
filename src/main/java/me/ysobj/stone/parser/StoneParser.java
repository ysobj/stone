package me.ysobj.stone.parser;

import java.util.ArrayList;
import java.util.List;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.ASTNodeList;
import me.ysobj.stone.model.BinaryExpression;
import me.ysobj.stone.model.DivideOperator;
import me.ysobj.stone.model.EquivalentOperator;
import me.ysobj.stone.model.IfNode;
import me.ysobj.stone.model.MinusOperator;
import me.ysobj.stone.model.MultiplyOperator;
import me.ysobj.stone.model.Operator;
import me.ysobj.stone.model.OperatorNode;
import me.ysobj.stone.model.PlusOperator;
import me.ysobj.stone.model.SubstituteOperator;
import me.ysobj.stone.model.WhileNode;
import me.ysobj.stone.parser.ParenthesesParser.BracketType;
import me.ysobj.stone.tokenizer.Tokenizer;

public class StoneParser implements Parser {
	Parser parser;

	public StoneParser() {
		ParenthesesParser parenthesesExp = new ParenthesesParser(BracketType.PARENTHESIS);
		Parser factor = new ChoiceParser(parenthesesExp, new NumberParser(), new StringParser(),
				new IdentifierParser());
		Parser operator = new OperatorParser();
		Parser terminator = new TerminatorParser();
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

			protected ASTNode buildRecursively(ASTNode[] nodes) {
				OperatorNode left = (OperatorNode) nodes[1];
				if (nodes.length == 3) {
					return new BinaryExpression(nodes[0], left, nodes[2]);
				}
				OperatorNode right = (OperatorNode) nodes[3];
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

			protected boolean isRight(OperatorNode left, OperatorNode right) {
				return left.getOperator().order() < right.getOperator().order();
			}
		};
		parenthesesExp.setParser(expression);
		Parser simple = expression;
		SequenceParser ifParser = new SequenceParser(new KeywordParser("if"), expression /* ,block */) {

			@Override
			protected ASTNode build(ASTNode[] children) {
				return new IfNode(children[1], children[2]);
			}

		};
		SequenceParser whileParser = new SequenceParser(new KeywordParser("while"), expression /* ,block */) {

			@Override
			protected ASTNode build(ASTNode[] children) {
				return new WhileNode(children[1], children[2]);
			}

		};
		Parser statement = new ChoiceParser(ifParser, whileParser, simple);
		Parser blockOption = new OptionalParser(new RepeatParser(new SequenceParser(terminator, statement)));
		ParenthesesParser block = new ParenthesesParser(BracketType.BRACKET);
		block.setParser(new SequenceParser(statement, blockOption) {
			@Override
			protected ASTNode build(ASTNode[] children) {
				ASTNode[] nodeArray = ((ASTNodeList) children[1]).getNodes();
				List<ASTNode> tmp = new ArrayList<>();
				tmp.add(children[0]);
				for (ASTNode astNode : nodeArray) {
					tmp.add(((ASTNodeList) astNode).getNodes()[1]);
				}
				return new ASTNodeList(tmp.toArray(new ASTNode[0]));
			}
		});
		ifParser.add(block);
		whileParser.add(block);
		parser = new SequenceParser(statement,
				new OptionalParser(new RepeatParser(new SequenceParser(terminator, statement)))) {
			@Override
			protected ASTNode build(ASTNode[] children) {
				ASTNode[] nodeArray = ((ASTNodeList) children[1]).getNodes();
				List<ASTNode> tmp = new ArrayList<>();
				tmp.add(children[0]);
				for (ASTNode astNode : nodeArray) {
					tmp.add(astNode);
				}
				return new ASTNodeList(tmp.toArray(new ASTNode[0]));
			}
		};

	}

	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		return this.parser.parse(tokenizer);
	}

	// factor := "(" expression ")"| NUMBER | STRING | IDENTIFIER
	// expression := factor [OPERATOR factor]*
	// block := "{" [ statement ] {TERMINATER [ statement ]} "}"
	// simple := expression
	// statement := "if" expression block
	// | while expression block
	// | simple
	// code := statement [TERMINATER statement]*
}
