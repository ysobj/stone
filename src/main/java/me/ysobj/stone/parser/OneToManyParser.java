package me.ysobj.stone.parser;

import java.util.ArrayList;
import java.util.List;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.ASTNodeList;
import me.ysobj.stone.tokenizer.Tokenizer;

public class OneToManyParser implements Parser {
	private Parser parser;
	private Parser separator;

	public OneToManyParser(Parser parser, Parser separator) {
		super();
		this.parser = parser;
		this.separator = separator;
	}

	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		List<ASTNode> nodes = new ArrayList<>();
		nodes.add(parser.parse(tokenizer));
		while (true) {
			try {
				separator.parse(tokenizer);
			} catch (ParseException e) {
				break;
			}
			nodes.add(parser.parse(tokenizer));
		}
		return new ASTNodeList(nodes.toArray(new ASTNode[] {}));
	}

}
