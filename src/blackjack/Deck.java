package blackjack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a standard 52-card deck that rebuilds and reshuffles itself when it runs out.
 *
 * @author Aniket
 * @version 1.0
 */
public class Deck {

    /** Number of cards in a full deck. */
    private static final int STANDARD_DECK_SIZE = 52;

    /** The cards currently left in the deck. The top of the deck is index 0. */
    private List<Card> cards;

    /**
     * Creates a new deck containing all 52 cards in order.
     * Call {@link #shuffle()} before dealing.
     */
    public Deck() {
        cards = new ArrayList<>(STANDARD_DECK_SIZE);
        initializeDeck();
    }

    /**
     * Removes any remaining cards and fills the deck with one card for every
     * rank and suit combination.
     */
    private void initializeDeck() {
        cards.clear();
        for (String suit : Card.SUITS) {
            for (String rank : Card.RANKS) {
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
     * rebuilt with all 52 cards and shuffled first, so this method never fails.
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
