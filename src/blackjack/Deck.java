package blackjack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Represents a standard 52-card deck of playing cards.
 *
 * @author Syed Rishad
 * @version 0.1
 */
public class Deck {

    private static final String[] RANKS = {
        "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", 
        "Ace"
    };

    private static final String[] SUITS = {"Hearts", "Diamonds", "Clubs",
        "Spades"};

    private List<Card> cards;

    /**
     * Creates a new deck containing all 52 cards in order.
     */
    public Deck() {
        cards = new ArrayList<>();
        initializeDeck();
    }

    /**
     * Fills the deck with one card for every rank and suit combination.
     */
    private void initializeDeck() {
        cards.clear();
        for (String suit : SUITS) {
            for (String rank : RANKS) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    /**
     * Shuffles the cards into a random order.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Removes and returns the top card of the deck. If the deck is empty, it is
     * rebuilt with all 52 cards and shuffled before drawing.
     *
     * @return the top card
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            System.out.println("[System] Reshuffling deck...");
            initializeDeck();
            shuffle();
        }
        return cards.remove(0);
    }

    /**
     * Returns how many cards are left in the deck.
     *
     * @return the number of remaining cards
     */
    public int remainingCards() {
        return cards.size();
    }
}
