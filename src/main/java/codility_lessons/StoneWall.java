package codility_lessons;

import java.util.Stack;

/**
 * Cover "Manhattan skyline" using the minimum number of rectangles
 */
// https://codility.com/programmers/lessons/7-stacks_and_queues/stone_wall/
public class StoneWall {
    public int solve(int[] heights) {
        int totalBlocks = 0;    
        Stack<Integer> blockHeights = new Stack<>();
        int currentHeight = 0;
        blockHeights.push(currentHeight);
        for (int offset = 0; offset < heights.length;) {
            currentHeight = blockHeights.peek();
            if (heights[offset] < currentHeight) {
                while (heights[offset] < currentHeight) {
                    blockHeights.pop();
                    currentHeight = blockHeights.peek();
                }
            } else {
                if (heights[offset] > currentHeight) {
                    totalBlocks++;
                    blockHeights.push(heights[offset]);
                } 
                offset++;
            }
        }
        return totalBlocks;
    }
}
