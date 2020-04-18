package top_phrases;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Computes an ordered frequency distribution of string values found in a CSV
 * file.
 */
public class TopPhrases {
    private final static Logger LOGGER = Logger.getLogger("defaultLogger");
    /**
     * Helper functional interface for turning IOExceptions into
     * UncheckedIOExceptions.
     */
    interface IOFunction<T> {
        T run() throws IOException;

        static <T> T call(IOFunction<T> op) {
            try {
                return op.run();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    /**
     * Helper void-returning functional interface for turning IOExceptions into
     * UncheckedIOExceptions.
     */
    interface IOProcedure {
        void run() throws IOException;

        static void call(IOProcedure op) {
            IOFunction.call(() -> {
                op.run();
                return null;
            });
        }
    }

    /**
     * The different ways to configure the analysis.
     */
    public static class Settings {
        /** Number of threads used for collecting input phrases into buckets. */
        public int numberOfThreads = 2;
        /** Number of files (buckets) to separate input phrases into. */
        public int numberOfBuckets = 16;
        public int maxResults = 100000;
        public int readBufferSize = 1024 * 8;
        public int writeBufferSize = 1024 * 8;
        public int phrasesInBatch = 8;
        public int maxPhraseLength = 1000;
        // Each bucket buffers data up to this limit
        public int lineBufferSize = 5000;
        public char phraseDelimiter = '|';
        public boolean preserveBucketFiles;
        /** Only in effect if non-negative. */
        public int maxInputLines = -1;
        public int maxResultsToShow = 100000;

        public File workDir = new File(new File(System.getProperty("java.io.tmpdir")), getClass().getName());
    }
    
    /**
     * An active object that performs the processing required to collect a
     * phrase into a bucket.
     * 
     * A bucket group manages a subset of the all buckets.
     * 
     * This object runs on its own thread, and all work done from here on runs
     * in this thread.
     */
    private class BucketGroup {
        private Integer key;
        private ExecutorService executorService;
        private Map<Integer, Bucket> buckets = new LinkedHashMap<>();
        private BlockingQueue<String> delayedWrites = new LinkedBlockingQueue<>(); 

        public BucketGroup(Integer key) {
            this.key = key;
            this.executorService = Executors.newSingleThreadExecutor(threadFactory);
            LOGGER.fine(() -> "Created bucket group " + key);
        }

        public void collectPhrase(String phrase) {
            delayedWrites.add(phrase);
            // we batch phrases up to  point
            if (delayedWrites.size() > settings.phrasesInBatch) {
                LOGGER.fine(() -> delayedWrites.size() + " delayed writes for bucket group " + key + ", committing");
                commit();
            }
        }

        private void commit() {
            List<String> batch = new LinkedList<>();
            delayedWrites.drainTo(batch);
            this.executorService.submit(() -> {
                batch.forEach(next -> 
                    getBucket(getBucketId(next)).collect(next)
                );
            });
            LOGGER.fine(() -> "Bucket group " + key + " committing " + batch.size() + " phrases");
        }
        
        public void doneReading() {
            commit();
        }

        /**
         * Collects the results for all buckets under this group.
         * 
         * The results, when available, will be fed into the given supplier.
         * 
         * @param resultConsumer
         */
        public void collectResults(Consumer<Queue<PhraseFrequency>> resultConsumer) {
            this.executorService.submit(() -> {
                Queue<PhraseFrequency> results = computeResults();
                resultConsumer.accept(results);
            });
        }

        /**
         * Collects the results combined from all buckets in this group.
         */
        private Queue<PhraseFrequency> computeResults() {
            LOGGER.fine("Collecting results for group " + this.key);
            // flush before we try reading the buckets
            this.buckets.values().forEach(it -> it.flush());
            Queue<PhraseFrequency> results = buckets.values().stream().map(it -> it.getResults(settings.maxResults))
                    .reduce(new LinkedList<>(), (a, b) -> combineResults(a, b));
            LOGGER.fine("Results collected for group " + this.key);
            return results;
        }

        /**
         * Obtains a bucket, creating it if needed.
         */
        private Bucket getBucket(int bucketId) {
            return buckets.computeIfAbsent(bucketId, key -> new Bucket(settings.workDir, bucketId, settings.writeBufferSize));
        }

        /**
         * A bucket is a working storage for a subset of the phrases to process
         * that are small enough to fit into RAM.
         * 
         * Operations in this class are generally not thread-safe.
         */
        private class Bucket {
            private static final String BUCKET_FILE_EXTENSION = ".bucket";

            private int bucketId;

            /** Backlog of phrases that have yet to be written to disk. */
            List<String> unprocessed = new ArrayList<String>();
            private int unprocessedLength;
            private int writeBufferSize;

            private File workDir;


            public Bucket(File workDir, int bucketId, int writeBufferSize) {
                this.bucketId = bucketId;
                this.workDir = workDir;
                this.unprocessed = new LinkedList<>();
                this.writeBufferSize = writeBufferSize;
                LOGGER.fine(() -> "Created bucket " + bucketId);
            }

            /**
             * Collects the given phrase into this bucket.
             * 
             * @param phrase
             */
            public void collect(String phrase) {
                if (phrase.length() + unprocessedLength > writeBufferSize)
                    flush();
                unprocessed.add(phrase);
                // include newline
                unprocessedLength += phrase.length() + 1;
            }

            /**
             * Saves the sentences currently in the backlog to disk.
             */
            private void flush() {
                if (unprocessed.isEmpty())
                    return;
                LOGGER.fine(() -> "Bucket " + bucketId + " flushed " + unprocessed.size() + " phrases");
                IOProcedure.call(() -> Files.write(getBucketPath(), unprocessed, StandardOpenOption.APPEND,
                        StandardOpenOption.CREATE));
                unprocessed.clear();
                unprocessedLength = 0;
            }

            private Path getBucketPath() {
                return new File(workDir, Integer.toHexString(this.bucketId) + BUCKET_FILE_EXTENSION).toPath();
            }

            public Queue<PhraseFrequency> getResults(int maxResults) {
                Map<String, PhraseFrequency> frequenciesByPhrase = new LinkedHashMap<>(maxResults);
                IOFunction.call(() -> Files.lines(getBucketPath())).forEach(phrase -> {
                    PhraseFrequency frequency = frequenciesByPhrase.computeIfAbsent(phrase,
                            key -> new PhraseFrequency(phrase, 0));
                    frequency.frequency++;
                });
                PriorityQueue<PhraseFrequency> results = new PriorityQueue<>(maxResults);
                frequenciesByPhrase.values().forEach(it -> results.add(it));
                return results;
            }
        }
    }

    private Map<Integer, BucketGroup> bucketGroups = Collections.synchronizedMap(new LinkedHashMap<>());
    private ThreadFactory threadFactory = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            thread.setName("custom");
            return thread;
        }
    };
    private Settings settings;
    
