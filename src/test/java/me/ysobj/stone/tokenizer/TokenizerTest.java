package me.ysobj.stone.tokenizer;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.junit.Test;

import me.ysobj.stone.model.Token;
import me.ysobj.stone.model.Token.TokenType;

public class TokenizerTest {

	protected Tokenizer createTokenizer(String str) {
		return new Tokenizer(str, new String[] { "*", "+", "=", "==", "<", ">", "<=", "!", ">=", "!=", "&&", "||" },
				new String[] { "select", "from" });
	}

	protected Tokenizer createStoneTokenizer(String str) {
		return new Tokenizer(str,
				new String[] { "+", "-", "*", "/", "=", "==", "<", ">", "<=", "!", ">=", "!=", "&&", "||" },
				new String[] { "if", "while", "func", "var", "else" });
	}

	@Test
	public void testSimpleCase() {
		Token tmp = null;
		Tokenizer tokenizer = createTokenizer("select * from hoge");
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
		assertThat(tmp.getType(), is(TokenType.IDENTIFIER));
		assertThat(tokenizer.hasNext(), is(false));
	}

	@Test
	public void testWithQuote() {
		Token tmp = null;
		Tokenizer tokenizer = createTokenizer("select 'ab cd' from hoge");
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
		assertThat(tmp.getType(), is(TokenType.IDENTIFIER));
		assertThat(tokenizer.hasNext(), is(false));
	}

	@Test
	public void testWithQuote2() {
		Token tmp = null;
		Tokenizer tokenizer = createTokenizer("select 'ab''cd' from hoge");
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
		assertThat(tmp.getType(), is(TokenType.IDENTIFIER));
		assertThat(tokenizer.hasNext(), is(false));
	}

	@Test
	public void testWithNumber() {
		Token tmp = null;
		Tokenizer tokenizer = createTokenizer("select 123 + 35 from hoge");
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
		assertThat(tmp.getType(), is(TokenType.IDENTIFIER));
		assertThat(tokenizer.hasNext(), is(false));
	}

	@Test
	public void testWithNumber2() {
		Token tmp = null;
		Tokenizer tokenizer = new Tokenizer("select 123+3.55 from hoge", new String[] { "+" },
				new String[] { "select", "from" });
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
		assertThat(tmp.getType(), is(TokenType.IDENTIFIER));
		assertThat(tokenizer.hasNext(), is(false));
	}

