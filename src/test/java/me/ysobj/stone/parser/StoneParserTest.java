package me.ysobj.stone.parser;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;
import org.junit.Test;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.Context;
import me.ysobj.stone.tokenizer.Tokenizer;

public class StoneParserTest {

	@Test
	public void test1() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("hoge = 123"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(123L));
	}

	@Test
	public void test2() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("hoge = 123 * 2"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(246L));
	}

	@Test
	public void test3() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("hoge = 123 - 3 + 1"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(121L));
	}

	@Test
	public void test4() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("hoge = 'abc'"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is("abc"));
	}

	@Test
	public void test5() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("hoge = 123 * 2; fuga = hoge * 3"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(246L));
		assertThat(context.get("fuga"), is(738L));
	}

	@Test
	public void test6() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("hoge = 111 * 2; fuga = hoge * 2; fuga = fuga * 2;"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(222L));
		assertThat(context.get("fuga"), is(888L));
	}

	@Test
	public void test7() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("hoge = 123 ; fuga = hoge + 'xxx'"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(123L));
		assertThat(context.get("fuga"), is("123xxx"));
	}

	@Test
	public void test8() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("hoge = 123 ; fuga = 'xxx'"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(123L));
		assertThat(context.get("fuga"), is("xxx"));
	}

	@Test
	public void test9() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("hoge = 123 + 2 * 4"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(131L));
	}

	@Test
	public void test10() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("hoge = (123 + 2) * 4"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(500L));
	}

	@Test
	public void test11() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("hoge = (123 + (2 * 4))"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(131L));
	}

	@Test
	public void test12() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("hoge = 2 ; fuga = 3; if hoge == 2 {hoge = 3 ; fuga = 4}"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(3L));
		assertThat(context.get("fuga"), is(4L));
	}

	@Test
	public void test13() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("hoge = 2 ; fuga = 3; if hoge == 3 {hoge = 3; fuga = 4}"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(2L));
		assertThat(context.get("fuga"), is(3L));
	}

	@Test
	public void test14() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("hoge = 0 ; hoge = hoge + 1; hoge = hoge + 2"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(3L));
	}

	@Test
	public void test15() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser
				.parse(createTokenizer("hoge = 0 ; fuga = 0; while hoge < 3 {hoge = hoge + 1;  fuga = fuga +2}"));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(3L));
		assertThat(context.get("fuga"), is(6L));
	}

	@Test
	public void test16() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode = parser.parse(createTokenizer("a = 2 < 0 ; b = 1 > 0; c = 1>= 2; d = 1>=1;"));
		astNode.evaluate(context);
		assertThat(context.get("a"), is(false));
		assertThat(context.get("b"), is(true));
		assertThat(context.get("c"), is(false));
		assertThat(context.get("d"), is(true));
	}

	protected Tokenizer createTokenizer(String str) {
		return new Tokenizer(str,
				new String[] { "+", "-", "*", "/", "=", "==", "<", ">", "<=", "!", ">=", "!=", "&&", "||" },
				new String[] {});
	}

}
