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
	public void test() throws ParseException {
		Parser parser = new StoneParser();
		Context context = new Context();
		ASTNode astNode =  parser.parse(new Tokenizer("hoge = 123", new String[] {}, new String[] {"="}));
		astNode.evaluate(context);
		assertThat(context.get("hoge"), is(123L));
	}

}
