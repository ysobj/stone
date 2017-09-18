package me.ysobj.stone.parser;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import org.junit.Test;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.ASTNodeList;
import me.ysobj.stone.tokenizer.Tokenizer;

public class RepeatParserTest {

	@Test
	public void test1() throws ParseException {
		RepeatParser parser = new RepeatParser(new KeywordParser("A"));
		ASTNodeList nodeList = (ASTNodeList) parser
				.parse(new Tokenizer("A A A A A", new String[] {}, new String[] { "A" }));

		assertThat(nodeList.getNodes().length, is(5));
	}

	@Test
	public void test2() throws ParseException {
		Parser keyword = new KeywordParser("A");
		RepeatParser repOption = new RepeatParser(new SequenceParser(new KeywordParser(","), keyword));
		Parser parser = new SequenceParser(keyword, repOption);
		ASTNodeList nodeList = (ASTNodeList) parser
				.parse(new Tokenizer("A,A,A,A,A", new String[] {}, new String[] { "A" }));

		assertThat(nodeList.getNodes().length, is(2));
		ASTNode node = nodeList.getNodes()[1];
		System.out.println(node);
	}

}
