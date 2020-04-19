package hackerrank;

public class RomanConverter {
    
    static String[] romanizer(int[] numbers) {
        String[] result = new String[numbers.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = toRoman(numbers[i]);
        }
        return result;
    }
    
    private static final char[] NO_CHARS = {};
    private static final char[][] symbols = {
        {'I', 'V', 'X'}, 
        {'X', 'L', 'C'}, 
        {'C', 'D', 'M'},
        {'M'}
    };
    private static final int[][] lawOfFormation = {
            {0},
            {0,0},
            {0,0,0},
            {0,1},
            {1},
            {1,0},
            {1,0,0},
            {1,0,0,0},
            {0, 2},
            {2}
        };
    public static String toRoman(int number) {
        assert number >= 1;
        assert number <= 1000;
        String numberAsString = Integer.toString(number);
        StringBuilder result = new StringBuilder();
        int length = numberAsString.length();
        for (int i = 0; i < length; i++) {
            result.append(digitToRoman(numberAsString.charAt(i), length - i));
        }
        return result.toString();
    }
    private static char[] digitToRoman(char digit, int position) {
        if (digit == '0') {
            return NO_CHARS;
        }
        int offset = digit - '1';
        int[] selected = lawOfFormation[offset];
        char[] romanChars = new char[selected.length];
        for (int i = 0; i < selected.length; i++) {
            romanChars[i] = symbols[position - 1][selected[i]];
        }
        return romanChars;
    }
}
