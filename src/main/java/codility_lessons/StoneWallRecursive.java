package codility_lessons;

//https://codility.com/programmers/lessons/7-stacks_and_queues/stone_wall/

// fails with SOE for high stacks
public class StoneWallRecursive {
    public int solve(int[] heights) {
        int totalBlocks = 0;
        int totalLength = 0;
        int requiredLength = heights.length;
        while (requiredLength > totalLength) {
            BlockStack result = buildBlockStack(heights, totalLength, 0);
            totalBlocks += result.blockCount;
            totalLength += result.length;        
        }
        return totalBlocks;
    }
    
    private static class BlockStack {
        int length = 1;
        int blockCount = 1;
        private void add(BlockStack childStack) {
            this.blockCount += childStack.blockCount;
            this.length += childStack.length;
        }
        private void extend(int lengthToAdd) {
            length += lengthToAdd;
        }
    }
    
    private BlockStack buildBlockStack(int[] heights, int offset, int baseHeight) {
        int requiredLength = heights.length;
        int requiredHeight = heights[offset];
        BlockStack thisStack = new BlockStack();
        for (int currentOffset = offset + 1; currentOffset < requiredLength && heights[currentOffset] >= requiredHeight; currentOffset = offset + thisStack.length) {
            if (heights[currentOffset] > requiredHeight) {
                BlockStack childStack = buildBlockStack(heights, currentOffset, requiredHeight);
                thisStack.add(childStack);
            } else {
                thisStack.extend(1);
            }
        }
        return thisStack;
    }

}
