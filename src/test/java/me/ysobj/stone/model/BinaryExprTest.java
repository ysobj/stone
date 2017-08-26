package me.ysobj.stone.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import me.ysobj.stone.tokenizer.Tokenizer;

public class BinaryExprTest {

	@Test
	public void test() {
		ASTNode left = new NumberLiteral(new Tokenizer("1").next());
		ASTNode right = new NumberLiteral(new Tokenizer("2").next());
		BinaryExpression x = new BinaryExpression(left, new PlusOperator(), right);
		Number n = (Number) x.evaluate(new Context());
		assertEquals(n.intValue(), 3);
	}

}
