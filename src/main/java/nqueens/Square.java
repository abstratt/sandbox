package nqueens;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Square {
	int row;
	int column;
	public Square(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public boolean sameLineAs(Square pointA, Square pointB) {
		return sameLine(this.row, this.column, pointA.row, pointA.column, pointB.row, pointB.column);
	}

	static boolean sameLine(int rowA, int columnA, int rowB, int columnB, int rowC, int columnC) {
		double angleAB = computeAngleDouble(rowA, columnA, rowB, columnB);
		double angleAC = computeAngleDouble(rowA, columnA, rowC, columnC);
		return sameNumber(angleAB, angleAC);
	}
	
	public static boolean sameLine(Square a, Square b, Square c) {
		if (a.doesThreat(b) || a.doesThreat(c) || b.doesThreat(c)) {
			return a.doesThreat(b) && a.doesThreat(c) && b.doesThreat(c);
		}
		return sameLine(a.row, a.column, b.row, b.column, c.row, c.column);
	}
	
	public BigDecimal computeAngle(Square another) {
		BigDecimal angle = computeAngle(this, another);
		return angle;
	}

	private static BigDecimal computeAngle(Square a, Square b) {
		return computeAngle(a.row, a.column, b.row, b.column);
	}

	static BigDecimal computeAngle(int aRow, int aColumn, int bRow, int bColumn) {
		return new BigDecimal(aRow - bRow).divide(new BigDecimal(aColumn - bColumn), 10, RoundingMode.HALF_EVEN);
	}
	
	static double computeAngleDouble(int aRow, int aColumn, int bRow, int bColumn) {
		return ((double) (aRow - bRow))/(aColumn - bColumn);
	}

	public boolean doesThreat(Square another) {
		return this.row == another.row || this.column == another.column || sameDiagonal(another);
	}

	public boolean sameDiagonal(Square another) {
		int thisRow = this.row;
		int thisColumm = this.column;
		int anotherRow = another.row;
		int anotherColumn = another.column;
		return sameDiagonal(thisRow, thisColumm, anotherRow, anotherColumn);
	}

	static boolean sameDiagonal(int thisRow, int thisColumm, int anotherRow, int anotherColumn) {
		double angle = Math.abs(computeAngleDouble(thisRow, thisColumm, anotherRow, anotherColumn));
		return sameNumber(angle, 1d);
	}

	private static boolean sameNumber(double value1, double value2) {
		return Math.abs(value1 - value2) < 0.000001d;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Square other = (Square) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return row + ":" + column;
	}
}