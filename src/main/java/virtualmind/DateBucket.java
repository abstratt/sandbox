package virtualmind;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DateBucket {
	Instant from;
	Instant to;

	@Override
	public String toString() {
		return "DateBucket{" +
			   "from=" + from +
			   ", to=" + to +
			   '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DateBucket that = (DateBucket) o;
		return Objects.equals(from, that.from) &&
			   Objects.equals(to, that.to);
	}

	@Override
	public int hashCode() {
		return Objects.hash(from, to);
	}

	public DateBucket(Instant from, Instant to) {
		this.from = from;
		this.to = to;
	}

	public static List<DateBucket> bucketize(
			ZonedDateTime fromDate,
			ZonedDateTime toDate,
			int bucketSize,
			ChronoUnit bucketSizeUnit) {

		Instant fromInstant = fromDate.toInstant();
		Instant toInstant = toDate.toInstant();
		Function<Instant, Instant> extend = from -> from.plus(bucketSize, bucketSizeUnit);
		DateBucket firstBucket = new DateBucket(fromInstant, extend.apply(fromInstant));
		Predicate<DateBucket> stop = bucket -> !toInstant.isBefore(bucket.from);
		List<DateBucket> collected = new ArrayList<>();
		// in Java 8, we must emulate takeWhile
		Stream.iterate(firstBucket, previous -> new DateBucket(previous.to, extend.apply(previous.to)))//
			.peek(it -> { if (stop.test(it)) collected.add(it); })//
			.allMatch(stop);
		return collected;
	}
}
