package me.ysobj.stone.parser;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.junit.Test;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.exception.VariableAlreadyDefinedException;
import me.ysobj.stone.exception.VariableNotFoundException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.Context;
import me.ysobj.stone.model.FuncNode;
import me.ysobj.stone.model.Identifier;
import me.ysobj.stone.model.NestedContext;
import me.ysobj.stone.model.ParamList;
import me.ysobj.stone.model.StoneObject;
import me.ysobj.stone.model.Token;
import me.ysobj.stone.model.Token.TokenType;
import me.ysobj.stone.model.Void;
import me.ysobj.stone.tokenizer.Tokenizer;

public class StoneParserTest {

	@Test
	public void testSimpleAssignment() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("var hoge = 123"));
		assertThat(astNode.toString(), is("[hoge = [123]]"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(123L));
	}

	@Test
	public void testAssignmentWithBinaryExpression() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("var hoge = 123 * 2"));
		assertThat(astNode.toString(), is("[hoge = [123] * [2]]"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(246L));
	}

	@Test
	public void testAssignmentWithBinaryExpressions() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("var hoge = 123 - 3 + 1"));
		assertThat(astNode.toString(), is("[hoge = [123] - [3] + [1]]"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(121L));
	}

	@Test
	public void testAssignString() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("var hoge = 'abc'"));
		assertThat(astNode.toString(), is("[hoge = ['abc']]"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is("abc"));
	}

	@Test
	public void testMultipleStatements() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("var hoge = 123 * 2; var fuga = hoge * 3"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(246L));
		assertThat(context.get("fuga"), is(738L));
	}

	@Test
	public void testMultipleStatementsAndVariable() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("var hoge = 111 * 2; var fuga = hoge * 2; fuga = fuga * 2;"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(222L));
		assertThat(context.get("fuga"), is(888L));
	}

	@Test
	public void testPlusOperatorWithNumberAndString() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("var hoge = 123 ; var fuga = hoge + 'xxx'"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(123L));
		assertThat(context.get("fuga"), is("123xxx"));
	}

	@Test
	public void testAssignNumberAndString() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("var hoge = 123 ; var fuga = 'xxx'"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(123L));
		assertThat(context.get("fuga"), is("xxx"));
	}

	@Test
	public void testOperatorPrecedence() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("var hoge = 123 + 2 * 4"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(131L));
	}

	@Test
	public void testOperatorPrecedenceWithParentheses() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("var hoge = (123 + 2) * 4"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(500L));
	}

	@Test
	public void testOperatorPrecedenceWithParentheses2() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("var hoge = (123 + (2 * 4))"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(131L));
	}

	@Test
	public void testIfConditionReturnTrue() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser
				.parse(createTokenizer("var hoge = 2 ; var fuga = 3; if(hoge == 2){hoge = 3 ; fuga = 4}"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(3L));
		assertThat(context.get("fuga"), is(4L));
	}

	@Test
	public void testIfConditionReturnFalse() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser
				.parse(createTokenizer("var hoge = 2 ; var   fuga = 3; if(hoge == 3){hoge = 3; fuga = 4}"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(2L));
		assertThat(context.get("fuga"), is(3L));
	}

	@Test
	public void testMultipleStatements2() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("var hoge = 0 ; hoge = hoge + 1; hoge = hoge + 2"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(3L));
	}

	@Test
	public void testWhile() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(
				createTokenizer("var hoge = 0 ;var fuga = 0; while(hoge < 3){hoge = hoge + 1;  fuga = fuga +2}"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(3L));
		assertThat(context.get("fuga"), is(6L));
	}

	@Test
	public void testBooleanOperatorAndAssignment() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("var a = 2 < 0 ; var b = 1 > 0; var c = 1>= 2;var d = 1>=1;"));
		astNode.evaluate(context);
		assertThat(context.get("a"), is(false));
		assertThat(context.get("b"), is(true));
		assertThat(context.get("c"), is(false));
		assertThat(context.get("d"), is(true));
	}

	@Test
	public void testIfAndGlobalVariable() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		// Oh, my language has no block scope!
		ASTNode astNode = parser.parse(createTokenizer("var hoge = 0 ; if( hoge < 1){ var fuga = 2}"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(0L));
		assertThat(context.get("fuga"), is(2L));
	}

	@Test
	public void testCallFunctionWithNoArgs() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("var a = 0; func hoge(){ a = a + 1 }; hoge();"));
		assertThat(astNode.toString(), is("[a = [0], [func hoge( [] ){[[a] = [a] + [1]]}], [[hoge([])]]]"));
		astNode.evaluate(context);
		assertThat(context.get("a"), is(1L));
	}

	@Test
	public void testCallFunctionAndGlobalAssignment() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser
				.parse(createTokenizer("var a = 1; var b = 2; var c = 3; func hoge(x,y){ a = a + x }; hoge(b,c);"));
		astNode.evaluate(context);
		assertThat(context.get("a"), is(3L));
	}

	@Test
	public void testParseCallFunctionWithNoArgs() throws ParseException {
		Parser parser = new StoneParser();
		ASTNode astNode = parser.parse(createTokenizer("hoge();"));
		assertThat(astNode.toString(), is("[[hoge([])]]"));
	}

	@Test(expected = VariableNotFoundException.class)
	public void testVariableNotFound() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("a = 3"));
		astNode.evaluate(context);
	}

	@Test(expected = VariableAlreadyDefinedException.class)
	public void testVariableAlreadyDefined() throws Exception {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("var a = 3; var a = 4"));
		astNode.evaluate(context);
	}

	@Test
	public void testExecuteFunction() throws Exception {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("func hoge(){ 123 }; var x = hoge();"));
		assertThat(astNode.toString(), is("[func hoge( [] ){[[123]]}, [x = [hoge([])]]]"));
		astNode.evaluate(context);
		assertThat(context.get("x"), is(123L));

	}

	@Test
	public void testIf() throws Exception {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("var x = 1; var y = 2; if (x == 0) { y = 3 } else { y = 4 }"));
		astNode.evaluate(context);
		assertThat(context.get("y"), is(4L));
	}

	@Test
	public void testCallFunctionWithVariable() throws Exception {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("func abc(x){ x * 2}; var y = abc(10);"));
		assertThat(astNode.toString(), is("[func abc( [x] ){[[x] * [2]]}, [y = [abc([[10]])]]]"));
		astNode.evaluate(context);
		assertThat(context.get("y"), is(20L));
	}

	@Test
	public void testCallFunctionWithVariables() throws Exception {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("func abc(x,y){ x * y}; var z = abc(3,4);"));
		assertThat(astNode.toString(),
				is("[func abc( [x, y] ){[[x] * [y]]}, [z = [abc([[3], [[Token [normalize=,, type=COMMA], [4]]]])]]]"));
		astNode.evaluate(context);
		assertThat(context.get("z"), is(12L));
	}

	@Test
	public void testCallFunctionWithExpression() throws Exception {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("func abc(x){ x * 2}; var y = abc(10 - 1);"));
		assertThat(astNode.toString(), is("[func abc( [x] ){[[x] * [2]]}, [y = [abc([[10] - [1]])]]]"));
		astNode.evaluate(context);
		assertThat(context.get("y"), is(18L));
	}

	@Test
	public void testCalculateFibonacciNumber() throws Exception {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser
				.parse(createTokenizer("func fib(n){ if(n<2){ n }else{ fib(n-1) + fib(n-2)}};var x = fib(10);"));
		assertThat(astNode.toString(), is(
				"[func fib( [n] ){[if ([n] < [2]){[[n]]}else{[[fib([[n] - [1]])] + [fib([[n] - [2]])]]}]}, [x = [fib([[10]])]]]"));
		astNode.evaluate(context);
		assertThat(context.get("x"), is(55L));
	}

	@Test
	public void testCalculateFibonacciNumberWithSrc() throws Exception {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer(pathToString("fib.stn")));
		assertThat(astNode.toString(), is(
				"[func fib( [n] ){[if ([n] < [2]){[[n]]}else{[[fib([[n] - [1]])] + [fib([[n] - [2]])]]}]}, [x = [fib([[10]])]]]"));
		astNode.evaluate(context);
		assertThat(context.get("x"), is(55L));
	}

	@Test
	public void testAssignFunctionToVariable() throws Exception {
		Parser parser = new StoneParser();
		Context context = new NestedContext();
		ASTNode astNode = parser.parse(createTokenizer(pathToString("assignFunction.stn")));
		assertThat(astNode.toString(), is("[x = [func( [] ){ [[35]] }], [y = [x([])]]]"));
		astNode.evaluate(context);
		assertThat(context.get("y"), is(35L));
	}

	@Test
	public void testClosure() throws Exception {
		Parser parser = new StoneParser();
		Context context = createContext();
		ASTNode astNode = parser.parse(createTokenizer(pathToString("closure.stn")));
		assertThat(astNode.toString(), is(
				"[x = [func( [] ){ [a = [0], [func( [] ){ [[a] = [a] + [1], [a]] }]] }], [y = [x([])]], [z = [y([])]], [[z] = [y([])]], [[z] = [y([])]], [[print([[z]])]]]"));
		astNode.evaluate(context);
		assertThat(context.get("z"), is(3L));
	}

	@Test
	public void testNativePrint() throws Exception {
		Parser parser = new StoneParser();
		Context context = createContext();
		ASTNode astNode = parser.parse(createTokenizer("print('Hello World')"));
		assertThat(astNode.toString(), is("[[print([['Hello World']])]]"));
		astNode.evaluate(context);
	}

	@Test
	public void testClass() throws Exception {
		Parser parser = new StoneParser();
		Context context = createContext();
		ASTNode astNode = parser.parse(createTokenizer(pathToString("class.stn")));
		assertThat(astNode.toString(), is(
				"[class Point{[x = [6], [[y = [7]], [func move( [nx, ny] ){[[x] = [nx], [y] = [ny]]}], [func calc( [] ){[[x] + [y]]}]]]}, [p = [Point.new([])]], [[p.move([[2], [[Token [normalize=,, type=COMMA], [3]]]])]], [[p.calc([])]], [[print([[p.x]])]], [[print([[p.y]])]]]"));

		astNode.evaluate(context);
		assertThat(context.get("p"), is(instanceOf(StoneObject.class)));
		StoneObject obj = (StoneObject) context.get("p");
		assertThat(obj.get("x"), is(2L));
		assertThat(obj.get("y"), is(3L));
	}

	protected String pathToString(String name) throws IOException {
		Path path = Paths.get("target/test-classes/", name);
		return Files.readAllLines(path, StandardCharsets.UTF_8).stream().collect(Collectors.joining());
	}

	protected Context createContext() {
		NestedContext context = new NestedContext();
		Token token = Token.create("param1", 0, TokenType.IDENTIFIER);
		Identifier param = new Identifier(token);
		ParamList paramList = new ParamList(param);
		ASTNode block = new ASTNode() {

			@Override
			public Object evaluate(Context context) {
				Object obj = context.get("param1");
				if (obj == null) {
					obj = "null";
				}
				System.out.println(obj.toString());
				return Void.VOID;
			}

		};
		FuncNode node = new FuncNode(paramList, block);
		context.putNew("print", node);
		return context;
	}

	protected Tokenizer createTokenizer(String str) {
		return new Tokenizer(str,
				new String[] { "+", "-", "*", "/", "=", "==", "<", ">", "<=", "!", ">=", "!=", "&&", "||" },
				new String[] { "if", "while", "func", "var", "else", "class" });
	}

}