	@Test
	public void testWithComma() {
		Token tmp = null;
		Tokenizer tokenizer = createTokenizer("select abc,def, ghi from hoge");
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("select"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("abc"));
		assertThat(tmp.getType(), is(TokenType.IDENTIFIER));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is(","));
		assertThat(tmp.getType(), is(TokenType.COMMA));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("def"));
		assertThat(tmp.getType(), is(TokenType.IDENTIFIER));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is(","));
		assertThat(tmp.getType(), is(TokenType.COMMA));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("ghi"));
		assertThat(tmp.getType(), is(TokenType.IDENTIFIER));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("from"));
		assertThat(tmp.getType(), is(TokenType.KEYWORD));
		assertThat(tokenizer.hasNext(), is(true));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("hoge"));
		assertThat(tmp.getType(), is(TokenType.IDENTIFIER));
		assertThat(tokenizer.hasNext(), is(false));
	}

	@Test
	public void testNext() {
		Token tmp = null;
		Tokenizer tokenizer = createTokenizer("select");
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
		Tokenizer tokenizer = createTokenizer("select * from hoge");
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
		assertThat(tmp.getType(), is(TokenType.IDENTIFIER));
		assertThat(tokenizer.hasNext(), is(false));
		tmp = tokenizer.peek();
		assertThat(tmp, is(Token.EOF));
	}

	@Test
	public void testPush() {
		Token tmp = null;
		Tokenizer tokenizer = createTokenizer("select * from hoge");
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
		assertThat(tmp.getType(), is(TokenType.IDENTIFIER));
		assertThat(tokenizer.hasNext(), is(false));
		tmp = tokenizer.peek();
		assertThat(tmp, is(Token.EOF));
	}

	@Test
	public void testToArray() {
		Tokenizer tokenizer = createTokenizer("select * from hoge");
		Token[] tokens = tokenizer.toArray();
		assertThat(tokens.length, is(5));
		assertThat(tokens[0].getOriginal(), is("select"));
		assertThat(tokens[0].getType(), is(TokenType.KEYWORD));
		assertThat(tokens[1].getOriginal(), is("*"));
		assertThat(tokens[1].getType(), is(TokenType.OPERATOR));
		assertThat(tokens[2].getOriginal(), is("from"));
		assertThat(tokens[2].getType(), is(TokenType.KEYWORD));
		assertThat(tokens[3].getOriginal(), is("hoge"));
		assertThat(tokens[3].getType(), is(TokenType.IDENTIFIER));
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

	@Test
	public void testMultipleStatement() {
		Tokenizer tokenizer = createTokenizer("hoge = 123; fuga = 246");
		Token tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("hoge"));
		assertThat(tmp.getType(), is(TokenType.IDENTIFIER));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("="));
		assertThat(tmp.getType(), is(TokenType.OPERATOR));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("123"));
		assertThat(tmp.getType(), is(TokenType.NUMBER));
		tmp = tokenizer.next();
		assertThat(tmp.getType(), is(TokenType.TERMINATOR));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("fuga"));
		assertThat(tmp.getType(), is(TokenType.IDENTIFIER));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("="));
		assertThat(tmp.getType(), is(TokenType.OPERATOR));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("246"));
		assertThat(tmp.getType(), is(TokenType.NUMBER));
	}

	@Test
	public void testMultipleStatement2() {
		Tokenizer tokenizer = createTokenizer("hoge = 123 ; fuga = 246");
		Token tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("hoge"));
		assertThat(tmp.getType(), is(TokenType.IDENTIFIER));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("="));
		assertThat(tmp.getType(), is(TokenType.OPERATOR));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("123"));
		assertThat(tmp.getType(), is(TokenType.NUMBER));
		tmp = tokenizer.next();
		assertThat(tmp.getType(), is(TokenType.TERMINATOR));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("fuga"));
		assertThat(tmp.getType(), is(TokenType.IDENTIFIER));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("="));
		assertThat(tmp.getType(), is(TokenType.OPERATOR));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("246"));
		assertThat(tmp.getType(), is(TokenType.NUMBER));
	}

	@Test
	public void testParenAndBrace() {
		Tokenizer tokenizer = createTokenizer("{hoge = (123 + 456)}");
		Token tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("{"));
		assertThat(tmp.getType(), is(TokenType.BRACE_OPEN));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("hoge"));
		assertThat(tmp.getType(), is(TokenType.IDENTIFIER));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("="));
		assertThat(tmp.getType(), is(TokenType.OPERATOR));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("("));
		assertThat(tmp.getType(), is(TokenType.PAREN_OPEN));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("123"));
		assertThat(tmp.getType(), is(TokenType.NUMBER));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("+"));
		assertThat(tmp.getType(), is(TokenType.OPERATOR));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("456"));
		assertThat(tmp.getType(), is(TokenType.NUMBER));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is(")"));
		assertThat(tmp.getType(), is(TokenType.PAREN_CLOSE));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("}"));
		assertThat(tmp.getType(), is(TokenType.BRACE_CLOSE));
	}

	@Test
	public void testEquivalentAndSubstitute() {
		Tokenizer tokenizer = createTokenizer("hoge = 123 == 456");
		Token tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("hoge"));
		assertThat(tmp.getType(), is(TokenType.IDENTIFIER));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("="));
		assertThat(tmp.getType(), is(TokenType.OPERATOR));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("123"));
		assertThat(tmp.getType(), is(TokenType.NUMBER));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("=="));
		assertThat(tmp.getType(), is(TokenType.OPERATOR));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("456"));
		assertThat(tmp.getType(), is(TokenType.NUMBER));
	}

	@Test
	public void testLt() {
		Tokenizer tokenizer = createTokenizer("hoge = 123<456");
		Token tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("hoge"));
		assertThat(tmp.getType(), is(TokenType.IDENTIFIER));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("="));
		assertThat(tmp.getType(), is(TokenType.OPERATOR));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("123"));
		assertThat(tmp.getType(), is(TokenType.NUMBER));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("<"));
		assertThat(tmp.getType(), is(TokenType.OPERATOR));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("456"));
		assertThat(tmp.getType(), is(TokenType.NUMBER));
	}

	@Test
	public void testLt2() {
		Tokenizer tokenizer = createTokenizer("hoge = n<456");
		Token tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("hoge"));
		assertThat(tmp.getType(), is(TokenType.IDENTIFIER));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("="));
		assertThat(tmp.getType(), is(TokenType.OPERATOR));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("n"));
		assertThat(tmp.getType(), is(TokenType.IDENTIFIER));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("<"));
		assertThat(tmp.getType(), is(TokenType.OPERATOR));
		tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is("456"));
		assertThat(tmp.getType(), is(TokenType.NUMBER));
	}

	@Test
	public void testParseFibonacciNumberFunc() {
		Tokenizer tokenizer = createStoneTokenizer(
				"func fib(n){ if(n<2){ n }else{ fib(n-1) + fib(n-2);}};var x = fib(10);");
		assertConvenient(tokenizer, "func", TokenType.KEYWORD);
		assertConvenient(tokenizer, "fib", TokenType.IDENTIFIER);
		assertConvenient(tokenizer, "(", TokenType.PAREN_OPEN);
		assertConvenient(tokenizer, "n", TokenType.IDENTIFIER);
		assertConvenient(tokenizer, ")", TokenType.PAREN_CLOSE);
		assertConvenient(tokenizer, "{", TokenType.BRACE_OPEN);
		assertConvenient(tokenizer, "if", TokenType.KEYWORD);
		assertConvenient(tokenizer, "(", TokenType.PAREN_OPEN);
		assertConvenient(tokenizer, "n", TokenType.IDENTIFIER);
		assertConvenient(tokenizer, "<", TokenType.OPERATOR);
		assertConvenient(tokenizer, "2", TokenType.NUMBER);
		assertConvenient(tokenizer, ")", TokenType.PAREN_CLOSE);
		assertConvenient(tokenizer, "{", TokenType.BRACE_OPEN);
		assertConvenient(tokenizer, "n", TokenType.IDENTIFIER);
		assertConvenient(tokenizer, "}", TokenType.BRACE_CLOSE);
		assertConvenient(tokenizer, "else", TokenType.KEYWORD);
		assertConvenient(tokenizer, "{", TokenType.BRACE_OPEN);
		assertConvenient(tokenizer, "fib", TokenType.IDENTIFIER);
		assertConvenient(tokenizer, "(", TokenType.PAREN_OPEN);
		assertConvenient(tokenizer, "n", TokenType.IDENTIFIER);
		assertConvenient(tokenizer, "-", TokenType.OPERATOR);
		assertConvenient(tokenizer, "1", TokenType.NUMBER);
		assertConvenient(tokenizer, ")", TokenType.PAREN_CLOSE);
		assertConvenient(tokenizer, "+", TokenType.OPERATOR);
		assertConvenient(tokenizer, "fib", TokenType.IDENTIFIER);
		assertConvenient(tokenizer, "(", TokenType.PAREN_OPEN);
		assertConvenient(tokenizer, "n", TokenType.IDENTIFIER);
		assertConvenient(tokenizer, "-", TokenType.OPERATOR);
		assertConvenient(tokenizer, "2", TokenType.NUMBER);
		assertConvenient(tokenizer, ")", TokenType.PAREN_CLOSE);
		assertConvenient(tokenizer, ";", TokenType.TERMINATOR);
		assertConvenient(tokenizer, "}", TokenType.BRACE_CLOSE);
		assertConvenient(tokenizer, "}", TokenType.BRACE_CLOSE);
		assertConvenient(tokenizer, ";", TokenType.TERMINATOR);
		assertConvenient(tokenizer, "var", TokenType.KEYWORD);
		assertConvenient(tokenizer, "x", TokenType.IDENTIFIER);
		assertConvenient(tokenizer, "=", TokenType.OPERATOR);
		assertConvenient(tokenizer, "fib", TokenType.IDENTIFIER);
		assertConvenient(tokenizer, "(", TokenType.PAREN_OPEN);
		assertConvenient(tokenizer, "10", TokenType.NUMBER);
		assertConvenient(tokenizer, ")", TokenType.PAREN_CLOSE);
		assertConvenient(tokenizer, ";", TokenType.TERMINATOR);
	}

	@Test
	public void testLf() throws Exception {
		Tokenizer tokenizer1 = createStoneTokenizer("var x = 123;\nvar y = 456;");
		Tokenizer tokenizer2 = createStoneTokenizer("var x = 123;var y = 456;");
		Token t1 = tokenizer1.next();
		Token t2 = tokenizer2.next();
		assertThat(t1, is(t2));
		t1 = tokenizer1.next();
		t2 = tokenizer2.next();
		assertThat(t1, is(t2));
		t1 = tokenizer1.next();
		t2 = tokenizer2.next();
		assertThat(t1, is(t2));
		t1 = tokenizer1.next();
		t2 = tokenizer2.next();
		assertThat(t1, is(t2));
		t1 = tokenizer1.next();
		t2 = tokenizer2.next();
		assertThat(t1, is(t2));
		t1 = tokenizer1.next();
		t2 = tokenizer2.next();
		assertThat(t1, is(t2));
		t1 = tokenizer1.next();
		t2 = tokenizer2.next();
		assertThat(t1, is(t2));
		t1 = tokenizer1.next();
		t2 = tokenizer2.next();
		assertThat(t1, is(t2));
		t1 = tokenizer1.next();
		t2 = tokenizer2.next();
		assertThat(t1, is(t2));
		t1 = tokenizer1.next();
		t2 = tokenizer2.next();
		assertThat(t1, is(t2));
	}

	@Test
	public void testTokenizeClosure() throws Exception {
		Tokenizer tokenizer = createStoneTokenizer(pathToString("closure.stn"));
		assertConvenient(tokenizer, "var", TokenType.KEYWORD);
		assertConvenient(tokenizer, "x", TokenType.IDENTIFIER);
		assertConvenient(tokenizer, "=", TokenType.OPERATOR);
		assertConvenient(tokenizer, "func", TokenType.KEYWORD);
		assertConvenient(tokenizer, "(", TokenType.PAREN_OPEN);
		assertConvenient(tokenizer, ")", TokenType.PAREN_CLOSE);
		assertConvenient(tokenizer, "{", TokenType.BRACE_OPEN);
		assertConvenient(tokenizer, "var", TokenType.KEYWORD);
		assertConvenient(tokenizer, "a", TokenType.IDENTIFIER);
		assertConvenient(tokenizer, "=", TokenType.OPERATOR);
		assertConvenient(tokenizer, "0", TokenType.NUMBER);
		assertConvenient(tokenizer, ";", TokenType.TERMINATOR);
	}

	protected void assertConvenient(Tokenizer tokenizer, String org, TokenType type) {
		Token tmp = tokenizer.next();
		assertThat(tmp.getOriginal(), is(org));
		assertThat(tmp.getType(), is(type));
	}

	protected String pathToString(String name) throws IOException {
		Path path = Paths.get("target/test-classes/", name);
		return Files.readAllLines(path, StandardCharsets.UTF_8).stream().collect(Collectors.joining());
	}
}
