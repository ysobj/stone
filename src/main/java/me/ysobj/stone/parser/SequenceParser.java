package me.ysobj.stone.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
		List<ASTNode> children = new ArrayList<>();
		for (int i = 0; i < parsers.length; i++) {
			Parser parser = parsers[i];
			ASTNode tmp = parser.parse(tokenizer);
			if (tmp != null) {
				accept = true;
				children.add(tmp);
			}
		}
		if (accept) {
			if (children.size() == 0) {
				return null;
			}
			return build(children.toArray(new ASTNode[children.size()]));
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

	public void add(Parser parser) {
		Parser[] tmp = new Parser[this.parsers.length + 1];
		System.arraycopy(this.parsers, 0, tmp, 0, this.parsers.length);
		tmp[this.parsers.length] = parser;
		this.parsers = tmp;
	}
}
