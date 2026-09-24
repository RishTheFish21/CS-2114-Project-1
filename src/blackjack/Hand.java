package blackjack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents the cards held by the player or the dealer and calculates their Blackjack total.
 *
 * @author Rishad Wyatt
 * @version 1.0
 */
public class Hand {

    /** The best possible hand total. */
    private static final int BLACKJACK = 21;

    /** Amount subtracted when an Ace changes from 11 to 1. */
    private static final int ACE_REDUCTION = 10;

    /** The cards in this hand. */
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
     * @throws NullPointerException if {@code card} is null
     */
    public void addCard(Card card) {
        cards.add(Objects.requireNonNull(card, "Cannot add a null card to a hand"));
    }

    /**
     * Calculates the best Blackjack total for this hand.
     *
     * <p>Every Ace starts out worth 11 (a "soft" Ace). While the total is over 21 and a
     * soft Ace remains, that Ace is changed to 1 by subtracting 10.</p>
     *
     * @return the total value of the hand
     */
    public int calculateTotal() {
        int total = 0;
        int softAces = 0;

        for (Card card : cards) {
            total += card.getBaseValue();
            if ("Ace".equals(card.getRank())) {
                softAces++;
            }
        }

        while (total > BLACKJACK && softAces > 0) {
            total -= ACE_REDUCTION;
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
        return cards.size() == 2 && calculateTotal() == BLACKJACK;
    }

    /**
     * Checks whether this hand has gone over 21.
     *
     * @return {@code true} if the total is over 21
     */
    public boolean isBust() {
        return calculateTotal() > BLACKJACK;
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
     * Returns the cards in this hand as a comma-separated list,
     * e.g. "Ace of Spades, 7 of Hearts".
     *
     * @return the hand as a string, or an empty string if the hand is empty
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(cards.get(i));
        }
        return sb.toString();
    }
}
