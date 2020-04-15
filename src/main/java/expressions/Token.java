package expressions;

public class Token {
	private final String text;
	private final TokenType type;

	private Token(String text, TokenType type) {
		this.text = text;
		this.type = type;
	}

	public TokenType getType() {
		return type;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return text;
	}
	
	public static Token asToken(String asText) {
		return new Token(asText, TokenType.forText(asText));
	}
}