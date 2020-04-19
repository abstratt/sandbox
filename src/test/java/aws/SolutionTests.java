package aws;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class SolutionTests {

    @Test
    public void solution1() {
        Solutions solution = new Solutions();
        assertEquals(15243, solution.solution1(12345));
        assertEquals(1423, solution.solution1(1234));
        assertEquals(10, solution.solution1(10));
        assertEquals(1, solution.solution1(1));
        assertEquals(0, solution.solution1(0));
        assertEquals(132, solution.solution1(123));
        assertEquals(1023, solution.solution1(1230));
        assertEquals(162534, solution.solution1(123456));
        assertEquals(100000, solution.solution1(100000));
        assertEquals(110000, solution.solution1(100001));
        assertEquals(948576, solution.solution1(987654));
        assertEquals(95867, solution.solution1(98765));
    }
    
    @Test
    public void solution2() {
        assertEquals(2, solution2(-3,-2,2,4,5));
        assertEquals(3, solution2(-1, 1, 3, 4, 4, 5,-3));
        assertEquals(5, solution2(-5, 2, 3, 4, 5));
        assertEquals(0, solution2(-1, 2, 3, 4, 5));
        assertEquals(1, solution2(-1, 1, 2, 3, 4, 5));
        assertEquals(2, solution2(1,1,1,1,-1, -2, 2, 3, 4, 5));
        assertEquals(1, solution2(-1,0,1, 4, 5));
    }
    
    @Test
    @Disabled  // not implemented yet
    public void solution3() {
        
        assertEquals(64, solution3("00:00:00", "23:59:59"));
        
        assertEquals(64, solution3("00:00:00", "11:11:11"));
        
        assertEquals(16, solution3("10:00:00", "10:11:11"));
        assertEquals(32, solution3("10:00:00", "11:11:11"));

        
        assertEquals(4, solution3("10:01:00", "10:01:59"));
        assertEquals(4, solution3("10:00:00", "10:00:11"));
        assertEquals(3, solution3("10:00:00", "10:00:10"));
        assertEquals(2, solution3("10:00:00", "10:00:09"));
        assertEquals(2, solution3("10:00:00", "10:00:01"));
        
        assertEquals(4, solution3("00:01:00", "00:01:59"));
        assertEquals(4, solution3("00:00:00", "00:00:11"));
        assertEquals(3, solution3("00:00:00", "00:00:10"));
        assertEquals(2, solution3("00:00:00", "00:00:09"));
        assertEquals(2, solution3("00:00:00", "00:00:01"));

        assertEquals(16, solution3("00:01:00", "10:01:59"));
        assertEquals(16, solution3("00:00:00", "10:00:11"));
        assertEquals(12, solution3("00:00:00", "10:00:10"));
        assertEquals(8, solution3("00:00:00", "10:00:09"));
        assertEquals(8, solution3("00:00:00", "10:00:01"));        

        
    }

    private int solution2(int... values) {
        return new Solutions().solution2(values);
    }
    
    private int solution3(String start, String end) {
        return new Solutions().solution3(start, end);
    }
}
