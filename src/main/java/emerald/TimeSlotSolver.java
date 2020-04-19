package emerald;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TimeSlotSolver {
    
    
    public static class TimeSlot implements Comparable<TimeSlot> {
        private static final int MINUTES_PER_HOUR = 60;
        private static final int HOURS_PER_DAY = 24;
        private static List<String> WEEK_DAYS = Arrays.asList(new String[] {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"});
        private int startMinute;
        private int endMinute;

        public static TimeSlot fromString(String slotAsString) {
            String[] slotComponents = slotAsString.split(" ");
            String weekDayAsString = slotComponents[0];
            String timeRangeAsString = slotComponents[1];
            String[] rangeComponents = timeRangeAsString.split("-");
            String startAsString = rangeComponents[0];
            String endAsString = rangeComponents[1];
            int weekDay = toWeekDay(weekDayAsString);
            return new TimeSlot(toMinuteOffset(weekDay, startAsString), toMinuteOffset(weekDay, endAsString));
        }

        private static int toWeekDay(String weekDayAsString) {
            return WEEK_DAYS.indexOf(weekDayAsString);
        }

        public static int toMinuteOffset(int weekDay, String timeString) {
            String[] timeComponents = timeString.split(":");
            int hour = Integer.parseInt(timeComponents[0]);
            int minute = Integer.parseInt(timeComponents[1]);
            return (weekDay * HOURS_PER_DAY +  hour) * MINUTES_PER_HOUR + minute;
        }

        private TimeSlot(int start, int end) {
            this.startMinute = start;
            this.endMinute = end;
        }

        @Override
        public int compareTo(TimeSlot o) {
            if (this.startMinute != o.startMinute) {
                return this.startMinute - o.startMinute;
            }
            return this.endMinute - o.endMinute;
        }
    }

    public int solution(String string) {
        List<String> timesAsString = Arrays.asList(string.split("\n"));
        List<TimeSlot> timeSlots = timesAsString.stream().map(TimeSlot::fromString).collect(Collectors.toCollection(() -> new ArrayList<>()));
        Collections.sort(timeSlots);
        timeSlots.add(TimeSlot.fromString("Sun 24:00-24:00"));
        int maxGap = 0;
        TimeSlot lastSlot = TimeSlot.fromString("Mon 00:00-00:00"); 
        for (TimeSlot timeSlot : timeSlots) {
            int gap = timeSlot.startMinute - lastSlot.endMinute;
            maxGap = Math.max(maxGap, gap);
            lastSlot = timeSlot;
        }
        return maxGap;
    }
}
