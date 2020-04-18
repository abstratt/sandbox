package top_phrases;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import top_phrases.TopPhrases.PhraseFrequency;
import top_phrases.TopPhrases.Settings;

public class TopPhrasesTest {

    @Test
    public void testBasic() {
        Settings settings = new TopPhrases.Settings();
        settings.maxResults = 3;
        TopPhrases analyzer = new TopPhrases(settings);
        Queue<PhraseFrequency> frequencies = analyzer.findMostCommonStrings(Stream.of("a", "b", "d", "d", "a", "a", "a", "c", "b", "b"));
        assertEquals(new LinkedList<>(Arrays.asList(new PhraseFrequency("a", 4), new PhraseFrequency("b", 3), new PhraseFrequency("d", 2))), frequencies);
    }
    
    @Test
    public void testCaseSensitivity() {
        Settings settings = new TopPhrases.Settings();
        settings.maxResults = 3;
        TopPhrases analyzer = new TopPhrases(settings);
        Queue<PhraseFrequency> frequencies = analyzer.findMostCommonStrings(Stream.of("C", "A", "a", "c", "a"));
        assertEquals(new LinkedList<>(Arrays.asList(new PhraseFrequency("a", 3), new PhraseFrequency("c", 2))), frequencies);
    }
}
