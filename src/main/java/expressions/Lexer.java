package expressions;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Lexer {
	private Token[] tokens;
	private int index;

	public Lexer(String[] tokens) {
		this.tokens = Arrays.stream(tokens).map(this::asToken).toArray(Token[]::new);
	}
	
	private Token asToken(String asText) {
		return Token.asToken(asText);
	}

	public Token getNext() {
		return index < tokens.length ? tokens[index++] : null;
	}
	
	public boolean hasNext() {
		return index < tokens.length;
	}

	public static Lexer analyze(String toScan) {
		return new Lexer(toScan.split(" "));
	}
	
	@Override
	public String toString() {
		return Arrays.stream(tokens).skip(index).map(Token::toString).collect(Collectors.joining(" "));
	}

	public void unget() {
		index--;
	}
}