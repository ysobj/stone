package me.ysobj.stone.parser;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.tokenizer.Tokenizer;


public class ParserTest {
	@Test
	public void testTokenParser() throws Exception {
		Parser parser = new KeywordParser("select");
		assertThat(parser.parse(new Tokenizer("select")), not(nullValue()));
	}

	@Test
	public void testTokenParser2() throws Exception {
		Parser parser = new KeywordParser();
		assertThat(parser.parse(new Tokenizer("select")), not(nullValue()));
		assertThat(parser.parse(new Tokenizer("update")), not(nullValue()));
	}

	@Test(expected = ParseException.class)
	public void testTokenParserFailed() throws Exception {
		Parser parser = new KeywordParser("select");
		// throw ParseException
		parser.parse(new Tokenizer("update"));
	}

	@Test
	public void testChoiseParser() throws Exception {
		Parser p1 = new KeywordParser("select");
		Parser p2 = new KeywordParser("update");
		Parser p3 = new KeywordParser("delete");
		Parser parser = new ChoiceParser(p1, p2, p3);
		assertThat(parser.parse(new Tokenizer("select")), not(nullValue()));
		assertThat(parser.parse(new Tokenizer("update")), not(nullValue()));
		assertThat(parser.parse(new Tokenizer("delete")), not(nullValue()));
	}

	@Test(expected = ParseException.class)
	public void testChoiseParserFailed() throws Exception {
		Parser p1 = new KeywordParser("select");
		Parser p2 = new KeywordParser("update");
		Parser p3 = new KeywordParser("delete");
		Parser parser = new ChoiceParser(p1, p2, p3);
		// throw ParseException
		parser.parse(new Tokenizer("insert"));
	}

	@Test
	public void testSeaquenceParser() throws Exception {
		Parser p1 = new KeywordParser("select");
		Parser p2 = new KeywordParser("update");
		Parser p3 = new KeywordParser("delete");
		Parser parser = new SequenceParser(p1, p2, p3);
		assertThat(parser.parse(new Tokenizer("select update delete")), not(nullValue()));
	}

	@Test(expected = ParseException.class)
	public void testSeaquenceParserFailed() throws Exception {
		Parser p1 = new KeywordParser("select");
		Parser p2 = new KeywordParser("update");
		Parser p3 = new KeywordParser("delete");
		Parser parser = new SequenceParser(p1, p2, p3);
		// throw ParseException
		parser.parse(new Tokenizer("select delete update"));
	}

	@Test
	public void testCombination() throws Exception {
		// (A|B)CD
		Parser ab = new ChoiceParser(new KeywordParser("A"), new KeywordParser("B"));
		Parser parser = new SequenceParser(ab, new KeywordParser("C"), new KeywordParser("D"));
		assertThat(parser.parse(new Tokenizer("A C D")), not(nullValue()));
		assertThat(parser.parse(new Tokenizer("B C D")), not(nullValue()));
	}

	@Test(expected = ParseException.class)
	public void testCombinationFailed1() throws Exception {
		// (A|B)CD
		Parser ab = new ChoiceParser(new KeywordParser("A"), new KeywordParser("B"));
		Parser parser = new SequenceParser(ab, new KeywordParser("C"), new KeywordParser("D"));
		// throw ParseException
		parser.parse(new Tokenizer("A B C D"));
	}

	@Test(expected = ParseException.class)
	public void testCombinationFailed2() throws Exception {
		// (A|B)CD
		Parser ab = new ChoiceParser(new KeywordParser("A"), new KeywordParser("B"));
		Parser parser = new SequenceParser(ab, new KeywordParser("C"), new KeywordParser("D"));
		// throw ParseException
		parser.parse(new Tokenizer("C D"));
	}

	@Test
	public void testOptional() throws Exception {
		// A?
		Parser parser = new OptionalParser(new KeywordParser("A"));
		assertThat(parser.parse(new Tokenizer("A")), not(nullValue()));
		assertThat(parser.parse(new Tokenizer("B")), is(nullValue()));
	}

	@Test
	public void testOptional2() throws Exception {
		// A?B
		Parser parser = new SequenceParser(new OptionalParser(new KeywordParser("A")), new KeywordParser("B"));
		assertThat(parser.parse(new Tokenizer("A B")), not(nullValue()));
		assertThat(parser.parse(new Tokenizer("B")), not(nullValue()));
	}

	@Test
	public void testDelete() throws Exception {
		Parser delete = new KeywordParser("delete");
		Parser from = new KeywordParser("from");
		Parser any = new KeywordParser();
		Parser where = new OptionalParser(new SequenceParser(new KeywordParser("where"), new KeywordParser(),
				new KeywordParser("="), new KeywordParser()));
		Parser parser = new SequenceParser(delete, from, any, where);
		parser.parse(new Tokenizer("delete from hoge"));
		parser.parse(new Tokenizer("delete from fuga"));
		parser.parse(new Tokenizer("delete from fuga where A = B"));
	}

	@Test(expected = ParseException.class)
	public void testDelete2() throws Exception {
		Parser delete = new KeywordParser("delete");
		Parser from = new KeywordParser("from");
		Parser any = new KeywordParser();
		Parser parser = new SequenceParser(delete, from, any);
		// throw ParseException
		parser.parse(new Tokenizer("delete from"));
	}

	@Test
	public void testRepeatParser() throws Exception {
		RepeatParser parser = new RepeatParser(new KeywordParser("A"));
		assertThat(parser.parse(new Tokenizer("A A A")), not(nullValue()));
	}

	@Test
	public void testRepeatParser2() throws Exception {
		Parser parser = new SequenceParser(new KeywordParser("A"),
				new RepeatParser(new SequenceParser(new CommaParser(), new KeywordParser("A"))),
				new KeywordParser("B"));
		assertThat(parser.parse(new Tokenizer("A,A,A B")), not(nullValue()));
	}

	@Test
	public void testRepeatParser3() throws Exception {
		Parser parser = new SequenceParser(new KeywordParser("select"), new KeywordParser("A"),
				new OptionalParser(new RepeatParser(new SequenceParser(new CommaParser(), new KeywordParser("A")))),
				new KeywordParser("from"), new KeywordParser());
		assertThat(parser.parse(new Tokenizer("select A,A,A from hoge")), not(nullValue()));
		assertThat(parser.parse(new Tokenizer("select A from fuga")), not(nullValue()));
	}

}
