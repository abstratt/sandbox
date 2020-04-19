package wallethub;

public class Palindrome {
    public boolean check(String toCheck) {
        char[] asArray = toCheck.toCharArray();
        int limit = asArray.length / 2;
        for (int i = 0; i < limit; i++) {
            if (asArray[i] != asArray[asArray.length - 1 - i]) {
                return false;
            }
        }
        return true;
    }
}
