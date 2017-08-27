package me.ysobj.stone.parser;

import java.util.Arrays;
import java.util.StringJoiner;

import me.ysobj.stone.exception.ParseException;
import me.ysobj.stone.model.ASTNode;
import me.ysobj.stone.model.ASTNodeList;
import me.ysobj.stone.tokenizer.Tokenizer;

public class SequenceParser implements Parser {
	private Parser[] parsers;

	public SequenceParser(Parser... parsers) {
		this.parsers = parsers;
	}

	@Override
	public ASTNode parse(Tokenizer tokenizer) throws ParseException {
		boolean accept = false;
		ASTNode[] children = new ASTNode[parsers.length];
		for (int i = 0; i < parsers.length; i++) {
			Parser parser = parsers[i];
			ASTNode tmp = parser.parse(tokenizer);
			if (tmp != null) {
				accept = true;
				children[i] = tmp;
			}
		}
		if (accept) {
			return build(children);
		} else {
			throw new ParseException();
		}
	}

	protected ASTNode build(ASTNode[] children) {
		return new ASTNodeList(children);
	}

	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(" ");
		joiner.setEmptyValue("");
		Arrays.stream(parsers).forEach(p -> joiner.add(p.toString()));
		return joiner.toString();
	}

}
