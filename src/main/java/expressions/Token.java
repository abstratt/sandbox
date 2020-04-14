package expressions;

public class Token {
	String text;
	TokenType type;

	private Token(String text, TokenType type) {
		this.text = text;
		this.type = type;
	}

	public static Token asToken(String asText) {
		return new Token(asText, TokenType.forText(asText));
	}

	@Override
	public String toString() {
		return text;
	}
}