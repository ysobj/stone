package me.ysobj.stone.exception;

import me.ysobj.stone.model.Token;

public class ParseException extends Exception {
	private static final long serialVersionUID = 1L;
	private Token causeToken;

	public ParseException(Token token) {
		this.causeToken = token;
	}

	@Override
	public String getMessage() {
		if (causeToken != null) {
			return String.format("ParseException [%s] at: %d", causeToken.getOriginal(), causeToken.getStartPos());
		}
		return super.getMessage();
	}
}
