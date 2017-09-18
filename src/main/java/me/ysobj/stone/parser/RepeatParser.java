/**
 * Project : sqlparser
 * Date : 2017/04/05 kougo
 *
 * Copyright 2017 TechMatrix Corporation, Inc. All rights reserved.
 */
package me.ysobj.stone.parser;

import java.util.ArrayList;
import java.util.List;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.ASTNodeList;
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
		List<ASTNode> nodes = new ArrayList<>();
		try {
			while (true) {
				nodes.add(parser.parse(tokenizer));
			}
		} catch (ParseException e) {
		}
		if(nodes.size() == 0) {
			return null;
		}
		return new ASTNodeList(nodes.toArray(new ASTNode[] {}));
	}

	@Override
	public String toString() {
		return parser.toString() + "*";
	}

}
