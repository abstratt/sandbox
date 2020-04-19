package hackerrank;

public class BSTTraversalValidator {

    public static boolean isValid(String string) {
        char[] asChars = string.replaceAll(" ",  "").toCharArray();
        return isValid(asChars, 0, asChars.length - 1, asChars[0]);
    }

    private static boolean isValid(char[] chars, int startIndex, int endIndex, char pivotValue) {
        int length = endIndex - startIndex + 1;
        if (length <= 1) {
            return true;
        }
        int firstGreater = -1;
        for (int i = 0; i <= endIndex; i++) {
            if (pivotValue < chars[i]) {
                firstGreater = i; 
                break;
            }
        }
        if (firstGreater == -1 || firstGreater == startIndex) {
            // all lower or higher
            return isValid(chars, startIndex+1, endIndex, chars[startIndex]);
        }
        for (int i = firstGreater; i <= endIndex; i++) {
            if (pivotValue > chars[i]) {
                return false;
            }
        }
        return isValid(chars, startIndex+2, firstGreater-1, chars[startIndex+1]) & isValid(chars, firstGreater, endIndex, chars[firstGreater]);
    }

}
