/**
 * Project : sqlparser
 * Date : 2017/04/05 kougo
 *
 * Copyright 2017 TechMatrix Corporation, Inc. All rights reserved.
 */
package me.ysobj.stone.parser;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.tokenizer.Tokenizer;

/**
 * <code>RepeatParser</code>クラスは○○するクラスです。
 * <p>
 * TODO クラスを説明するJavadocが未記載です。
 * 
 * @author kougo
 */
public class RepeatParser implements Parser {
	private Parser parser;

	/**
	 * コンストラクター
	 *
	 * @param keywordParser
	 */
	public RepeatParser(Parser parser) {
		this.parser = parser;
	}

	/**
	 * parseメソッドのオーバーライド
	 *
	 * @param tokenizer
	 * @return
	 * @throws ParseException
	 * @see me.ysobj.sqlparser.parser.Parser#parse(me.ysobj.sqlparser.tokenizer.Tokenizer)
	 */
	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		parser.parse(tokenizer);
		try {
			while (true) {
				parser.parse(tokenizer);
			}
		} catch (ParseException e) {
		}
		return new ASTNode();
	}

	@Override
	public String toString() {
		return parser.toString() + "*";
	}

}
