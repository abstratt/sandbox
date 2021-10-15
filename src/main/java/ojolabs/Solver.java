package ojolabs;

public class Solver {

	public int computeWater(int... heights) {
		int[] tallestToTheLeft = new int[heights.length];
		int[] tallestToTheRight = new int[heights.length];
		int maxToLeft = 0;
		int maxToRight = 0;
		for (int i = 0; i < heights.length; i++) {
			if (heights[i] > maxToLeft) {
				maxToLeft = heights[i]; 
			}
			tallestToTheLeft[i] = maxToLeft;
		}
		for (int i = heights.length - 1;i >= 0; i--) {
			if (heights[i] > maxToRight) {
				maxToRight = heights[i]; 
			}
			tallestToTheRight[i] = maxToRight;
		}
		int total = 0;
		for (int i = 0; i < heights.length; i++) {
			if (heights[i] < tallestToTheLeft[i] && heights[i] < tallestToTheRight[i])
				total += (Math.min(tallestToTheLeft[i], tallestToTheRight[i]) - heights[i]);
		}
		return total;
	}
}
