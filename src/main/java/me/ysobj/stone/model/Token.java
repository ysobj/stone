package me.ysobj.stone.model;

public class Token {

	public static enum TokenType {
		KEYWORD, IDENTIFIER, STRING, NUMBER, OPERATOR, COMMA, PAREN_OPEN, PAREN_CLOSE, BRACE_OPEN, BRACE_CLOSE, TERMINATOR, OTHER
	}

	public static final Token EOF = new Token("", TokenType.OTHER, 0);
	private int startPos;
	private String original;
	private String normalize;
	private TokenType type;

	private Token(String str, TokenType type, int startPos) {
		this.original = str;
		this.normalize = str != null ? str.toUpperCase() : str;
		this.type = type;
		this.startPos = startPos;
	}

	public String getOriginal() {
		return original;
	}

	public TokenType getType() {
		return type;
	}

	public static Token create(String str, int start, TokenType type) {
		return new Token(str, type, start);
	}

	@Override
	public String toString() {
		return "Token [normalize=" + normalize + ", type=" + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((normalize == null) ? 0 : normalize.toUpperCase().hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	public String getNormalize() {
		return normalize;
	}

	public int getStartPos() {
		return startPos;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Token)) {
			return false;
		}
		Token subject = (Token) obj;
		if (!this.getType().equals(subject.getType())) {
			return false;
		}
		return (this.getNormalize().equals(subject.getNormalize().toUpperCase()));
	}

}
