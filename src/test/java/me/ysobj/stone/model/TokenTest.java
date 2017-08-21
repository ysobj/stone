package me.ysobj.stone.model;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class TokenTest {

	@Test
	public void testEquals() {
		Token t1 = Token.create("tEsT");
		Token t2 = Token.create("TeSt");
		assertThat(t1, is(t2));
	}

}
