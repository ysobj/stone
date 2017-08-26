package me.ysobj.stone.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import me.ysobj.stone.tokenizer.Tokenizer;

public class BinaryExprTest {

	@Test
	public void testPlus() {
		ASTNode left = new NumberLiteral(new Tokenizer("1").next());
		ASTNode right = new NumberLiteral(new Tokenizer("2").next());
		BinaryExpression x = new BinaryExpression(left, new PlusOperator(), right);
		Number n = (Number) x.evaluate(new Context());
		assertEquals(3, n.intValue());
	}

	@Test
	public void testMinus() {
		ASTNode left = new NumberLiteral(new Tokenizer("5").next());
		ASTNode right = new NumberLiteral(new Tokenizer("3").next());
		BinaryExpression x = new BinaryExpression(left, new MinusOperator(), right);
		Number n = (Number) x.evaluate(new Context());
		assertEquals(2, n.intValue());
	}

	@Test
	public void testMultiply() {
		ASTNode left = new NumberLiteral(new Tokenizer("5").next());
		ASTNode right = new NumberLiteral(new Tokenizer("3").next());
		BinaryExpression x = new BinaryExpression(left, new MultiplyOperator(), right);
		Number n = (Number) x.evaluate(new Context());
		assertEquals(15, n.intValue());
	}

	@Test
	public void testDivide() {
		ASTNode left = new NumberLiteral(new Tokenizer("6").next());
		ASTNode right = new NumberLiteral(new Tokenizer("3").next());
		BinaryExpression x = new BinaryExpression(left, new DivideOperator(), right);
		Number n = (Number) x.evaluate(new Context());
		assertEquals(2, n.intValue());
	}

}
