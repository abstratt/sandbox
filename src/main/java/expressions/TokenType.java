package expressions;

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