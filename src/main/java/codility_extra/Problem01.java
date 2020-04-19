package codility_extra;

public class Problem01 {
    public int solve(int a, int b) {
        int dx = Math.abs(a);
        int dy = Math.abs(b);
        final int limit = 100000000;
        double moves = Math.max(Math.floor((dx+1)/2), Math.max(Math.floor((dy+1)/2), Math.floor((dx+dy+2)/3)));
        while ((moves%2)!=(dx+dy)%2 && moves <= limit) moves++;
        return moves <= limit ? (int) moves : -2;
    }
}
