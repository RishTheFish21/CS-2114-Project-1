package blackjack;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the cards held by the player or the dealer and calculates their Blackjack total.
 *
 * @author Aniket
 * @version 1.1
 */
public class Hand {

    private static final int BLACKJACK = 21;
    private static final int ACE_REDUCTION = 10;

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
     * <p>Every Ace starts out worth 11. While the total is over 21 and a
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
     * @return true if the hand is a natural Blackjack
     */
    public boolean isNaturalBlackjack() {
        return cards.size() == 2 && calculateTotal() == BLACKJACK;
    }

    /**
     * Checks whether this hand has gone over 21.
     *
     * @return true if the total is over 21
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
     * Returns the cards in this hand.
     *
     * @return the list of cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Draws every card in this hand as ASCII art, side by side.
     *
     * @return the hand as multi-line ASCII art
     */
    public String toAsciiArt() {
        List<String[]> arts = new ArrayList<>();
        for (Card card : cards) {
            arts.add(card.toAsciiArt());
        }
        return Card.renderSideBySide(arts);
    }

    /**
     * Returns the cards in this hand as a comma-separated list,
     *
     * @return the hand as a string
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