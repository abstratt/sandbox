package virtualmind;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DateBucketTest {
    @Test
    public void test() {
        ZonedDateTime start = ZonedDateTime.of(2020, 01, 10, 13, 30, 0, 0, ZoneOffset.UTC.normalized());
        ZonedDateTime end = start.plusHours(2);
        List<DateBucket> buckets = DateBucket.bucketize(start, end, 30, ChronoUnit.MINUTES);
        Assertions.assertEquals(5, buckets.size());
        Assertions.assertEquals(new DateBucket(start.toInstant(), start.plusMinutes(30).toInstant()), buckets.get(0));
        Assertions.assertEquals(new DateBucket(end.toInstant(), end.plusMinutes(30).toInstant()), buckets.get(4));
    }

}
