package nqueens;

public class InvalidSolutionException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	enum Reason {
		NotEnoughQueens, QueensUnderThreat, ThreeQueensOnSameLine, Unsolvable
	}

	private Reason reason;

	public InvalidSolutionException(Reason reason) {
		this(reason, "");
	}

	public InvalidSolutionException(Reason reason, String message) {
		super(reason + " - " + message);
		this.reason = reason;
	}

	public Reason getReason() {
		return reason;
	}
}