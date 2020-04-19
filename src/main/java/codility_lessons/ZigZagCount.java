package codility_lessons;

public class ZigZagCount {
    public static class Tree {
        public Tree(int x, Tree l, Tree r) {
            this.x = x;
            this.l = l;
            this.r = r;
        }
        public Tree(int x) {
            this.x = x;
        }
        public int x;
        public Tree l;
        public Tree r;
    }
    
    static int NONE = 0;
    static int LEFT = 1;
    static int RIGHT = -1;
    
    public int solve(Tree tree) {
        if (tree == null) {
            return 0;
        }
        return countMaxZigZags(tree, NONE);
    }

    private int countMaxZigZags(Tree tree, int direction) {
        int leftCount = 0;
        int rightCount = 0;
        if (tree.l != null) {
            leftCount = countMaxZigZags(tree.l, LEFT);
            if (direction == RIGHT) {
                leftCount++;
            }
        }
        if (tree.r != null) {
            rightCount = countMaxZigZags(tree.r, RIGHT);
            if (direction == LEFT) {
                rightCount++;
            }
        }
        return Math.max(leftCount, rightCount);
    }
}
