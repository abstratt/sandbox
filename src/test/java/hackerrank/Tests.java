package hackerrank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static hackerrank.RomanConverter.toRoman;

import org.junit.jupiter.api.Test;

public class Tests {

    @Test
    public void test() {
        assertEquals("X", toRoman(10));                
        assertEquals("I", toRoman(1));
        assertEquals("II", toRoman(2));
        assertEquals("III", toRoman(3));
        assertEquals("IV", toRoman(4));
        assertEquals("V", toRoman(5));
        assertEquals("VI", toRoman(6));
        assertEquals("VII", toRoman(7));
        assertEquals("VIII", toRoman(8));
        assertEquals("IX", toRoman(9));
        assertEquals("XIV", toRoman(14));
        assertEquals("XXXVI", toRoman(36));
        assertEquals("L", toRoman(50));
        assertEquals("LXVII", toRoman(67));
        assertEquals("C", toRoman(100));
        assertEquals("CLXXXIX", toRoman(189));
        assertEquals("DLXVII", toRoman(567));
        assertEquals("CMLXXXVII", toRoman(987));
        assertEquals("M", toRoman(1000));
        
    }
    
    @Test
    public void bstValidatorTest() {
        assertFalse(BSTTraversalValidator.isValid("1 3 4 2"));
        assertTrue(BSTTraversalValidator.isValid("1 2 3"));
        assertTrue(BSTTraversalValidator.isValid("1 3 2"));
        assertTrue(BSTTraversalValidator.isValid("2 1 3"));
        assertTrue(BSTTraversalValidator.isValid("3 2 1 5 4 6"));
        assertFalse(BSTTraversalValidator.isValid("3 4 5 1 2"));
    }

}
