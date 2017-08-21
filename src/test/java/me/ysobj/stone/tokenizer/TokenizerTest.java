package me.ysobj.stone.tokenizer;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.Reader;

import org.junit.Test;

import me.ysobj.stone.model.Token;
import me.ysobj.stone.model.Token.TokenType;


public class TokenizerTest {
	@Test
	public void testSimpleCase() {
		Token tmp = null;
		Tokenizer tokenizer = new Tokenizer("select * from hoge");
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("select"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("*"));
		assertThat(tmp.getType(), is(TokenType.OPERATOR));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("from"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("hoge"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(false));
	}

	@Test
	public void testWithQuote() {
		Token tmp = null;
		Tokenizer tokenizer = new Tokenizer("select 'ab cd' from hoge");
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("select"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("'ab cd'"));
		assertThat(tmp.getType(), is(TokenType.STRING));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("from"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("hoge"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(false));
	}

	@Test
	public void testWithQuote2() {
		Token tmp = null;
		Tokenizer tokenizer = new Tokenizer("select 'ab''cd' from hoge");
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("select"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("'ab''cd'"));
		assertThat(tmp.getType(), is(TokenType.STRING));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("from"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("hoge"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(false));
	}

	@Test
	public void testWithNumber() {
		Token tmp = null;
		Tokenizer tokenizer = new Tokenizer("select 123 + 35 from hoge");
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("select"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("123"));
		assertThat(tmp.getType(), is(TokenType.NUMBER));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("+"));
		assertThat(tmp.getType(), is(TokenType.OPERATOR));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("35"));
		assertThat(tmp.getType(), is(TokenType.NUMBER));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("from"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("hoge"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(false));
	}

	@Test
	public void testWithNumber2() {
		Token tmp = null;
		Tokenizer tokenizer = new Tokenizer("select 123+3.55 from hoge");
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("select"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("123"));
		assertThat(tmp.getType(), is(TokenType.NUMBER));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("+"));
		assertThat(tmp.getType(), is(TokenType.OPERATOR));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("3.55"));
		assertThat(tmp.getType(), is(TokenType.NUMBER));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("from"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("hoge"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(false));
	}

	@Test
	public void testWithComma() {
		Token tmp = null;
		Tokenizer tokenizer = new Tokenizer("select abc,def, ghi from hoge");
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("select"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("abc"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is(","));
		assertThat(tmp.getType(), is(TokenType.COMMA));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("def"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is(","));
		assertThat(tmp.getType(), is(TokenType.COMMA));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("ghi"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("from"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("hoge"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(false));
	}

	@Test
	public void testNext() {
		Token tmp = null;
		Tokenizer tokenizer = new Tokenizer("select");
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("select"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(false));
		tmp = tokenizer.next();
		assertThat(tmp, is(Token.EOF));
	}

	@Test
	public void testPeek() {
		Token tmp = null;
		Tokenizer tokenizer = new Tokenizer("select * from hoge");
		tmp = tokenizer.peek();
		assertThat(tmp.getOriginal(), is("select"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		tmp = tokenizer.peek();
		assertThat(tmp.getOriginal(), is("select"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("select"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("*"));
		assertThat(tmp.getType(), is(TokenType.OPERATOR));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("from"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("hoge"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(false));
		tmp = tokenizer.peek();
		assertThat(tmp, is(Token.EOF));
	}

	@Test
	public void testPush() {
		Token tmp = null;
		Tokenizer tokenizer = new Tokenizer("select * from hoge");
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("select"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		tokenizer.push(tmp);
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("select"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("*"));
		assertThat(tmp.getType(), is(TokenType.OPERATOR));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("from"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("hoge"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(false));
		tmp = tokenizer.peek();
		assertThat(tmp, is(Token.EOF));
	}

	@Test
	public void testToArray() {
		Tokenizer tokenizer = new Tokenizer("select * from hoge");
		Token[] tokens = tokenizer.toArray();
		assertThat(tokens.length, is(5));
		assertThat(tokens[0].getOriginal(), is("select"));
		assertThat(tokens[0].getType(), is(TokenType.KEYWORD));
		assertThat(tokens[1].getOriginal(), is("*"));
		assertThat(tokens[1].getType(), is(TokenType.OPERATOR));
		assertThat(tokens[2].getOriginal(), is("from"));
		assertThat(tokens[2].getType(), is(TokenType.KEYWORD));
		assertThat(tokens[3].getOriginal(), is("hoge"));
		assertThat(tokens[3].getType(), is(TokenType.KEYWORD));
		assertThat(tokens[4], is(Token.EOF));
	}

	@Test
	public void testTokenPosition() {
		Token tmp = null;
		Tokenizer tokenizer = null;
		tokenizer = new Tokenizer("select * from hoge");
		tmp = tokenizer.next();
		assertThat(tmp.getStartPos(), is(0));
		tmp = tokenizer.next();
		assertThat(tmp.getStartPos(), is(7));
		tmp = tokenizer.next();
		assertThat(tmp.getStartPos(), is(9));
		tmp = tokenizer.next();
		assertThat(tmp.getStartPos(), is(14));
		//
		tokenizer = new Tokenizer("select     *    from   hoge");
		tmp = tokenizer.next();
		assertThat(tmp.getStartPos(), is(0));
		tmp = tokenizer.next();
		assertThat(tmp.getStartPos(), is(11));
		tmp = tokenizer.next();
		assertThat(tmp.getStartPos(), is(16));
		tmp = tokenizer.next();
		assertThat(tmp.getStartPos(), is(23));
	}

	@Test
	public void testNullReader() {
		Tokenizer tokenizer = new Tokenizer((Reader) null);
		assertThat(tokenizer.hasNext(), is(false));
	}

}
