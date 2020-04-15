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
		String noSpaces = toScan.replaceAll(" ", "");
		StringTokenizer tokenizer = new StringTokenizer(noSpaces, "+-*/()", true);
		List<Token> tokens = new ArrayList<>();
		while (tokenizer.hasMoreTokens()) {
			tokens.add(Token.asToken(tokenizer.nextToken()));
		}
		return new Lexer(tokens.toArray(new Token[0]));
	}

	@Override
	public String toString() {
		return Arrays.stream(tokens).skip(index).map(Token::toString).collect(Collectors.joining(" "));
	}

	public void unget() {
		index--;
	}
}