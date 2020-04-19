package toptal_warmup;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    public boolean solve() {
        return false;
    }

    public List<Integer> getChange(double money, double price) {
        int totalChangeInCents = (int) Math.round((money - price) * 100);
        //1c, 5c, 10c, 25c, 50c, and $1
        int[] denominations = {100, 50, 25, 10, 5, 1};
        List<Integer> result = new ArrayList<Integer>(denominations.length);
        
        int remainingChange = totalChangeInCents;
        for (int i = 0; i < denominations.length; i++) {
            int count = remainingChange / denominations[i];
            result.add(0, count);
            remainingChange -= count * denominations[i];
        }
        return result;
    }

    public int solveDeck(String[] cards) {
        int cardsPerSuit = 13;
        int[][] countsPerSuit = new int[4][cardsPerSuit];
        for (String cardString : cards) {
            Card card = new Card(cardString);
            countsPerSuit[card.suit][card.rank]++;
        }
        int decks = cards.length;
        for (int i = 0; i < countsPerSuit.length; i++) {
            for (int j = 0; j < countsPerSuit[i].length; j++) {
                decks = Math.min(decks, countsPerSuit[i][j]);
            }
        }
        return decks;
    }
    
    private static class Card {
        static String suits = "SCHD";
        static String ranks= "23456789TJQKA";

        int suit;
        int rank;
        public Card(String cardString) {
            rank = ranks.indexOf(cardString.charAt(0));
            suit = suits.indexOf(cardString.charAt(1));
        }
    }
}
