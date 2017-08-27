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
		ASTNode astNode = parser.parse(createTokenizer("hoge = 123 - 2"));
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

	protected Tokenizer createTokenizer(String str) {
		return new Tokenizer(str, new String[] { "+", "-", "*", "/" }, new String[] { "=" });
	}

}
