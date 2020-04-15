package expressions;

import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class Lexer {
	private Token[] tokens;
	private int index;

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
		Token[] tokens = Collections.list(tokenizer).stream()//
				.map(t -> Token.asToken((String) t))//
				.toArray(Token[]::new);
		return new Lexer(tokens);
	}

	@Override
	public String toString() {
		return Arrays.stream(tokens).skip(index).map(Token::toString).collect(Collectors.joining(" "));
	}

	public void unget() {
		index--;
	}
}