package ojolabs;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

class DateBucket {
	public DateBucket(Instant from, Instant to) {
		super();
		this.from = from;
		this.to = to;
	}

	final Instant from;
	final Instant to;

	public Instant getFrom() {
		return from;
	}

	public Instant getTo() {
		return to;
	}

	public static List<DateBucket> bucketizeOriginal(ZonedDateTime fromDate, ZonedDateTime toDate, int bucketSize,
			ChronoUnit bucketSizeUnit) {
		List<DateBucket> buckets = new ArrayList<>();
		boolean reachedDate = false;
		for (int i = 0; !reachedDate; i++) {
			ZonedDateTime minDate = fromDate.plus(i * bucketSize, bucketSizeUnit);
			ZonedDateTime maxDate = fromDate.plus((i + 1) * bucketSize, bucketSizeUnit);
			reachedDate = toDate.isBefore(maxDate);
			buckets.add(new DateBucket(minDate.toInstant(), maxDate.toInstant()));
		}

		return buckets;
	}

	public static List<DateBucket> bucketizeUsingStream(ZonedDateTime fromDate, ZonedDateTime toDate, int bucketSize,
			ChronoUnit bucketSizeUnit) {

		Instant fromInstant = fromDate.toInstant();
		Instant toInstant = toDate.toInstant();
		Function<Instant, Instant> extend = from -> from.plus(bucketSize, bucketSizeUnit);
		DateBucket firstBucket = new DateBucket(fromInstant, extend.apply(fromInstant));
		// in Java 9, we can use takeWhile
		return Stream.iterate(firstBucket, previous -> new DateBucket(previous.to, extend.apply(previous.to)))//
				.takeWhile(bucket -> !toInstant.isBefore(bucket.from))//
				.collect(Collectors.toList());
	}

}
