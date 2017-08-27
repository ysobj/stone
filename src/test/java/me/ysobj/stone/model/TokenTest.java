package me.ysobj.stone.model;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import me.ysobj.stone.model.Token.TokenType;

public class TokenTest {

	@Test
	public void testEquals() {
		Token t1 = Token.create("tEsT",0,TokenType.KEYWORD);
		Token t2 = Token.create("TeSt",0,TokenType.KEYWORD);
		assertThat(t1, is(t2));
	}

}
