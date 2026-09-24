package blackjack;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a standard 52-card deck that rebuilds and reshuffles itself when it runs out.
 *
 * @author Aniket
 * @version 1.0
 */
public class Deck {

    private static final int STANDARD_DECK_SIZE = 52;
    private List<Card> cards;

    /**
     * Creates a new deck containing all 52 cards in order.
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
        for (int i = cards.size() - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));

            Card temp = cards.get(i);
            cards.set(i, cards.get(j));
            cards.set(j, temp);
        }
    }

    /**
     * Removes and returns the top card of the deck. If the deck is empty, it is
     * rebuilt with all 52 cards and shuffled first
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
