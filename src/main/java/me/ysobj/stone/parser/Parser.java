package me.ysobj.stone.parser;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.tokenizer.Tokenizer;

public interface Parser {
	ASTNode parse(Tokenizer tokenizer) throws ParseException;
}
