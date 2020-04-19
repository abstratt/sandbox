package hackerrank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class Tests {

    @Test
    public void test() {
        RomanConverter roman = new RomanConverter();
        assertEquals("X", roman.toRoman(10));                
        assertEquals("I", roman.toRoman(1));
        assertEquals("II", roman.toRoman(2));
        assertEquals("III", roman.toRoman(3));
        assertEquals("IV", roman.toRoman(4));
        assertEquals("V", roman.toRoman(5));
        assertEquals("VI", roman.toRoman(6));
        assertEquals("VII", roman.toRoman(7));
        assertEquals("VIII", roman.toRoman(8));
        assertEquals("IX", roman.toRoman(9));
        assertEquals("XIV", roman.toRoman(14));
        assertEquals("XXXVI", roman.toRoman(36));
        assertEquals("L", roman.toRoman(50));
        assertEquals("LXVII", roman.toRoman(67));
        assertEquals("C", roman.toRoman(100));
        assertEquals("CLXXXIX", roman.toRoman(189));
        assertEquals("DLXVII", roman.toRoman(567));
        assertEquals("CMLXXXVII", roman.toRoman(987));
        assertEquals("M", roman.toRoman(1000));
        
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