    public TopPhrases(Settings settings) {
        this.settings = settings;
    }

    /**
     * Collects the given phrase into its corresponding bucket.
     * 
     * @param phrase
     * @throws IOException
     */
    private void collect(String phrase) {
        if (!phrase.isEmpty()) {
            int bucketId = getBucketId(phrase);
            getBucketGroup(bucketId).collectPhrase(phrase);
        }
    }

    private BucketGroup getBucketGroup(int bucketId) {
        Integer groupId = getBucketGroupId(bucketId);
        return bucketGroups.computeIfAbsent(groupId, key -> new BucketGroup(key));
    }

    private int getBucketGroupId(int bucketId) {
        return bucketId % settings.numberOfThreads;
    }

    private int getBucketId(String phrase) {
        return Math.abs(phrase.hashCode()) % settings.numberOfBuckets;
    }

    /**
     * Returns a stream with the frequency information for most commons strings.
     * 
     * @return
     */
    public Queue<PhraseFrequency> findMostCommonStrings(File dataFile) {
        try (Stream<String> fileLines = readFileAsLines(dataFile)) {
            return findMostCommonStrings(fileLines);
        }
    }

    public Queue<PhraseFrequency> findMostCommonStrings(Stream<String> lines) {
        LOGGER.fine("Starting");
        setUp();
        if (settings.maxInputLines >= 0)
            lines = lines.limit(settings.maxInputLines);
        processLines(lines);
        Queue<PhraseFrequency> results = collectResults();
        if (!settings.preserveBucketFiles)
            deleteBucketFiles();
        return results;
    }

