package codility_lessons;


/**
 * Count the minimum number of nails that allow a series of planks to be nailed
 */
// https://codility.com/programmers/lessons/14-binary_search_algorithm/nailing_planks/
public class NailingPlanks {
    private boolean check(int[] plankStarts, int[] plankEnds, int[] nails, int nailsToUse) {
        int plankCount = plankStarts.length;
        plankLoop : for (int p = 0;p < plankCount; p++) {
            nailLoop : for (int n = 0;n < nailsToUse;n++) {
                if (plankStarts[p] <= nails[n] && nails[n] <= plankEnds[p]) {
                    continue plankLoop;
                }
            }
            return false;
        }
        return true;
    }
    public int solve(int[] plankStarts, int[] plankEnds, int[] nails) {
        int worst = nails.length;
        int best = 1;
        int result = -1;
        while (worst >= best) {
            int guess = (worst + best) / 2;
            if (check(plankStarts, plankEnds, nails, guess)) {
                result = guess;
                worst = guess - 1;
            } else {
                best = guess + 1;
            }
        }
        return result;
    }
}
