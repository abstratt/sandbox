package emerald;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class Tester {
    @Test
    public void test1() {
        TimeSlotSolver p1 = new TimeSlotSolver();
        List<String> times = new ArrayList<>();
        times.add("Mon 01:00-23:00");
        times.add("Tue 01:00-23:00");
        times.add("Wed 01:00-23:00");
        times.add("Thu 01:00-23:00");
        times.add("Fri 01:00-23:00");
        times.add("Sat 01:00-23:00");
        times.add("Sun 01:00-21:00");
        Assertions.assertEquals(180, p1.solution(StringUtils.join(times, "\n")));
    }
    
    @Test
    public void test1a() {
        TimeSlotSolver p1 = new TimeSlotSolver();
        List<String> times = new ArrayList<>();
        times.add("Mon 01:00-23:00");
        times.add("Fri 00:00-23:00");
        times.add("Sun 01:00-21:00");
        times.add("Tue 01:00-24:00");
        Assertions.assertEquals(2 * 24 * 60, p1.solution(StringUtils.join(times, "\n")));
    }
    
    @Test
    public void test2() {
        Assertions.assertEquals(0, TimeSlotSolver.TimeSlot.toMinuteOffset(0,  "00:00"));
        Assertions.assertEquals(24 * 60, TimeSlotSolver.TimeSlot.toMinuteOffset(0,  "24:00"));
        Assertions.assertEquals(24 * 60, TimeSlotSolver.TimeSlot.toMinuteOffset(1,  "00:00"));
        Assertions.assertEquals(48 * 60, TimeSlotSolver.TimeSlot.toMinuteOffset(1,  "24:00"));
        Assertions.assertEquals(48 * 60, TimeSlotSolver.TimeSlot.toMinuteOffset(2,  "00:00"));
        Assertions.assertEquals(12 * 60, TimeSlotSolver.TimeSlot.toMinuteOffset(0,  "12:00"));
        Assertions.assertEquals(24 * 60 + 12 * 60, TimeSlotSolver.TimeSlot.toMinuteOffset(1,  "12:00"));
        Assertions.assertEquals(7 * 24 * 60, TimeSlotSolver.TimeSlot.toMinuteOffset(6,  "24:00"));
    }
}
