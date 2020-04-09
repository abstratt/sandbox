package nqueens;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import nqueens.InvalidSolutionException.Reason;

public class BoardTest {
	@Test
	public void fiveSquareBoard() {
		BitSet asBitset = new BitSet();
		asBitset.set(0);
		asBitset.set(6);
		asBitset.set(12);
		asBitset.set(18);
		asBitset.set(24);
		checkBoardContents(Arrays.asList(//
				new Square(0, 0), //
				new Square(1, 1), //
				new Square(2, 2), //
				new Square(3, 3), //
				new Square(4, 4) //
		), new Board(5, asBitset));
	}

	@Test
	public void fourSquareBoard() {
		BitSet asBitSet = new BitSet();
		asBitSet.set(0);
		asBitSet.set(5);
		asBitSet.set(10);
		asBitSet.set(15);
		checkBoardContents(Arrays.asList(//
				new Square(0, 0), //
				new Square(1, 1), //
				new Square(2, 2), //
				new Square(3, 3)//
		), new Board(4, asBitSet));
	}

	private void checkBoardContents(Collection<Square> expected, Board board) {
		assertEquals(new LinkedHashSet<>(expected), board.getOccupied(), board::toString);
	}

	@Test
	public void checkSolution() {
		new Board(4, //
				new Square(1, 0), //
				new Square(3, 1), //
				new Square(0, 2), //
				new Square(2, 3) //
		).checkSolution();
		new Board(8, //
				new Square(3, 0), //
				new Square(1, 1), //
				new Square(7, 2), //
				new Square(4, 3), //
				new Square(6, 4), //
				new Square(0, 5), //
				new Square(2, 6), //
				new Square(5, 7) //
		).checkSolution();
	}

	private void checkBadSolution(Consumer<Reason> consumer, Board board) {
		try {
			board.checkSolution();
			fail(() -> "should have failed");
		} catch (InvalidSolutionException e) {
			consumer.accept(e.getReason());
		}
	}

	@Test
	public void errorReason() {
		checkBadSolution(Reason.NotEnoughQueens::equals, new Board(4, //
				new Square(1, 1), //
				new Square(3, 1), //
				new Square(0, 2) //
		));
		checkBadSolution(Reason.QueensUnderThreat::equals, new Board(4, //
				new Square(1, 1), //
				new Square(3, 1), //
				new Square(0, 2), //
				new Square(2, 1) //
		));
		checkBadSolution(Reason.ThreeQueensOnSameLine::equals, new Board(8, //
				new Square(6, 0), //
				new Square(4, 1), //
				new Square(2, 2), //
				new Square(0, 3), //
				new Square(5, 4), //
				new Square(7, 5), //
				new Square(1, 6), //
				new Square(3, 7) //
		));
	}

}