    private void setUp() {
        settings.workDir.mkdirs();
        // in case there are bucket files left over from a previous run
        deleteBucketFiles();
        for (int i = 0; i < settings.numberOfThreads; i++)
            bucketGroups.put(i, new BucketGroup(i));
    }

    private void deleteBucketFiles() {
        LOGGER.fine("Deleting old buckets");
        Stream<Path> oldBuckets = IOFunction
                .call(() -> Files
                        .find(settings.workDir.toPath(), 2,
                                (path, attributes) -> attributes.isDirectory()
                                        || (attributes.isRegularFile() && isBucketFile(path))))
                .filter(it -> isBucketFile(it));
        long deleted = oldBuckets.map(it -> {
            IOProcedure.call(() -> Files.delete(it));
            return 1;
        }).count();
        LOGGER.fine(() -> "Deleted " + deleted + " old buckets");
    }

    private boolean isBucketFile(Path path) {
        return path.toFile().getName().endsWith(BucketGroup.Bucket.BUCKET_FILE_EXTENSION);
    }

    /**
     * Collects the results combined from all bucket groups.
     */
    private Queue<PhraseFrequency> collectResults() {
        LOGGER.info("Collecting results");
        BlockingQueue<Queue<PhraseFrequency>> groupResults = new LinkedBlockingQueue<>(bucketGroups.size());
        Semaphore rendezvouz = new Semaphore(0);
        bucketGroups.values().forEach(it -> it.collectResults(collected -> {
            Queue<PhraseFrequency> existing = groupResults.poll();
            if (existing != null) {
                // found one lying there, combine and addthe result to the queue
                groupResults.add(combineResults(existing, collected));
            } else {
                // none available, just add to the queue
                groupResults.add(collected);
            }
            rendezvouz.release();
        }));
        LOGGER.info("Waiting for results from bucket groups");
        try {
            rendezvouz.acquire(bucketGroups.size());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("Combining " + groupResults.size() + " from " + bucketGroups.size() + " bucket groups");
        Queue<PhraseFrequency> results = groupResults.stream().reduce(new LinkedList<>(), this::combineResults);
        LOGGER.info("Final results collected");
        return results;
    }

    /**
     * Combines two results.
     * 
     * @param first
     * @param second
     * @return
     */
    private Queue<PhraseFrequency> combineResults(Queue<PhraseFrequency> first, Queue<PhraseFrequency> second) {
        int resultSize = Math.min(settings.maxResults, first.size() + second.size());
        LinkedList<PhraseFrequency> combined = new LinkedList<>();
        for (int i = 0; i < resultSize && !first.isEmpty() && !second.isEmpty(); i++) {
            Queue<PhraseFrequency> elected = (first.peek().frequency >= second.peek().frequency) ? first : second;
            combined.add(elected.remove());
        }
        Consumer<Queue<PhraseFrequency>> drainer = toDrain -> {
            while (combined.size() < resultSize && !toDrain.isEmpty())
                combined.add(toDrain.remove());
        };
        drainer.accept(first);
        drainer.accept(second);
        return combined;
    }

    private void postLine(String line, int lineNumber) {
        StringTokenizer tokenizer = new StringTokenizer(line, " " + settings.phraseDelimiter);
        while (tokenizer.hasMoreTokens()) {
            String phrase = tokenizer.nextToken();
            if (phrase.length() > settings.maxPhraseLength)
                throw new IllegalArgumentException("Phrase is too long (" + phrase.length() + "): " + phrase); 
            collect(phrase.toLowerCase());
        }
    }

    /**
     * Process lines from a stream. Returns when all lines have been seen and
     * farmed out to their corresponding bucket group.
     */
    private void processLines(Stream<String> lines) {
        LOGGER.info("Processing lines");
        AtomicInteger lineCount = new AtomicInteger();
        lines.forEach(line -> {
            postLine(line, lineCount.incrementAndGet());
        });
        LOGGER.fine("Done reading input");
        bucketGroups.values().forEach(it -> it.doneReading());
        LOGGER.info("Processed " + lineCount.get() + " lines");
    }

    private Stream<String> readFileAsLines(File dataFile) {
        if (dataFile == null) {
            throw new IllegalStateException("No data file set");
        }
        if (!dataFile.isFile()) {
            throw new IllegalStateException("Path is not a file: " + dataFile.getAbsolutePath());
        }

        return IOFunction.call(() -> {
            BufferedReader reader = new BufferedReader(new FileReader(dataFile), settings.readBufferSize);
            return reader.lines().onClose(() -> IOProcedure.call(() -> reader.close()));
        });
    }

    /**
     * Represents the number of occurrences for a phrase.
     */
    public static class PhraseFrequency implements Comparable<PhraseFrequency> {
        private final String phrase;
        private int frequency;

        public PhraseFrequency(String phrase, int frequency) {
            super();
            this.phrase = phrase;
            this.frequency = frequency;
        }

        @Override
        public int compareTo(PhraseFrequency o) {
            if (frequency != o.frequency) {
                // phrases with higher frequencies should show first
                return -Integer.compare(frequency, o.frequency);
            }
            return phrase.compareTo(o.phrase);
        }

        @Override
        public String toString() {
            return phrase + " (" + frequency + ")";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + frequency;
            result = prime * result + ((phrase == null) ? 0 : phrase.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            PhraseFrequency other = (PhraseFrequency) obj;
            if (frequency != other.frequency)
                return false;
            if (phrase == null) {
                if (other.phrase != null)
                    return false;
            } else if (!phrase.equals(other.phrase))
                return false;
            return true;
        }
    }

    public static void main(String[] args) throws IOException {
        Settings settings = configureFromCLI();
        TopPhrases analyzer = new TopPhrases(settings);
        Queue<PhraseFrequency> frequencies;
        if (args.length == 0) {
            LOGGER.info("Running in test mode");
            String testString = System.getProperty("testString",
                    "banana|orange| banana\n||strawberry| strawberry \n avocado");
            Stream<String> lines = Arrays.asList(testString.split("\\n")).stream();
            frequencies = analyzer.findMostCommonStrings(lines);
        } else {
            File dataFile = new File(args[0]);
            frequencies = analyzer.findMostCommonStrings(dataFile);
        }
        frequencies.stream().limit(settings.maxResultsToShow).forEach(System.out::println);
        LOGGER.fine("Completed");
    }

    private static Settings configureFromCLI() {
        Settings settings = new Settings();
        settings.phrasesInBatch = Integer.parseInt(System.getProperty("phrasesInBatch", "" + settings.phrasesInBatch));
        settings.readBufferSize = Integer.parseInt(System.getProperty("readBufferSize", "" + settings.readBufferSize));
        settings.writeBufferSize = Integer
                .parseInt(System.getProperty("writeBufferSize", "" + settings.writeBufferSize));
        settings.numberOfThreads = Integer
                .parseInt(System.getProperty("numberOfThreads", "" + settings.numberOfThreads));
        settings.numberOfBuckets = Integer
                .parseInt(System.getProperty("numberOfBuckets", "" + settings.numberOfBuckets));
        settings.maxInputLines = Integer.parseInt(System.getProperty("maxInputLines", "" + settings.maxInputLines));
        settings.maxResults = Integer.parseInt(System.getProperty("maxResults", "" + settings.maxResults));
        settings.preserveBucketFiles = Boolean
                .parseBoolean(System.getProperty("preserveBucketFiles", "" + settings.preserveBucketFiles));
        settings.maxResultsToShow = Integer.parseInt(System.getProperty("maxResultsToShow", "" + settings.maxResults));
        settings.phraseDelimiter = (char) System.getProperty("phraseDelimiter", "" + settings.phraseDelimiter).chars()
                .findFirst().orElse(settings.phraseDelimiter);
        return settings;
    }
}
