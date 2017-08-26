package me.ysobj.stone.model;

import static org.junit.Assert.*;

import static org.hamcrest.core.Is.is;
import org.junit.Test;

import me.ysobj.stone.tokenizer.Tokenizer;

public class AssignTest {

	@Test
	public void test() {
		Context context = new Context();
		Name name = new Name(new Tokenizer("hoge").next());
		ASTNode val = new NumberLiteral(new Tokenizer("123").next());
		new Assign(name,val).evaluate(context);
		assertThat(context.get("hoge"), is(123L));
	}

}
