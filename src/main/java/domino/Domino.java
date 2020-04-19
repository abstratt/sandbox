package domino;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class Domino {

    public int solveDomino(String sequence) {
        if ("".equals(sequence)) {
            return 0;
        }
        String[] tiles = sequence.split(",");
        List<String[]> parsedTiles = Arrays.stream(tiles).map(it -> parseTile(it)).collect(Collectors.toList());
        int longestSequence = 1;
        int currentSequence = 1;
        for (int i = 1; i < tiles.length; i++) {
            System.out.println(Arrays.toString(parsedTiles.get(i)));
            String myLeft = parsedTiles.get(i)[0];
            String prevRight = parsedTiles.get(i-1)[1];
            System.out.println(myLeft + "-" + prevRight);
            if (myLeft.equals(prevRight)) {
                currentSequence++;
                if (currentSequence > longestSequence) {
                    longestSequence = currentSequence;
                }
            } else {
                currentSequence = 1;
            }
        }
        return longestSequence;
    }
    
    public String[] parseTile(String tileAsStr) { 
        return tileAsStr.split("-");
                
    }
    public String correctFormat(String paragraph) {
        StringBuffer result = new StringBuffer();
        String sentenceDelimiters = ".?!";
        String inSentenceDelimiters = ",:;";
        String delimiters = " " + sentenceDelimiters + inSentenceDelimiters;
        StringTokenizer sentenceTokenizer = new StringTokenizer(paragraph, delimiters, true);
        boolean newSentence = true;
        while (sentenceTokenizer.hasMoreTokens()) {
            char lastChar = result.length() == 0 ? 0 : result.charAt(result.length() - 1);            
            String nextToken = sentenceTokenizer.nextToken();
            System.out.println("[" + nextToken + "]");
            if (Character.isAlphabetic(nextToken.charAt(0))) {
                if (newSentence) {
                    nextToken = toFirstUpper(nextToken);
                    newSentence = false;
                }
            } else if (" ".equals(nextToken)) {
                if (result.length() == 0 || !(inSentenceDelimiters.indexOf(lastChar) >= 0 || Character.isAlphabetic(result.charAt(result.length() - 1)))) {
                    System.out.println("skipped " + nextToken);
                    continue;
                }
            } else {
                if (sentenceDelimiters.contains(nextToken)) {
                    newSentence = true;
                }
                if (lastChar == ' ') {
                    System.out.println("Deleting last space");
                    result.deleteCharAt(result.length() - 1);
                }
                nextToken = nextToken + " ";
            }
            result.append(nextToken);
            System.out.println("Appending " + nextToken + " -> " + result.toString());
        }
        return result.toString().trim();
    }
    
    private String toFirstUpper(String word) {
        return "" + Character.toUpperCase(word.charAt(0)) + word.toLowerCase().subSequence(1, word.length());
    }
    
}
