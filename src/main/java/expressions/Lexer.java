package expressions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class Lexer {
	private Token[] tokens;
	private int index;

	public Lexer(String[] tokens) {
		this.tokens = Arrays.stream(tokens).map(this::asToken).toArray(Token[]::new);
	}
	public Lexer(Token[] tokens) {
		this.tokens = tokens;
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
		StringTokenizer tokenizer = new StringTokenizer(toScan, "+-*/ )(", true);
		List<String> tokens = new ArrayList<>();
		while (tokenizer.hasMoreTokens()) {
			String nextToken = tokenizer.nextToken();
			if (!" ".equals(nextToken)) {
				tokens.add(nextToken);
			}
		}
		return new Lexer(tokens.stream().map(Token::asToken).toArray(Token[]::new));
	}

	@Override
	public String toString() {
		return Arrays.stream(tokens).skip(index).map(Token::toString).collect(Collectors.joining(" "));
	}

	public void unget() {
		index--;
	}
}