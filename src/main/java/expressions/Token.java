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
	
	enum TokenType {
		Int, Decimal, Plus, Minus, MultOrDiv, OpenBrace, CloseBrace;

		static TokenType forText(String asText) {
			if ("(".equals(asText)) {
				return OpenBrace;
			}
			if (")".equals(asText)) {
				return CloseBrace;
			}
			if ("*".equals(asText) || "/".equals(asText)) {
				return MultOrDiv;
			}
			if ("+".equals(asText)) {
				return Plus;
			}
			if ("-".equals(asText)) {
				return Minus;
			}
			return asText.contains(".") ? Decimal : Int;
		}
	}
}