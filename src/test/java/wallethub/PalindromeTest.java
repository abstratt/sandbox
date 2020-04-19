package wallethub;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PalindromeTest {

    @Test
    public void testPositive() {
        Palindrome palindrome = new Palindrome();
        assertTrue(palindrome.check("a"));
        assertTrue(palindrome.check("aba"));
        assertTrue(palindrome.check("abba"));
        assertTrue(palindrome.check("abcba"));
        assertTrue(palindrome.check("aabbaa"));
        assertTrue(palindrome.check(""));
    }

    @Test
    public void testNegative() {
        Palindrome palindrome = new Palindrome();
        assertFalse(palindrome.check("ab"));
        assertFalse(palindrome.check("abbab"));
        assertFalse(palindrome.check("abcdba"));
    }

}
