package me.ysobj.stone.parser;

import java.util.ArrayList;
import java.util.List;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.ASTNodeList;
import me.ysobj.stone.model.BinaryExpression;
import me.ysobj.stone.model.CallFuncNode;
import me.ysobj.stone.model.CallObjectNode;
import me.ysobj.stone.model.ClassInfoNode;
import me.ysobj.stone.model.FuncNode;
import me.ysobj.stone.model.Identifier;
import me.ysobj.stone.model.IfNode;
import me.ysobj.stone.model.OperatorNode;
import me.ysobj.stone.model.ParamList;
import me.ysobj.stone.model.Token;
import me.ysobj.stone.model.WhileNode;
import me.ysobj.stone.parser.ParenthesesParser.BracketType;
import me.ysobj.stone.tokenizer.Tokenizer;

public class StoneParser implements Parser {
	Parser parser;

	public StoneParser() {
		Parser param = new IdentifierParser();
		Parser paramsOption = new OptionalParser(new RepeatParser(new SequenceParser(new CommaParser(), param)));
		Parser params = new SequenceParser(param, paramsOption);
		ParenthesesParser paramList = new ParenthesesParser(BracketType.PARENTHESIS);

		paramList.setParser(new OptionalParser(params) {

			@Override
			public ASTNode parse(Tokenizer tokenizer) throws ParseException {
				ParamList paramList = new ParamList();
				while (tokenizer.hasNext()) {
					Token token = tokenizer.peek();
					if (token.getType() == Token.TokenType.IDENTIFIER) {
						tokenizer.next();
						paramList.add(new Identifier(token));
					} else if (token.getType() == Token.TokenType.COMMA) {
						tokenizer.next();
					} else {
						break;
					}
				}
				return paramList;
			}

		});
		SequenceParser func = new SequenceParser(new KeywordParser("func"), new IdentifierParser(),
				paramList/* , block */) {

			@Override
			protected ASTNode build(ASTNode[] children) {
				return new FuncNode((Identifier) children[1], (ParamList) children[2], children[3]);
			}

		};
		SequenceParser closure = new SequenceParser(new KeywordParser("func"), paramList) {

			@Override
			protected ASTNode build(ASTNode[] children) {
				return new FuncNode((ParamList) children[1], children[2]);
			}

		};
		ParenthesesParser parenthesesExp = new ParenthesesParser(BracketType.PARENTHESIS);
		SequenceParser callIdentifier = new SequenceParser(new IdentifierParser()) {

			@Override
			public ASTNode parse(Tokenizer tokenizer) throws ParseException {
				// TODO Auto-generated method stub
				return super.parse(tokenizer);
			}

			@Override
			protected ASTNode build(ASTNode[] children) {
				switch (children.length) {
				case 1:
					return children[0];
				case 2:
					ASTNodeList tmp = (ASTNodeList) children[1];
					Token t = tmp.getNodes().length > 0 ? tmp.getNodes()[0].getToken() : null;
					if (t != null && t.getType() == Token.TokenType.KEYWORD && ".".equals(t.getOriginal())) {
						return new CallObjectNode((Identifier) children[0],
								(Identifier) ((ASTNodeList) children[1]).getNodes()[1]);
					}
					return new CallFuncNode((Identifier) children[0], (ASTNodeList) children[1]);
				case 3:
					return new CallObjectNode((Identifier) children[0],
							(Identifier) ((ASTNodeList) children[1]).getNodes()[1], (ASTNodeList) children[2]);
				}
				throw new RuntimeException();
			}
		};
		SequenceParser factor = new SequenceParser(
				new ChoiceParser(parenthesesExp, new NumberParser(), new StringParser(), callIdentifier, closure));
		SequenceParser expression = new SequenceParser() {

			@Override
			protected ASTNode build(ASTNode[] children) {
				if (children.length == 1) {
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
				if (nodes.length == 1) {
					ASTNodeList nodeList = (ASTNodeList) nodes[0];
					if (nodeList.getNodes().length == 2 && nodeList.getNodes()[1] == null) {
						return nodeList.getNodes()[0];
					}
					Identifier identifier = (Identifier) nodeList.getNodes()[0];
					ASTNodeList args = (ASTNodeList) nodeList.getNodes()[1];
					return new CallFuncNode(identifier, args);
				}
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
		Parser arg = expression;
		Parser argsOption = new OptionalParser(new RepeatParser(new SequenceParser(new CommaParser(), arg)));
		Parser args = new OptionalParser(new SequenceParser(arg, argsOption));
		ParenthesesParser argList = new ParenthesesParser(BracketType.PARENTHESIS) {

			@Override
			public ASTNode parse(Tokenizer tokenizer) throws ParseException {
				ASTNode tmp = super.parse(tokenizer);
				if (tmp == null) {
					tmp = new ASTNodeList();
				}
				return tmp;
			}

		};
		argList.setParser(args);
		SequenceParser callObject = new SequenceParser(new KeywordParser("."), new IdentifierParser());
		callIdentifier.add(new OptionalParser(callObject, true));
		callIdentifier.add(new OptionalParser(argList, true));
		Parser operator = new OperatorParser();
		Parser terminator = new TerminatorParser();
		Parser expressionOption = new OptionalParser(new RepeatParser(new SequenceParser(operator, factor)));
		expression.add(factor);
		expression.add(expressionOption);
		parenthesesExp.setParser(expression);
		Parser simple = expression;

		SequenceParser classDef = new SequenceParser(new KeywordParser("class"),
				new IdentifierParser()/* , classBody */) {

			@Override
			protected ASTNode build(ASTNode[] children) {
				return new ClassInfoNode((Identifier) children[1], children[2]);
			}

		};
		ParenthesesParser classBody = new ParenthesesParser(BracketType.BRACKET);
		classDef.add(classBody);

		SequenceParser ifParser = new SequenceParser(new KeywordParser("if"), parenthesesExp /* ,block */) {

			@Override
			protected ASTNode build(ASTNode[] children) {
				if (children.length == 3) {
					return new IfNode(children[1], children[2]);
				} else if (children.length == 4) {
					ASTNode[] el = ((ASTNodeList) children[3]).getNodes();
					return new IfNode(children[1], children[2], el[1]);
				}
				throw new RuntimeException();
			}

		};
		SequenceParser whileParser = new SequenceParser(new KeywordParser("while"), parenthesesExp /* ,block */) {

			@Override
			protected ASTNode build(ASTNode[] children) {
				return new WhileNode(children[1], children[2]);
			}

		};
		SequenceParser varParser = new SequenceParser(new KeywordParser("var"), new IdentifierParser(),
				new OperatorParser("=", true), expression) {

			@Override
			protected ASTNode build(ASTNode[] children) {
				return new BinaryExpression(children[1], (OperatorNode) children[2], children[3]);
			}

		};
		Parser defParser = new ChoiceParser(func, varParser);
		Parser defParserOption = new OptionalParser(new RepeatParser(new SequenceParser(terminator, defParser)));
		classBody.setParser(new SequenceParser(defParser, defParserOption));

		Parser statement = new ChoiceParser(ifParser, whileParser, varParser, simple);
		Parser blockOption = new OptionalParser(new RepeatParser(new SequenceParser(terminator, statement)));
		ParenthesesParser block = new ParenthesesParser(BracketType.BRACKET);
		block.setParser(new SequenceParser(statement, blockOption) {
			@Override
			protected ASTNode build(ASTNode[] children) {
				List<ASTNode> tmp = new ArrayList<>();
				tmp.add(children[0]);
				if (children.length == 2) {
					ASTNode[] nodeArray = ((ASTNodeList) children[1]).getNodes();
					for (ASTNode astNode : nodeArray) {
						tmp.add(((ASTNodeList) astNode).getNodes()[0]);
					}
				}
				return new ASTNodeList(tmp.toArray(new ASTNode[0]));
			}
		});
		ifParser.add(block);
		ifParser.add(new OptionalParser(new SequenceParser(new KeywordParser("else"), block)));
		whileParser.add(block);
		func.add(block);
		closure.add(block);
		Parser code = new ChoiceParser(classDef, func, statement);
		parser = new SequenceParser(code, new OptionalParser(new RepeatParser(new SequenceParser(terminator, code)))) {
			@Override
			protected ASTNode build(ASTNode[] children) {
				List<ASTNode> tmp = new ArrayList<>();
				tmp.add(children[0]);
				if (children.length == 2) {
					ASTNode[] nodeArray = ((ASTNodeList) children[1]).getNodes();
					for (ASTNode astNode : nodeArray) {
						if (astNode != null) {
							tmp.add(astNode);
						}
					}
				}
				return new ASTNodeList(tmp.toArray(new ASTNode[0]));
			}
		};

	}

	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		return this.parser.parse(tokenizer);
	}
	// arg := expression
	// args := arg { "," arg }
	// arg_list := "(" [args] ")"
	// param := IDENTIFIER
	// params := param { "," param }
	// param_list := "(" [params] ")"
	// func := "func" IDENTIFIER param_list block
	// closure := "func" param_list block
	// call_identifier := IDENTIFIER [DOT IDENTIFIER] {arg_list}
	// factor := ( parentheses_expression | NUMBER | STRING | call_identifier |
	// closure)
	// expression := factor {OPERATOR factor}
	// parentheses_expression := "(" expression ")"
	// block := "{" statement {TERMINATOR [ statement ]} "}"
	// simple := expression
	// class_def := "class" IDENTIFIER { "extends" IDENTIFIER } class_body
	// class_body := "{" def_statement {TERMINATOR [ def_statement ]} "}"
	// def_statement = func | var_statement
	// if_statement := "if" parentheses_expression block ["else" block]
	// while_statement := while parentheses_expression block
	// var_statement := "var" IDENTIFIER OPERATOR expression
	// statement := if_statement
	// | while_statement
	// | var_statement
	// | simple
	// code := class_def | func | statement
	// program := code {TERMINATOR code}
}
