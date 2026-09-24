package blackjack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the cards held by the player or the dealer and calculates their Blackjack total.
 *
 * @author rishad wyatt
 * @version 0.1
 */
public class Hand {

    private List<Card> cards;

    /**
     * Creates an empty hand.
     */
    public Hand() {
        cards = new ArrayList<>();
    }

    /**
     * Adds a card to this hand.
     *
     * @param card the card to add
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Calculates the best Blackjack total for this hand.
     *
     *
     * @return the total value of the hand
     */
    public int calculateTotal() {
        int total = 0;
        int softAces = 0;

        for (Card card : cards) {
            total += card.getBaseValue();
            if (card.getRank().equals("Ace")) {
                softAces++;
            }
        }

        while (total > 21 && softAces > 0) {
            total -= 10;
            softAces--;
        }
        return total;
    }

    /**
     * Checks whether this hand is a natural Blackjack (21 with exactly two cards).
     *
     * @return {@code true} if the hand is a natural Blackjack
     */
    public boolean isNaturalBlackjack() {
        return cards.size() == 2 && calculateTotal() == 21;
    }

    /**
     * Checks whether this hand has gone over 21.
     *
     * @return {@code true} if the total is over 21
     */
    public boolean isBust() {
        return calculateTotal() > 21;
    }

    /**
     * Removes all cards from this hand.
     */
    public void clear() {
        cards.clear();
    }

    /**
     * Returns the cards in this hand. The returned list cannot be modified.
     *
     * @return a read-only list of the cards
     */
    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    /**
     * Returns the cards in this hand as a comma-separated list.
     *
     * @return the hand as a string
     */
    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < cards.size(); i++) {
            if (i > 0) {
                result += ", ";
            }
            result += cards.get(i);
        }
        return result;
    }
}
