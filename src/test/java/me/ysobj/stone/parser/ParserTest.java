package me.ysobj.stone.parser;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.tokenizer.Tokenizer;

public class ParserTest {
	protected Tokenizer createTokenizer(String str) {
		Tokenizer tokenizer = new Tokenizer(str, new String[] {},
				new String[] { "select", "update", "delete", "insert", "from", "A", "B", "C", "D" });
		return tokenizer;
	}

	@Test
	public void testTokenParser() throws Exception {
		Parser parser = new KeywordParser("select");
		assertThat(parser.parse(createTokenizer("select")), not(nullValue()));
	}

	@Test
	public void testTokenParser2() throws Exception {
		Parser parser = new KeywordParser();
		assertThat(parser.parse(createTokenizer("select")), not(nullValue()));
		assertThat(parser.parse(createTokenizer("update")), not(nullValue()));
	}

	@Test(expected = ParseException.class)
	public void testTokenParserFailed() throws Exception {
		Parser parser = new KeywordParser("select");
		// throw ParseException
		parser.parse(createTokenizer("update"));
	}

	@Test
	public void testChoiseParser() throws Exception {
		Parser p1 = new KeywordParser("select");
		Parser p2 = new KeywordParser("update");
		Parser p3 = new KeywordParser("delete");
		Parser parser = new ChoiceParser(p1, p2, p3);
		assertThat(parser.parse(createTokenizer("select")), not(nullValue()));
		assertThat(parser.parse(createTokenizer("update")), not(nullValue()));
		assertThat(parser.parse(createTokenizer("delete")), not(nullValue()));
	}

	@Test(expected = ParseException.class)
	public void testChoiseParserFailed() throws Exception {
		Parser p1 = new KeywordParser("select");
		Parser p2 = new KeywordParser("update");
		Parser p3 = new KeywordParser("delete");
		Parser parser = new ChoiceParser(p1, p2, p3);
		// throw ParseException
		parser.parse(createTokenizer("insert"));
	}

	@Test
	public void testSeaquenceParser() throws Exception {
		Parser p1 = new KeywordParser("select");
		Parser p2 = new KeywordParser("update");
		Parser p3 = new KeywordParser("delete");
		Parser parser = new SequenceParser(p1, p2, p3);
		assertThat(parser.parse(createTokenizer("select update delete")), not(nullValue()));
	}

	@Test(expected = ParseException.class)
	public void testSeaquenceParserFailed() throws Exception {
		Parser p1 = new KeywordParser("select");
		Parser p2 = new KeywordParser("update");
		Parser p3 = new KeywordParser("delete");
		Parser parser = new SequenceParser(p1, p2, p3);
		// throw ParseException
		parser.parse(createTokenizer("select delete update"));
	}

	@Test
	public void testCombination() throws Exception {
		// (A|B)CD
		Parser ab = new ChoiceParser(new KeywordParser("A"), new KeywordParser("B"));
		Parser parser = new SequenceParser(ab, new KeywordParser("C"), new KeywordParser("D"));
		assertThat(parser.parse(createTokenizer("A C D")), not(nullValue()));
		assertThat(parser.parse(createTokenizer("B C D")), not(nullValue()));
	}

	@Test(expected = ParseException.class)
	public void testCombinationFailed1() throws Exception {
		// (A|B)CD
		Parser ab = new ChoiceParser(new KeywordParser("A"), new KeywordParser("B"));
		Parser parser = new SequenceParser(ab, new KeywordParser("C"), new KeywordParser("D"));
		// throw ParseException
		parser.parse(createTokenizer("A B C D"));
	}

	@Test(expected = ParseException.class)
	public void testCombinationFailed2() throws Exception {
		// (A|B)CD
		Parser ab = new ChoiceParser(new KeywordParser("A"), new KeywordParser("B"));
		Parser parser = new SequenceParser(ab, new KeywordParser("C"), new KeywordParser("D"));
		// throw ParseException
		parser.parse(createTokenizer("C D"));
	}

	@Test
	public void testOptional() throws Exception {
		// A?
		Parser parser = new OptionalParser(new KeywordParser("A"));
		assertThat(parser.parse(createTokenizer("A")), not(nullValue()));
		assertThat(parser.parse(createTokenizer("B")), is(nullValue()));
	}

	@Test
	public void testOptional2() throws Exception {
		// A?B
		Parser parser = new SequenceParser(new OptionalParser(new KeywordParser("A")), new KeywordParser("B"));
		assertThat(parser.parse(createTokenizer("A B")), not(nullValue()));
		assertThat(parser.parse(createTokenizer("B")), not(nullValue()));
	}

	@Test
	public void testDelete() throws Exception {
		Parser delete = new KeywordParser("delete");
		Parser from = new KeywordParser("from");
		Parser any = new IdentifierParser();
		Parser where = new OptionalParser(new SequenceParser(new KeywordParser("where"), new IdentifierParser(),
				new KeywordParser("="), new IdentifierParser()));
		Parser parser = new SequenceParser(delete, from, any, where);
		parser.parse(createTokenizer("delete from hoge"));
		parser.parse(createTokenizer("delete from fuga"));
		parser.parse(createTokenizer("delete from fuga where x = y"));
	}

	@Test(expected = ParseException.class)
	public void testDelete2() throws Exception {
		Parser delete = new KeywordParser("delete");
		Parser from = new KeywordParser("from");
		Parser any = new KeywordParser();
		Parser parser = new SequenceParser(delete, from, any);
		// throw ParseException
		parser.parse(createTokenizer("delete from"));
	}

	@Test
	public void testParseExceptionMessage() throws Exception {
		Parser delete = new KeywordParser("delete");
		Parser from = new KeywordParser("from");
		Parser any = new IdentifierParser();
		Parser parser = new SequenceParser(delete, from, any);
		try {
			parser.parse(createTokenizer("delete from 12345"));
		} catch (ParseException e) {
			assertThat(e.getMessage(), is("ParseException [12345] at: 12"));
		}
	}

	@Test
	public void testRepeatParser() throws Exception {
		RepeatParser parser = new RepeatParser(new KeywordParser("A"));
		assertThat(parser.parse(createTokenizer("A A A")), not(nullValue()));
	}

	@Test
	public void testRepeatParser2() throws Exception {
		Parser parser = new SequenceParser(new KeywordParser("A"),
				new RepeatParser(new SequenceParser(new CommaParser(), new KeywordParser("A"))),
				new KeywordParser("B"));
		assertThat(parser.parse(createTokenizer("A,A,A B")), not(nullValue()));
	}

	@Test
	public void testRepeatParser3() throws Exception {
		Parser parser = new SequenceParser(new KeywordParser("select"), new KeywordParser("A"),
				new OptionalParser(new RepeatParser(new SequenceParser(new CommaParser(), new KeywordParser("A")))),
				new KeywordParser("from"), new IdentifierParser());
		assertThat(parser.parse(createTokenizer("select A,A,A from hoge")), not(nullValue()));
		assertThat(parser.parse(createTokenizer("select A from fuga")), not(nullValue()));
	}
}
